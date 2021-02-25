package com.taotao.cloud.java.javase.day16.chap16_4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class LoginThread extends Thread {
	@Override
	public void run() {
		try {
			//1创建Serversocket 
			ServerSocket listener=new ServerSocket(7777);
			//2调用accept方法
			System.out.println("登录服务器已启动......");
			Socket socket=listener.accept();
			//3获取输入输出流
			BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
			BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"));
			//4接收客户端发送的数据{id : 1001, pwd :123}
			String json=br.readLine();
			//id : 1001 pwd :123
			String[] infos=json.substring(1, json.length()-1).split(",");
			String id=infos[0].split(":")[1];
			//5加载属性文件
			Properties properties=Tools.loadProperties();
			//6判断是否存在
			if(properties.containsKey(id)) {
				//判断密码是否正确
				String pwd=infos[1].split(":")[1];
				String value=properties.getProperty(id);
				String[] arr=value.substring(1, value.length()-1).split(",");
				String pwd2=arr[2].split(":")[1];
				if(pwd.equals(pwd2)) {
					bw.write("登录成功");
				}else {
					bw.write("密码错误");
				}
				
			}else {
				//保存属性文件
				bw.write("用户名或密码错误");
			}
			bw.newLine();
			bw.flush();
			bw.close();
			br.close();
			socket.close();
			listener.close();			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
