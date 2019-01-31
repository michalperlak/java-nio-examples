package pl.michalperlak.nio.server.consumer;

import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;

public class ThreadedConsumer<S> implements Consumer<S> {
    private final ThreadFactory threadFactory;
    private final Consumer<S> consumer;

    public ThreadedConsumer(ThreadFactory threadFactory, Consumer<S> consumer) {
        this.threadFactory = threadFactory;
        this.consumer = consumer;
    }

    @Override
    public void accept(S s) {
        threadFactory
                .newThread(() -> consumer.accept(s))
                .start();
    }
}
