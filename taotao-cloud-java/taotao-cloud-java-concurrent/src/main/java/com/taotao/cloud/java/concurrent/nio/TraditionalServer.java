package com.taotao.cloud.java.concurrent.nio;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TraditionalServer {

    @SuppressWarnings("resource")
    public static void main(String args[]) throws Exception {
        // 监听端口
        ServerSocket server_socket = new ServerSocket(5000);
        System.out.println("等待，端口为：" + server_socket.getLocalPort());

        while (true) {
            // 阻塞接受消息
            Socket socket = server_socket.accept();
            // 打印链接信息
            System.out.println("新连接： " + socket.getInetAddress() + ":"
                    + socket.getPort());
            // 从socket中获取流
            DataInputStream input = new DataInputStream(socket.getInputStream());
            // 接收数据
            byte[] byteArray = new byte[4096];
            while (true) {
                int nread = input.read(byteArray, 0, 4096);
                System.out.println(new String(byteArray, "UTF-8"));
                if (-1 == nread) {
                    break;
                }

            }
            socket.close();
            System.out.println("Connection closed by client");

        }
    }
}
