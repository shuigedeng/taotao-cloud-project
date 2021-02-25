package com.taotao.cloud.java.socket;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServiceServer {

    public static void main(String[] args) throws Exception {

        // 创建一个serversocket，绑定到本机的8899端口上
        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress("localhost", 8899));

        // 接受客户端的连接请求;accept是一个阻塞方法，会一直等待，到有客户端请求连接才返回
        while (true) {
            Socket socket = server.accept();
            new Thread(new ServiceServerTask(socket)).start();
        }
    }

}
