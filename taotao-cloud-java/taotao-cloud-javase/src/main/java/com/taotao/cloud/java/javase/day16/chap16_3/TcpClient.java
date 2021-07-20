package com.taotao.cloud.java.javase.day16.chap16_3;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * TCP客户端：一直向服务器发送数据
 * @author shuigedeng
 *
 */
public class TcpClient {
	public static void main(String[] args) throws Exception {
		//1创建Socket
		Socket socket=new Socket("192.168.0.103", 10086);
		//2获取输出流
		OutputStream os=socket.getOutputStream();
		BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os,"utf-8"));
		//3控制输入
		Scanner input=new Scanner(System.in);
		while(true) {
			String data=input.nextLine();
			bw.write(data);
			bw.newLine();//发送换行符
			bw.flush();
			if(data.equals("886")||data.equals("byebye")) {
				break;
			}
		}
		//4关闭
		bw.close();
		socket.close();
	}
}
