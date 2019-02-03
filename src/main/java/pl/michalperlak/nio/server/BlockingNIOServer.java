package pl.michalperlak.nio.server;

import pl.michalperlak.nio.server.consumer.BlockingChannelConsumer;
import pl.michalperlak.nio.server.consumer.EchoChannelConsumer;
import pl.michalperlak.nio.server.consumer.LoggingConsumer;
import pl.michalperlak.nio.server.consumer.ThreadPoolConsumer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.Executors;

public class BlockingNIOServer implements Server {
    @Override
    public void start(int port) throws IOException {
        var serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        var consumer = new ThreadPoolConsumer<>(
                Executors.newFixedThreadPool(10),
                (t, e) -> System.out.println("Uncaught: " + t + " error: " + e),
                new LoggingConsumer<>(
                        System.out,
                        new BlockingChannelConsumer(
                                new EchoChannelConsumer()
                        )
                )
        );

        while (true) {
            var socketChannel = serverSocketChannel.accept();
            consumer.accept(socketChannel);
        }
    }
}
