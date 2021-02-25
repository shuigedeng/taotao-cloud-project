package com.taotao.cloud.java.javase.day14.chatper14_7;

public class Prodcut implements Runnable {

	private BreadCon con;
	
	public Prodcut(BreadCon con) {
		super();
		this.con = con;
	}

	@Override
	public void run() {
		for(int i=0;i<30;i++) {
			con.input(new Bread(i, Thread.currentThread().getName()));
		}
	}
	
}
