package pl.michalperlak.nio.server;

import pl.michalperlak.nio.server.consumer.EchoConsumer;
import pl.michalperlak.nio.server.consumer.LoggingConsumer;
import pl.michalperlak.nio.server.consumer.ThreadPoolConsumer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executors;

public class BlockingThreadPoolServer implements Server {

    @Override
    public void start(int port) throws IOException {
        var serverSocket = new ServerSocket(port);
        var consumer = new ThreadPoolConsumer<>(
                Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()),
                (t, e) -> System.out.println("Uncaught: " + t + " error: " + e),
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
