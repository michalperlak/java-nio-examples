package pl.michalperlak.nio.server.consumer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.function.Consumer;

public class EchoChannelConsumer implements Consumer<SocketChannel> {
    @Override
    public void accept(SocketChannel socketChannel) {
        try {
            acceptImpl(socketChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptImpl(SocketChannel socketChannel) throws IOException {
        var buffer = ByteBuffer.allocateDirect(80);
        var read = socketChannel.read(buffer);
        if (read == -1) {
            socketChannel.close();
        } else if (read > 0) {
            buffer.flip();
            while (buffer.hasRemaining()) {
                socketChannel.write(buffer);
            }
            buffer.compact();
        }
    }
}
