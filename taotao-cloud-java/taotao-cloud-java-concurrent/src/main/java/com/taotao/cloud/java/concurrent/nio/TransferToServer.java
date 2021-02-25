package com.taotao.cloud.java.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class TransferToServer {

    public static void main(String[] args) throws IOException {
        // 创建socket channel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket ss = serverSocketChannel.socket();
        ss.setReuseAddress(true);// 地址重用
        ss.bind(new InetSocketAddress("localhost", 9026));// 绑定地址
        System.out.println("监听端口 : "
                + new InetSocketAddress("localhost", 9026).toString());

        // 分配一个新的字节缓冲区
        ByteBuffer dst = ByteBuffer.allocate(4096);
        // 读取数据
        while (true) {
            SocketChannel channle = serverSocketChannel.accept();// 接收数据
            System.out.println("Accepted : " + channle);
            channle.configureBlocking(true);// 设置阻塞，接不到就停
            int nread = 0;
            while (nread != -1) {
                try {
                    nread = channle.read(dst);// 往缓冲区里读
                    byte[] array = dst.array();//将数据转换为array
                    //打印
                    String string = new String(array, 0, dst.position());
                    System.out.print(string);
                    dst.clear();
                } catch (IOException e) {
                    e.printStackTrace();
                    nread = -1;
                }
            }
        }
    }
}
