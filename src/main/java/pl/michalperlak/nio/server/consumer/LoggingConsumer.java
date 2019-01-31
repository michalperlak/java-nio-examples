package pl.michalperlak.nio.server.consumer;

import java.io.PrintStream;
import java.util.function.Consumer;

public class LoggingConsumer<S> implements Consumer<S> {
    private final PrintStream log;
    private final Consumer<S> other;

    public LoggingConsumer(PrintStream log, Consumer<S> consumer) {
        this.log = log;
        this.other = consumer;
    }

    @Override
    public void accept(S s) {
        log.println("Connected to: " + s);
        try {
            other.accept(s);
        } finally {
            log.println("Disconnected from: " + s);
        }
    }
}
