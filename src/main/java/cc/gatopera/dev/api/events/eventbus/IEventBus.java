package cc.gatopera.dev.api.events.eventbus;

public interface IEventBus {

    void registerLambdaFactory(LambdaListener.Factory factory);

    <T> T post(T event);

    <T extends ICancellable> T post(T event);

    void subscribe(Object object);

    void subscribe(Class<?> klass);

    void subscribe(IListener listener);

    void unsubscribe(Object object);

    void unsubscribe(Class<?> klass);

    void unsubscribe(IListener listener);
}
