package pl.michalperlak.nio.server;

import pl.michalperlak.nio.server.consumer.EchoConsumer;
import pl.michalperlak.nio.server.consumer.LoggingConsumer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BlockingSingleThreadServer implements Server {

    @Override
    public void start(int port) throws IOException {
        var serverSocket = new ServerSocket(port);
        var socketConsumer = new LoggingConsumer<>(
                System.out, new EchoConsumer()
        );

        while (true) {
            Socket socket = serverSocket.accept();
            socketConsumer.accept(socket);
        }
    }
}
