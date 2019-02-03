package pl.michalperlak.nio.server.consumer;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class AcceptConsumer implements Consumer<SelectionKey> {
    private final Map<SocketChannel, Queue<ByteBuffer>> pendingData;

    public AcceptConsumer(Map<SocketChannel, Queue<ByteBuffer>> pendingData) {
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
        var serverChannel = (ServerSocketChannel) selectionKey.channel();
        var socketChannel = serverChannel.accept();
        System.out.println("Connected to: " + socketChannel);
        pendingData.put(socketChannel, new ConcurrentLinkedQueue<>());
        socketChannel.configureBlocking(false);
        socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ);
    }
}
