package com.taotao.cloud.netty.zerocopy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.util.Map;

public class NewIOClient {

    public static void main(String[] args) throws Exception {
//        SocketChannel socketChannel = SocketChannel.open();
//        socketChannel.connect(new InetSocketAddress("localhost", 8899));
//        socketChannel.configureBlocking(true);
//
//        String fileName = "/Users/zhanglong/Desktop/spark-2.2.0-bin-hadoop2.7.tgz";
//
//        FileChannel fileChannel = new FileInputStream(fileName).getChannel();
//
//        long startTime = System.currentTimeMillis();
//
//        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
//
//        System.out.println("发送总字节数：" + transferCount + "，耗时： " + (System.currentTimeMillis() - startTime));
//
//        fileChannel.close();

	    System.out.println("output environment var:");
	    for(Map.Entry entry:System.getenv().entrySet()){
		    System.out.println(entry.getKey()+":"+entry.getValue());
	    }
    }
}
