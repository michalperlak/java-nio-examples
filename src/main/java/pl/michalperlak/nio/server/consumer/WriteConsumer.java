package pl.michalperlak.nio.server.consumer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Queue;

public class WriteConsumer extends UncheckedIOConsumer<SelectionKey> {
    private final Map<SocketChannel, Queue<ByteBuffer>> pendingData;

    public WriteConsumer(Map<SocketChannel, Queue<ByteBuffer>> pendingData) {
        this.pendingData = pendingData;
    }

    @Override
    protected void acceptImpl(SelectionKey selectionKey) throws IOException {
        var socketChannel = (SocketChannel) selectionKey.channel();
        var queue = pendingData.get(socketChannel);
        while (!queue.isEmpty()) {
            var buffer = queue.peek();
            var written = socketChannel.write(buffer);
            if (written == -1) {
                socketChannel.close();
                System.out.println("Disconnected from : " + socketChannel);
                pendingData.remove(socketChannel);
                return;
            }
            if (buffer.hasRemaining()) {
                return;
            }
            queue.remove();
        }
        selectionKey.interestOps(SelectionKey.OP_READ);
    }
}
