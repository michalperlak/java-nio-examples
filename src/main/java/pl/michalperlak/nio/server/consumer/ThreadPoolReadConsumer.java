package pl.michalperlak.nio.server.consumer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

public class ThreadPoolReadConsumer extends UncheckedIOConsumer<SelectionKey> {
    private final ExecutorService pool;
    private final Queue<Runnable> selectorActions;
    private final Map<SocketChannel, Queue<ByteBuffer>> pendingData;

    public ThreadPoolReadConsumer(ExecutorService pool, Queue<Runnable> selectorActions, Map<SocketChannel, Queue<ByteBuffer>> pendingData) {
        this.pool = pool;
        this.selectorActions = selectorActions;
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
            System.out.println("Disconnected from " + socketChannel);
            return;
        }

        if (read > 0) {
            pool.submit(() -> {
                buffer.flip();
                pendingData.get(socketChannel).add(buffer);
                selectorActions.add(() -> selectionKey.interestOps(SelectionKey.OP_WRITE));
                selectionKey.selector().wakeup();
            });
        }
    }
}
