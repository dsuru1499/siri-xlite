package siri_xlite.common;

import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class CacheControl {
    public static final String PUBLIC = "public";
    public static final String MAX_AGE = "max-age=";
    public static final String S_MAX_AGE = "s-maxage=";
    public static final String PROXY_REVALIDATE = "proxy-revalidate";
    public static final String MUST_REVALIDATE = "must-revalidate";

    public static final String RECORDED_AT_TIME = "recordedAtTime";
    public static final Comparator<Document> COMPARATOR = Comparator.comparing(t -> t.getDate(RECORDED_AT_TIME).getTime());

    public static Date getLastModified(RoutingContext context) {
        if (context != null) {
            String text = context.request().getHeader(HttpHeaders.IF_MODIFIED_SINCE);
            return StringUtils.isNotEmpty(text) ? DateTimeUtils.fromRFC1123(text) : null;
        }
        return null;
    }

    public static Date getLastModified(Document document) {
        return (document != null) ? document.getDate(RECORDED_AT_TIME) : null;
    }

    public static Date getLastModified(List<? extends Document> list) {
        Optional<? extends Document> result = list.stream().max(COMPARATOR);
        return result.map(CacheControl::getLastModified).orElse(null);
    }
}
