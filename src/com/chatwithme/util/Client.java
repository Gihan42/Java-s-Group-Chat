package com.chatwithme.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client extends Socket {

    String name;
    DataOutputStream out;

    public Client (String name, String host, int port) throws IOException {
        super(host,port);
        this.name = name;
        out = new DataOutputStream(this.getOutputStream());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public void setOut(DataOutputStream out) {
        this.out = out;
    }

}
