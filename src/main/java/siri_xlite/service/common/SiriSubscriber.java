package siri_xlite.service.common;

import com.fasterxml.jackson.core.JsonGenerator;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.exceptions.Exceptions;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.infinispan.manager.EmbeddedCacheManager;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import siri_xlite.Configuration;
import siri_xlite.common.Color;
import siri_xlite.common.DateTimeUtils;
import siri_xlite.marshaller.json.SiriExceptionMarshaller;
import siri_xlite.repositories.NotModifiedException;

import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import static siri_xlite.common.JsonUtils.createJsonWriter;


@Slf4j
public abstract class SiriSubscriber<T, P extends DefaultParameters> implements Subscriber<T> {

    @Autowired
    protected EmbeddedCacheManager manager;
    protected Configuration configuration;
    protected DefaultParameters parameters;
    protected RoutingContext context;
    protected ByteArrayOutputStream out;
    protected JsonGenerator writer;
    protected Document current;
    protected AtomicInteger count;

    public void configure(Configuration configuration, P parameters, RoutingContext context) {
        this.configuration = configuration;
        this.parameters = parameters;
        this.context = context;
        this.out = new ByteArrayOutputStream(10 * 1024);
        this.writer = createJsonWriter(this.out);
        this.current = null;
        this.count = new AtomicInteger();
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
    }

    @Override
    public void onError(Throwable t) {
        try {
            if (t instanceof NotModifiedException) {
                writeNotModified();
            } else if (t instanceof SiriException) {
                writeSiriException((SiriException) t);
            } else if (t != null) {
                writeSiriException(SiriException.createOtherError(t));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Exceptions.propagate(e);
        }
    }

    protected void writeSiriException(SiriException e) throws Exception {
        log.error(e.getMessage(), e);
        SiriExceptionMarshaller.getInstance().write(writer, e);
        writer.close();
        this.context.response().putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end(out.toString());
    }

    protected void writeNotModified() throws Exception {
        writer.close();
        HttpServerResponse response = this.context.response()
                .setStatusCode(HttpURLConnection.HTTP_NOT_MODIFIED);
        response.headers().iterator().forEachRemaining(
                (entry) -> log.info(Color.GREEN + entry.getKey() + "=" + entry.getValue() + Color.NORMAL));
        response.end();
    }

    protected void writeNotFound() throws Exception {
        SiriExceptionMarshaller.getInstance().write(writer, SiriException.createInvalidDataReferencesError());
        writer.close();
        this.context.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
                .end(out.toString());
    }

    protected void writeResponse(Date lastModified) {
        this.context.response().putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .putHeader(HttpHeaders.CACHE_CONTROL, Arrays.asList(
                        CacheControl.PUBLIC,
                        CacheControl.MUST_REVALIDATE,
                        CacheControl.PROXY_REVALIDATE,
                        CacheControl.S_MAX_AGE + parameters.getSMaxAge(),
                        CacheControl.MAX_AGE + parameters.getMaxAge()))
                .putHeader(HttpHeaders.LAST_MODIFIED, DateTimeUtils.toRFC1123(lastModified))
                .end(out.toString());
    }
}