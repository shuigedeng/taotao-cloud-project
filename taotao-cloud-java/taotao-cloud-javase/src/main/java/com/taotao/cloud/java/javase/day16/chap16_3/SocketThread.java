package com.taotao.cloud.java.javase.day16.chap16_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketThread extends Thread{

	private Socket socket;
	
	public SocketThread(Socket socket) {
		this.socket=socket;
	}
	@Override
	public void run() {
		if(socket!=null) {
			BufferedReader br=null;
			try {
				InputStream is = socket.getInputStream();
				br=new BufferedReader(new InputStreamReader(is,"utf-8"));
				while(true) {
					String data=br.readLine();
					if(data==null) {//客户端已经关闭
						break;
					}
					System.out.println(socket.getInetAddress()+"说:"+data);
					if(data.equals("886")||data.equals("byebye")) {
						break;
					}
				}	
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}finally {
				try {
					br.close();
					socket.close();
					System.out.println(socket.getInetAddress()+"退出了...");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			
			
		}
		
	}
}
