package pl.michalperlak.nio.server;

import java.io.IOException;

public interface Server {
    void start(int port) throws IOException;
}
