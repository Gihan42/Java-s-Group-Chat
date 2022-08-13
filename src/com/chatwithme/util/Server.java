package com.chatwithme.util;

import java.io.IOException;
import java.net.ServerSocket;

public class Server extends ServerSocket {

    public Server(int port, int backlog) throws IOException {
        super(port,backlog);
    }

}
