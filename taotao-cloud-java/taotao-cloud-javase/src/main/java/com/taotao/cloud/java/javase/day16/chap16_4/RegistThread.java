package com.taotao.cloud.java.javase.day16.chap16_4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

/**
 * 实现注册功能
 * @author shuigedeng
 *
 */
public class RegistThread extends Thread{
	@Override
	public void run() {
		
		try {
			//1创建Serversocket 
			ServerSocket listener=new ServerSocket(6666);
			//2调用accept方法
			System.out.println("注册服务器已启动......");
			Socket socket=listener.accept();
			//3获取输入输出流
			BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
			BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"));
			//4接收客户端发送的数据{id : 1001, name :tom, pwd :123, age : 20 }
			String json=br.readLine();
			//id : 1001, name :tom, pwd :123, age : 20
			String[] infos=json.substring(1, json.length()-1).split(",");
			String id=infos[0].split(":")[1];
			//5加载属性文件
			Properties properties=Tools.loadProperties();
			//6判断
			if(properties.containsKey(id)) {
				//有
				bw.write("此用户已存在...");
			}else {
				//保存属性文件
				Tools.saveProperties(json);
				bw.write("注册成功");
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
