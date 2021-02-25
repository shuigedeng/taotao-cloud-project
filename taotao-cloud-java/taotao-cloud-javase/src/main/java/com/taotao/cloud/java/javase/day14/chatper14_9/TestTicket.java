package com.taotao.cloud.java.javase.day14.chatper14_9;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestTicket {
	public static void main(String[] args) {
		Ticket ticket=new Ticket();
		ExecutorService es=Executors.newFixedThreadPool(4);
		for(int i=0;i<4;i++) {
			es.submit(ticket);
		}
		es.shutdown();
		
	}
}
