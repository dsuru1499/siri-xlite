package siri_xlite.common;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.subscribers.LambdaSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Slf4j
public class MonitorSubscriber<T> implements Subscriber<T> {

    private String label;
    private Subscriber<T> subscriber;
    private Monitor monitor;

    public MonitorSubscriber(String label, Consumer<? super T> onNext, Consumer<? super Throwable> onError,
                             Action onComplete, Consumer<? super Subscription> onSubscribe) {
        this.label = label;
        this.subscriber = new LambdaSubscriber<T>(onNext, onError, onComplete, onSubscribe);
    }

    public MonitorSubscriber(String label, Subscriber<T> subscriber) {
        this.label = label;
        this.subscriber = subscriber;
    }

    public static <T> MonitorSubscriber<T> create(String label, Consumer<? super T> onNext) {
        return MonitorSubscriber.create(label, onNext, null, null, null);
    }

    public static <T> MonitorSubscriber<T> create(String label, Consumer<? super T> onNext,
                                                  Consumer<? super Throwable> onError) {
        return MonitorSubscriber.create(label, onNext, onError, null, null);
    }

    public static <T> MonitorSubscriber<T> create(String label, Consumer<? super T> onNext,
                                                  Consumer<? super Throwable> onError, Action onComplete, Consumer<? super Subscription> onSubscribe) {
        return new MonitorSubscriber<T>(label, onNext, onError, onComplete, onSubscribe);
    }

    @Override
    public void onSubscribe(Subscription s) {
        log.info("------------> onSubscribe");
        monitor = MonitorFactory.start(label);
        subscriber.onSubscribe(s);
    }

    @Override
    public void onNext(T t) {
        subscriber.onNext(t);
    }

    @Override
    public void onError(Throwable t) {
        log.info("------------> onError");
        try {
            subscriber.onError(t);
        } finally {
            stop();
        }
    }

    @Override
    public void onComplete() {
        log.info("------------> onComplete");
        try {
            subscriber.onComplete();
        } finally {
            stop();
        }
    }

    protected void stop() {
        log.info(Color.YELLOW + monitor.stop() + Color.NORMAL);
    }
}