package com.taotao.cloud.java.javase.day17.chap17_3;

public class TestSingleTon {
	public static void main(String[] args) {
		for(int i=0;i<3;i++) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					System.out.println(SingleTon3.getInstance().hashCode());
				}
			}).start();
		}
	}
}
