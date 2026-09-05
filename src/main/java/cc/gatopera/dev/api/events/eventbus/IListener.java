package cc.gatopera.dev.api.events.eventbus;

public interface IListener {

    void call(Object event);

    Class<?> getTarget();

    int getPriority();

    boolean isStatic();
}
