package pl.michalperlak.nio.server;

import java.io.IOException;

public class ServerDemoApp {
    public static void main(String[] args) throws IOException {
        var server = new NonBlockingThreadPoolServer();
        server.start(9090);
    }
}
