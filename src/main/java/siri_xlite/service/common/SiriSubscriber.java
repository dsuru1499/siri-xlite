package siri_xlite.service.common;

import com.fasterxml.jackson.core.JsonGenerator;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.exceptions.Exceptions;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.infinispan.manager.EmbeddedCacheManager;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import siri_xlite.Configuration;
import siri_xlite.common.DateTimeUtils;
import siri_xlite.common.JsonUtils;
import siri_xlite.marshaller.json.SiriExceptionMarshaller;
import siri_xlite.repositories.NotModifiedException;

import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
public abstract class SiriSubscriber<T, P extends DefaultParameters> implements Subscriber<T>, JsonUtils {

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
                writer.close();
                this.context.response().putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .putHeader(HttpHeaders.CACHE_CONTROL, Arrays.asList(
                                CacheControl.MAX_AGE + parameters.getMaxAge(),
                                CacheControl.S_MAX_AGE + parameters.getSMaxAge()))
                        .putHeader(HttpHeaders.LAST_MODIFIED, DateTimeUtils.toRFC1123(CacheControl.getLastModified(context)))
                        .setStatusCode(HttpURLConnection.HTTP_NOT_MODIFIED).end();
            } else if (t instanceof SiriException) {
                log.error(t.getMessage(), t);
                SiriExceptionMarshaller.getInstance().write(writer, (SiriException) t);
                writer.close();
                this.context.response().putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end(out.toString());
            } else if (t != null) {
                log.error(t.getMessage(), t);
                SiriExceptionMarshaller.getInstance().write(writer, SiriException.createOtherError(t));
                writer.close();
                this.context.response().putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end(out.toString());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Exceptions.propagate(e);
        }
    }
}