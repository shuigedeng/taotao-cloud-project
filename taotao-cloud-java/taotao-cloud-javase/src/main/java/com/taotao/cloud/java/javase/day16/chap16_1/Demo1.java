package com.taotao.cloud.java.javase.day16.chap16_1;

import java.net.InetAddress;

/**
 * 演示InetAddress类的使用
 * (1)创建本机IP地址对象
 * (2)创建局域网IP地址对象
 * (3)创建外网IP地址对象
 * @author wgy
 *
 */
public class Demo1 {
	public static void main(String[] args) throws Exception{
		//1创建本机IP地址对象
		//1.1getLocalhost()方法
		InetAddress ia1=InetAddress.getLocalHost();
		System.out.println("ip地址:"+ia1.getHostAddress()+" 主机名:"+ia1.getHostName());
		//1.2getByName("ip地址");
		InetAddress ia2=InetAddress.getByName("192.168.0.103");
		System.out.println("ip地址:"+ia2.getHostAddress()+" 主机名:"+ia2.getHostName());
		//1.3getByName("127.0.0.1");
		InetAddress ia3=InetAddress.getByName("127.0.0.1");
		System.out.println("ip地址:"+ia3.getHostAddress()+" 主机名:"+ia3.getHostName());
		//1.4getByName("localhost");
		InetAddress ia4=InetAddress.getByName("localhost");
		System.out.println("ip地址:"+ia4.getHostAddress()+" 主机名:"+ia4.getHostName());
		
		//2创建局域网IP地址对象
		
//		InetAddress ia5=InetAddress.getByName("192.168.0.104");
//		System.out.println("ip地址:"+ia5.getHostAddress()+" 主机名:"+ia5.getHostName());
//		System.out.println("2秒钟是否可达:"+ia5.isReachable(2000));
		
		//3创建外网IP地址对象
		InetAddress ia6=InetAddress.getByName("www.baidu.com");
		System.out.println("ip地址:"+ia6.getHostAddress()+" 主机名:"+ia6.getHostName());
		System.out.println("2秒钟是否可达:"+ia6.isReachable(2000));
		System.out.println("--------------");
		InetAddress[] ias=InetAddress.getAllByName("www.baidu.com");
		for (InetAddress inetAddress : ias) {
			System.out.println(inetAddress.getHostAddress());
		}
		
	}
}
