package pl.michalperlak.nio.server.consumer;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Consumer;

public abstract class UncheckedIOConsumer<S> implements Consumer<S> {

    @Override
    public final void accept(S s) {
        try {
            acceptImpl(s);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    protected abstract void acceptImpl(S s) throws IOException;
}
