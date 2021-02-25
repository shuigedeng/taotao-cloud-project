package com.taotao.cloud.java.javase.day14.chatper14_2;

public class TestBankCard2 {
	public static void main(String[] args) {
		BankCard card=new BankCard();
		//存钱
		Runnable add=new Runnable() {
			@Override
			public void run() {
				for(int i=0;i<10;i++) {
					card.setMoney(card.getMoney()+1000);
					System.out.println(Thread.currentThread().getName()+"存了1000,余额是:"+card.getMoney());
				}
			}
		};
		//取钱
		Runnable sub=new Runnable() {
			
			@Override
			public void run() {
				for(int i=0;i<10;i++) {
					if(card.getMoney()>=1000) {
						card.setMoney(card.getMoney()-1000);
						System.out.println(Thread.currentThread().getName()+"取了1000，余额是:"+card.getMoney());
					}else {
						System.out.println("余额不足,请赶快存取");
						i--;
					}
				}
			}
		};
		
		//创建线程对象,并启动
		new Thread(add, "明明").start();
		new Thread(sub,"丽丽").start();
		
		
	}
}
