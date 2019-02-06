package pl.michalperlak.nio.server.consumer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Queue;

public class ReadConsumer extends UncheckedIOConsumer<SelectionKey> {
    private final Map<SocketChannel, Queue<ByteBuffer>> pendingData;

    public ReadConsumer(Map<SocketChannel, Queue<ByteBuffer>> pendingData) {
        this.pendingData = pendingData;
    }

    @Override
    protected void acceptImpl(SelectionKey selectionKey) throws IOException {
        var socketChannel = (SocketChannel) selectionKey.channel();
        var buffer = ByteBuffer.allocateDirect(80);
        var read = socketChannel.read(buffer);
        if (read == -1) {
            pendingData.remove(socketChannel);
            socketChannel.close();
            System.out.println("Disconnected from : " + socketChannel);
        } else if (read > 0) {
            buffer.flip();
            pendingData.get(socketChannel).add(buffer);
            selectionKey.interestOps(SelectionKey.OP_WRITE);
        }
    }
}
