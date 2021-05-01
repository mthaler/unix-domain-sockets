package com.mthaler.unixdomainsockets;

import java.io.IOException;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {
        Path socket = Paths.get("/tmp/uds.socket");
        UnixDomainSocketAddress address = UnixDomainSocketAddress.of(socket);
        ServerSocketChannel serverChannel = ServerSocketChannel.open(StandardProtocolFamily.UNIX);
    }
}
