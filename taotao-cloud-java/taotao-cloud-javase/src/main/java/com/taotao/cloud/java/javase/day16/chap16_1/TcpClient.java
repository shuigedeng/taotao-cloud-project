package com.taotao.cloud.java.javase.day16.chap16_1;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * 基于TCP客户端开发
 * 1 创建客户端套接字，并指定服务器的地址和端口号
 * 2 获取输出流，发送数据给服务器
 * 3 获取输入流，读取服务器回复的数据
 * 4 关闭释放资源
 * @author shuigedeng
 *
 */
public class TcpClient {
	public static void main(String[] args) throws Exception{
//		 * 1 创建客户端套接字，并指定服务器的地址和端口号
		Socket socket=new Socket("192.168.0.103", 8899);
//		 * 2 获取输出流，发送数据给服务器
		OutputStream os=socket.getOutputStream();
		BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os,"utf-8"));
		bw.write("好久不见");
//		 * 3 获取输入流，读取服务器回复的数据[可选]
//		 * 4 关闭释放资源
		bw.close();
		socket.close();
	}
}
