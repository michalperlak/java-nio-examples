package pl.michalperlak.nio.server.consumer;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.util.function.Consumer;

public class EchoConsumer implements Consumer<Socket> {

    @Override
    public void accept(Socket socket) {
        try (
                var inputStream = socket.getInputStream();
                var outputStream = socket.getOutputStream()
        ) {
            var data = inputStream.read();
            while (data != -1) {
                outputStream.write(data);
                data = inputStream.read();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
