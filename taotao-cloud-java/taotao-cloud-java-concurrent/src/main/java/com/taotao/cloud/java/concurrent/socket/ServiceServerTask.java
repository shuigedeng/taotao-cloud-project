package com.taotao.cloud.java.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ServiceServerTask implements Runnable {
    Socket socket;
    InputStream in = null;
    OutputStream out = null;

    public ServiceServerTask(Socket socket) {
        this.socket = socket;
    }

    //业务逻辑：跟客户端进行数据交互
    @Override
    public void run() {
        try {
            //从socket连接中获取到与client之间的网络通信输入输出流
            in = socket.getInputStream();
            out = socket.getOutputStream();


            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            //从网络通信输入流中读取客户端发送过来的数据
            //注意：socketinputstream的读数据的方法都是阻塞的
            String param = br.readLine();


            /**
             * 作业：
             * 将以下业务调用逻辑写成更加通用的：可以根据客户端发过来的调用类名、调用方法名、调用该参数来灵活调用
             *
             * 《反射》
             *
             */

            GetDataServiceImpl getDataServiceImpl = new GetDataServiceImpl();
            String result = getDataServiceImpl.getData(param);


            //将调用结果写到sokect的输出流中，以发送给客户端
            PrintWriter pw = new PrintWriter(out);
            pw.println(result);
            pw.flush();


        } catch (IOException e) {

            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}
