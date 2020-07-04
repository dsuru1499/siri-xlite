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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public abstract class SiriSubscriber<T, P extends DefaultParameters> implements Subscriber<T>, JsonUtils {

    static final String PROXY_REVALIDATE = "proxy-revalidate";
    static final String PUBLIC = "public";
    private static final String RECCORDED_AT_TIME = "recordedAtTime";

    static final Comparator<Document> COMPARATOR = Comparator.comparing(t -> t.getDate(RECCORDED_AT_TIME).getTime());

    protected RoutingContext context;
    protected JsonGenerator writer;
    @Autowired
    protected EmbeddedCacheManager manager;
    Configuration configuration;
    DefaultParameters parameters;
    ByteArrayOutputStream out;
    Document current;
    AtomicInteger count;

    public static String getEtag(RoutingContext context) {
        String text = context.request().getHeader(HttpHeaders.IF_NONE_MATCH);
        String[] noneMatch = (StringUtils.isNotEmpty(text)) ? text.split(",") : ArrayUtils.EMPTY_STRING_ARRAY;
        return (ArrayUtils.isNotEmpty(noneMatch)) ? noneMatch[0].replaceAll("^\"|\"$", "") : null;
    }

    private static String getEtag(Document document) {
        return document.getObjectId(ID).toHexString();
    }

    public static String createEtag(List<? extends Document> list) {
        Optional<? extends Document> result = list.stream().max(COMPARATOR);
        return result.map(SiriSubscriber::createEtag).orElse(null);
    }

    static String createEtag(Document document) {
        return (document != null) ? String.valueOf(document.getDate(RECCORDED_AT_TIME).getTime()) : null;
    }

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
                        .putHeader(HttpHeaders.CACHE_CONTROL,
                                Arrays.asList(PUBLIC, parameters.getMaxAge(), parameters.getSMaxAge(),
                                        PROXY_REVALIDATE))
                        .putHeader(HttpHeaders.ETAG, getEtag(context))
                        .setStatusCode(HttpURLConnection.HTTP_NOT_MODIFIED).end();
            } else if (t instanceof SiriException) {
                SiriExceptionMarshaller.getInstance().write(writer, (SiriException) t);
                writer.close();
                this.context.response().putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end(out.toString());
            } else if (t != null) {
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

    public final boolean noneMatch(Document document) {
        return !StringUtils.equals(getEtag(document), getEtag(context));
    }

}