package siri_xlite.service.common;

import org.reactivestreams.Subscriber;
import siri_xlite.common.JsonUtils;
import siri_xlite.common.Parameters;

public interface SiriSubscriber<T, P extends Parameters> extends Subscriber<T>, JsonUtils {

    void configure(P parameters);

    void close();
}
