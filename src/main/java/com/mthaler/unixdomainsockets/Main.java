package com.mthaler.unixdomainsockets;

import java.io.IOException;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException {
        Path socket = Paths.get("/tmp/uds.socket");
        UnixDomainSocketAddress address = UnixDomainSocketAddress.of(socket);

        ServerSocketChannel serverChannel = ServerSocketChannel.open(StandardProtocolFamily.UNIX);
        serverChannel.bind(address);

        Thread serverThread = new Thread(() -> {
            try {
                SocketChannel channel = serverChannel.accept();
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                while (true) {
                    int count = channel.read(buffer);
                    if (count > 0) {
                        byte[] bytes = new byte[count];
                        buffer.flip();
                        buffer.get(bytes);
                        String msg = new String(bytes);
                        System.out.println(msg);
                    }
                    buffer.clear();
                    Thread.sleep(500);
                }
            } catch (IOException | InterruptedException ex) {
                System.err.println("Server error " + ex);
            }
        });
        serverThread.start();

        SocketChannel clientChannel = SocketChannel.open(StandardProtocolFamily.UNIX);
        clientChannel.connect(address);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(LocalDateTime.now().toString().getBytes());
        buffer.flip();
        while (buffer.hasRemaining()) {
            clientChannel.write(buffer);
        }
    }
}
