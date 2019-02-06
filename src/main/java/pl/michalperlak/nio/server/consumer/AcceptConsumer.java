package pl.michalperlak.nio.server.consumer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AcceptConsumer extends UncheckedIOConsumer<SelectionKey> {
    private final Map<SocketChannel, Queue<ByteBuffer>> pendingData;

    public AcceptConsumer(Map<SocketChannel, Queue<ByteBuffer>> pendingData) {
        this.pendingData = pendingData;
    }

    @Override
    protected void acceptImpl(SelectionKey selectionKey) throws IOException {
        var serverChannel = (ServerSocketChannel) selectionKey.channel();
        var socketChannel = serverChannel.accept();
        System.out.println("Connected to: " + socketChannel);
        pendingData.put(socketChannel, new ConcurrentLinkedQueue<>());
        socketChannel.configureBlocking(false);
        socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ);
    }
}
