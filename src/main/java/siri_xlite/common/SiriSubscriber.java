package siri_xlite.common;

import com.fasterxml.jackson.core.JsonGenerator;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.rxjava3.exceptions.Exceptions;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import siri_xlite.common.marshaller.json.SiriExceptionMarshaller;

import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Date;

import static siri_xlite.common.JsonUtils.createJsonWriter;

@Slf4j
public abstract class SiriSubscriber<T, P extends DefaultParameters> implements Subscriber<T> {

    @Autowired
    protected P parameters;
    protected RoutingContext context;
    protected ByteArrayOutputStream out;
    protected JsonGenerator writer;

    public void configure(P parameters, RoutingContext context) {
        this.parameters = parameters;
        this.context = context;
        this.out = new ByteArrayOutputStream(10 * 1024);
        this.writer = createJsonWriter(this.out);
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
        HttpServerResponse response = this.context.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setStatusCode(HttpResponseStatus.BAD_REQUEST.code());
        log(response);
        response.end(out.toString());
    }

    protected void writeNotFound() throws Exception {
        SiriExceptionMarshaller.getInstance().write(writer, SiriException.createInvalidDataReferencesError());
        writer.close();
        HttpServerResponse response = this.context.response()
                .setStatusCode(HttpResponseStatus.NOT_FOUND.code());
        log(response);
        response.end(out.toString());
    }

    protected void writeNotModified() throws Exception {
        writer.close();
//        log(context.request());
        HttpServerResponse response = this.context.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .putHeader(HttpHeaders.CACHE_CONTROL, Arrays.asList(
                        CacheControl.MUST_REVALIDATE,
                        CacheControl.PROXY_REVALIDATE,
                        CacheControl.S_MAX_AGE + parameters.getSMaxAge(),
                        CacheControl.MAX_AGE + parameters.getMaxAge()))
                .putHeader(HttpHeaders.LAST_MODIFIED, DateTimeUtils.toRFC1123(CacheControl.getLastModified(context)))
                .setStatusCode(HttpURLConnection.HTTP_NOT_MODIFIED);
//        log(response);
        response.end();
    }

    protected void writeResponse(Date lastModified) {
//        log(context.request());
        HttpServerResponse response = this.context.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .putHeader(HttpHeaders.CACHE_CONTROL, Arrays.asList(
                        CacheControl.MUST_REVALIDATE,
                        CacheControl.PROXY_REVALIDATE,
                        CacheControl.S_MAX_AGE + parameters.getSMaxAge(),
                        CacheControl.MAX_AGE + parameters.getMaxAge()))
                .putHeader(HttpHeaders.LAST_MODIFIED, DateTimeUtils.toRFC1123(lastModified));
//        log(response);
        response.end(out.toString());
    }

    public void log(HttpServerRequest request) {
        log.info(Color.GREEN + "[DSU] GET " + request.uri() + Color.NORMAL);
        MultiMap headers = request.headers();
        for (String key : headers.names()) {
            String value = String.join(",", headers.getAll(key));
            log.info(Color.GREEN + key + "=" + value + Color.NORMAL);
        }
    }

    public void log(HttpServerResponse response) {
        log.info(Color.GREEN
                + String.format("[DSU] %d : %s", response.getStatusCode(), response.getStatusMessage())
                + Color.NORMAL);
        MultiMap headers = response.headers();
        for (String key : headers.names()) {
            String value = String.join(",", headers.getAll(key));
            log.info(Color.GREEN + key + "=" + value + Color.NORMAL);
        }

    }
}