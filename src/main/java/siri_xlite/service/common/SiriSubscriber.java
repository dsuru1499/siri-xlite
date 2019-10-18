package siri_xlite.service.common;

import org.reactivestreams.Subscriber;
import siri_xlite.Configuration;
import siri_xlite.marshaller.json.SiriServiceMarshaller;

public interface SiriSubscriber<T, P extends DefaultParameters> extends Subscriber<T>, SiriServiceMarshaller<P> {

    void configure(Configuration configuration, P parameters);

    void close();
}
