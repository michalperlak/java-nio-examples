package pl.michalperlak.nio.server.consumer;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;

public class WriteConsumer implements Consumer<SelectionKey> {
    private final Map<SocketChannel, Queue<ByteBuffer>> pendingData;

    public WriteConsumer(Map<SocketChannel, Queue<ByteBuffer>> pendingData) {
        this.pendingData = pendingData;
    }

    @Override
    public void accept(SelectionKey selectionKey) {
        try {
            acceptImpl(selectionKey);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void acceptImpl(SelectionKey selectionKey) throws IOException {
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
