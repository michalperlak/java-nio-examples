package pl.michalperlak.nio.server;

import pl.michalperlak.nio.server.consumer.AcceptConsumer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Queue;

public class NonBlockingSingleThreadServer implements Server {

    @Override
    public void start(int port) throws IOException {
        var serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        var selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        var pendingData = new HashMap<SocketChannel, Queue<ByteBuffer>>();
        var acceptConsumer = new AcceptConsumer(pendingData);
    }
}
