package siri_xlite.service.common;

import com.fasterxml.jackson.core.JsonGenerator;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.exceptions.Exceptions;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.infinispan.manager.EmbeddedCacheManager;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import siri_xlite.Configuration;
import siri_xlite.common.JsonUtils;
import siri_xlite.marshaller.json.SiriExceptionMarshaller;
import siri_xlite.repositories.NotModifiedException;

import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public abstract class SiriSubscriber<T, P extends Parameters> implements Subscriber<T>, JsonUtils {

    public static final String MAX_AGE = "max-age=3";
    public static final String S_MAX_AGE = "s-maxage=30";
    public static final String PROXY_REVALIDATE = "proxy-revalidate";
    public static final String PUBLIC = "public";
    public static final String RECCORDED_AT_TIME = "recordedAtTime";

    public static final Comparator<Document> COMPARATOR = Comparator.comparing(t -> {
        return t.getDate(RECCORDED_AT_TIME).getTime();
    });

    protected RoutingContext context;
    protected Configuration configuration;
    protected P parameters;
    protected ByteArrayOutputStream out;
    protected JsonGenerator writer;
    protected Document current;
    protected AtomicInteger count;

    @Autowired
    protected EmbeddedCacheManager manager;

    public static final String getEtag(RoutingContext context) {
        String text = context.request().getHeader(HttpHeaders.IF_NONE_MATCH);
        String[] noneMatch = (StringUtils.isNotEmpty(text)) ? text.split(",") : ArrayUtils.EMPTY_STRING_ARRAY;
        String result = (ArrayUtils.isNotEmpty(noneMatch)) ? noneMatch[0].replaceAll("^\"|\"$", "") : null;
        return result;
    }

    public static final String getEtag(Document document) {
        return document.getObjectId(ID).toHexString();
    }

    public static final String createEtag(List<? extends Document> list) {
        Optional<? extends Document> result = list.stream().max(COMPARATOR);
        return createEtag(result.get());
    }

    public static final String createEtag(Document document) {
        Date recordedAtTime = document.getDate(RECCORDED_AT_TIME);
        return (document != null) ? String.valueOf(recordedAtTime.getTime()) : null;
        // return (document != null) ? document.getObjectId(ID).toHexString() : null;
    }

    public void configure(Configuration configuration, P parameters, RoutingContext context) {
        this.configuration = configuration;
        this.parameters = parameters;
        this.context = context;
        this.out = new ByteArrayOutputStream(1024);
        this.writer = createJsonWriter(out);
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
                        .putHeader(HttpHeaders.CACHE_CONTROL,
                                Arrays.asList(PUBLIC, MAX_AGE, S_MAX_AGE, PROXY_REVALIDATE))
                        .putHeader(HttpHeaders.ETAG, getEtag(context))
                        .setStatusCode(HttpURLConnection.HTTP_NOT_MODIFIED).end();
            } else if (t instanceof SiriException) {
                SiriExceptionMarshaller.getInstance().write(writer, (SiriException) t);
                writer.close();
                this.context.response().putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end(out.toString());
            } else if (t instanceof Throwable) {
                SiriExceptionMarshaller.getInstance().write(writer, SiriException.createOtherError(t));
                writer.close();
                this.context.response().putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end(out.toString());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Exceptions.propagate(e);
        }
    }

    public final boolean noneMatch(Document document) {
        return !StringUtils.equals(getEtag(document), getEtag(context));
    }

}