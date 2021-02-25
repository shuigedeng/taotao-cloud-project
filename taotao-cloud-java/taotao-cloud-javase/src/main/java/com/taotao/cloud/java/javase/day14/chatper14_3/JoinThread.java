package com.taotao.cloud.java.javase.day14.chatper14_3;
/**
 * join方法的使用
 * @author wgy
 *
 */
public class JoinThread extends Thread {
	@Override
	public void run() {
		for(int i=0;i<30;i++) {
			System.out.println(Thread.currentThread().getName()+"......"+i);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
