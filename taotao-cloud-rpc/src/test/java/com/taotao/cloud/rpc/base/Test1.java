package com.taotao.cloud.rpc.base;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test1 {

	public static void main(String[] args) throws InterruptedException, IOException {

		//JRedisHelper.setWorkerId("123",123L);
		//
		//long begin1 = System.currentTimeMillis();
		//for (int i = 0; i < 10; i++) {
		//    byte[] res1 = LRedisHelper.getForRetryRequestId("123");
		//}
		//System.out.println("LRedisHelper.getForRetryRequestId " + (System.currentTimeMillis() - begin1));
		//
		//long begin2 = System.currentTimeMillis();
		//JRedisHelper.getForRetryRequestId("123");
		//for (int i = 0; i < 10; i++) {
		//    String res2 = JRedisHelper.getForRetryRequestId("123");
		//}
		//System.out.println("JRedisHelper.getForRetryRequestId " + (System.currentTimeMillis() - begin2));
		//
		//Thread[] threads1 = new Thread[10];
		//Thread[] threads2 = new Thread[10];
		//long mainStart = System.currentTimeMillis();
		//for (int i = 0; i < 10; i++) {
		//    Thread t = new Thread(() -> {
		//        for (int j = 0; j < 10; j++) {
		//            String res2 = JRedisHelper.getForRetryRequestId("123");
		//        }
		//    }, "t" + i);
		//    threads1[i] = t;
		//    t.start();
		//}
		//
		//long mainStart1 = System.currentTimeMillis();
		//for (int i = 0; i < 10; i++) {
		//    Thread t = new Thread(() -> {
		//        for (int j = 0; j < 10; j++) {
		//            byte[] res2 = LRedisHelper.getForRetryRequestId("123");
		//        }
		//    }, "t" + i);
		//    threads2[i] = t;
		//    t.start();
		//}
		//
		//new Thread(() -> {
		//    for (Thread t : threads1) {
		//        try {
		//            t.join();
		//        } catch (InterruptedException e) {
		//            e.printStackTrace();
		//        }
		//    }
		//    System.out.println("JRedisHelper.getForRetryRequestId " + (System.currentTimeMillis() - mainStart));
		//}).start();
		//
		//
		//
		//
		//
		//new Thread(() -> {
		//    for (Thread t : threads2) {
		//        try {
		//            t.join();
		//        } catch (InterruptedException e) {
		//            e.printStackTrace();
		//        }
		//    }
		//    System.out.println("LRedisHelper.getForRetryRequestId " + (System.currentTimeMillis() - mainStart1));
		//}).start();

		//JRedisHelper.remWorkerId(IpUtils.getPubIpAddr());
		//System.out.println(Runtime.getRuntime().availableProcessors());

		//JRedisHelper.getForHostName("1.12.54.231");
		//log.info("开始");
		//for (int i = 0; i < 1000; i++) {
		//    String forHostName = JRedisHelper.getForHostName("1.12.54.231");
		//    //log.info("forHostName:" + forHostName);
		//}
		//log.info("结束");
		//LRedisHelper.getForHostName("1.12.54.231");
		//log.info("开始");
		//for (int i = 0; i < 1000; i++) {
		//    String forHostName = LRedisHelper.getForHostName("1.12.54.231");
		//    //log.info("forHostName:" + forHostName);
		//}
		//log.info("结束");

		//log.info("开始");
		//
		//long begin = System.currentTimeMillis();
		//log.info(begin + "");
		//
		//Thread.sleep(1000);
		//
		//
		//long end = System.currentTimeMillis();
		//log.info(end + "");
		//
		//log.info((end - begin) + "");
		//
		//log.info("结束");
		start(8081);


	}


	public static void start(int port) {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			log.info("Server is running...");
			Socket socket;
			// 监听 客户端连接
			while ((socket = serverSocket.accept()) != null) {
				log.info("customer has connected successfully! ip = " + socket.getInetAddress());
				InputStreamReader ois = new InputStreamReader(socket.getInputStream());
				OutputStreamWriter oos = new OutputStreamWriter(socket.getOutputStream());

				char[] buffer = new char[1024];

				int read = ois.read(buffer, 0, buffer.length);
				System.out.println("接收数据 " + buffer);
				oos.write("I have receive data");
			}
		} catch (IOException e) {
			log.error("Exception throws when connecting, info: {}", e);
		}
	}
}
