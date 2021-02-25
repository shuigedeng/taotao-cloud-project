package com.taotao.cloud.java.javase.day16.chap16_2;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Tcp客户端
 * @author wgy
 *
 */
public class TcpFileClient {
	public static void main(String[] args) throws Exception {
		//1创建Socket
		Socket socket=new Socket("192.168.0.103", 9999);
		//2获取输出流
		OutputStream os=socket.getOutputStream();
		//3边读取文件，边发送
		FileInputStream fis=new FileInputStream("d:\\001.jpg");
		byte[] buf=new byte[1024*4];
		int count=0;
		while((count=fis.read(buf))!=-1) {
			os.write(buf,0,count);
		}
		//4关闭
		fis.close();
		os.close();
		socket.close();
		System.out.println("发送完毕");
	}
}
