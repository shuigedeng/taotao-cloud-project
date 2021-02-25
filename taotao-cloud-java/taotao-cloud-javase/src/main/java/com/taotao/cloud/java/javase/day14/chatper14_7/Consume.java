package com.taotao.cloud.java.javase.day14.chatper14_7;

public class Consume implements Runnable{

	private BreadCon con;
	
	public Consume(BreadCon con) {
		super();
		this.con = con;
	}

	@Override
	public void run() {
		for(int i=0;i<30;i++) {
			con.output();
		}
	}

}
