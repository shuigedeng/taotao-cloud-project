package com.taotao.cloud.java.concurrent.reflect.socket;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class TestClient {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 9898);
        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();

        PrintWriter pw = new PrintWriter(new BufferedOutputStream(out));
        pw.println("com.datachina.bigdata.day04.itcast_04_reflect.socket.TestBusiness:getPrice:yifu");
        pw.flush();

        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String readLine = br.readLine();

        System.out.println("client get result: " + readLine);


        socket.close();


    }
}
