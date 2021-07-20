package com.taotao.cloud.java.javase.day16.chap16_3;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * 使用TCP实现接收多个客户端请求
 * @author shuigedeng
 *
 */
public class TcpServer {
	public static void main(String[] args) throws Exception {
		//1创建ServerSocket
		ServerSocket listener=new ServerSocket(10086);
		//2调用accept(),接收客户端请求
		System.out.println("服务器已启动..........");
		while(true) {
			Socket socket=listener.accept();
			System.out.println(socket.getInetAddress()+"进来了.........");
			//创建线程对象，负责接收数据
			new SocketThread(socket).start();
		}
	}
}
