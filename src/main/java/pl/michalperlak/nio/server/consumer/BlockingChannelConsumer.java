package pl.michalperlak.nio.server.consumer;

import java.nio.channels.SocketChannel;
import java.util.function.Consumer;

public class BlockingChannelConsumer implements Consumer<SocketChannel> {
    private final Consumer<SocketChannel> consumer;

    public BlockingChannelConsumer(Consumer<SocketChannel> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void accept(SocketChannel socketChannel) {
        while (socketChannel.isConnected()) {
            consumer.accept(socketChannel);
        }
    }
}
