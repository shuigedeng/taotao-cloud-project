package com.taotao.cloud.java.reflect.socket;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer {

    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress("localhost", 9898));
        while (true) {
            Socket socket = server.accept();
            new Thread(new TestServerTask(socket)).start();
        }
    }


}
