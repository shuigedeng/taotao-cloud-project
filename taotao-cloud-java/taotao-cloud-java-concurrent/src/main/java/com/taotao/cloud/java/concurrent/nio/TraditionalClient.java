package com.taotao.cloud.java.concurrent.nio;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.Socket;

public class TraditionalClient {

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        // 创建socket链接
        Socket socket = new Socket("localhost", 5000);
        System.out.println("Connected with server " + socket.getInetAddress()
                + ":" + socket.getPort());
        // 读取文件
        FileInputStream inputStream = new FileInputStream("/home/dengtao/公司资料/新建文本文档.txt");
        // 输出文件
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        // 缓冲区4096K
        byte[] b = new byte[4096];
        // 传输长度
        long read = 0, total = 0;
        // 读取文件，写到socketio中
        while ((read = inputStream.read(b)) >= 0) {
            total = total + read;
            output.write(b);
        }
        // 关闭
        output.close();
        socket.close();
        inputStream.close();
        // 打印时间
        System.out.println("bytes send--" + total + " and totaltime--"
                + (System.currentTimeMillis() - start));
    }
}
