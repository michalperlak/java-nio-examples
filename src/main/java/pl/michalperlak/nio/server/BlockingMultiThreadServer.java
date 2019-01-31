package pl.michalperlak.nio.server;

import pl.michalperlak.nio.server.consumer.EchoConsumer;
import pl.michalperlak.nio.server.consumer.LoggingConsumer;
import pl.michalperlak.nio.server.consumer.ThreadedConsumer;

import java.io.IOException;
import java.net.ServerSocket;

public class BlockingMultiThreadServer implements Server {
    @Override
    public void start(int port) throws IOException {
        var serverSocket = new ServerSocket(port);
        var consumer = new ThreadedConsumer<>(
                Thread::new,
                new LoggingConsumer<>(
                        System.out, new EchoConsumer()
                )
        );

        while (true) {
            var socket = serverSocket.accept();
            consumer.accept(socket);
        }
    }
}
