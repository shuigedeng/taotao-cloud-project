package com.taotao.cloud.java.javase.day14.chatper14_7;

public class BreadCon {
	//存放面包的数组
	private Bread[] cons=new Bread[6];
	//存放面包的位置
	private int index=0;
	
	//存放面包
	public synchronized void input(Bread b) { //锁this
		//判断容器有没有满
		while(index>=6) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		cons[index]=b;
		System.out.println(Thread.currentThread().getName()+"生产了"+b.getId()+"");
		index++;
		//唤醒
		this.notifyAll();
		
		
		
	}
	//取出面包
	public synchronized void output() {//锁this
		while(index<=0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		index--;
		Bread b=cons[index];
		System.out.println(Thread.currentThread().getName()+"消费了"+b.getId()+" 生产者:"+b.getProductName());
		cons[index]=null;
		//唤醒生产者
		this.notifyAll();
		
		
		
	}
}
