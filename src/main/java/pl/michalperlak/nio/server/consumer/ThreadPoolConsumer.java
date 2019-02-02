package pl.michalperlak.nio.server.consumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;

public class ThreadPoolConsumer<S> implements Consumer<S> {
    private final ExecutorService pool;
    private final Thread.UncaughtExceptionHandler exceptionHandler;
    private final Consumer<S> consumer;

    public ThreadPoolConsumer(ExecutorService pool, Thread.UncaughtExceptionHandler exceptionHandler, Consumer<S> consumer) {
        this.pool = pool;
        this.exceptionHandler = exceptionHandler;
        this.consumer = consumer;
    }

    @Override
    public void accept(S s) {
        pool.submit(new RunConsumerTask(s));
    }

    private class RunConsumerTask extends FutureTask<S> {
        RunConsumerTask(S s) {
            super(() -> consumer.accept(s), null);
        }

        @Override
        protected void setException(Throwable t) {
            exceptionHandler.uncaughtException(Thread.currentThread(), t);
        }
    }
}
