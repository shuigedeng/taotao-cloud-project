package com.taotao.cloud.java.javase.day14.chatper14_6;

public class BankCard {
	//余额
	private double money;
	//标记
	private boolean flag=false;// true 表示有钱可以取钱  false没钱 可以存取
	
	
	
	//存钱
	
	public synchronized void save(double m) { //this
		while(flag) {//有钱
			try {
				this.wait();//进入等待队列,同时释放锁 ，和cpu
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		money=money+m;
		System.out.println(Thread.currentThread().getName()+"存了"+m+" 余额是"+money);
		//修改标记
		flag=true;
		//唤醒取钱线程
		this.notifyAll();
		
	
		
	}
	
	//取钱
	public synchronized void take(double m) {//this
		while(!flag) {
			try {
				this.wait();//进入等待队列,同时释放锁 ，和cpu
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		money=money-m;
		System.out.println(Thread.currentThread().getName()+"取了"+m+" 余额是"+money);
		//修改标记
		flag=false;
		//唤醒存钱线程
		this.notifyAll();
		
		
	}
	
}
