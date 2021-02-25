package com.taotao.cloud.java.javase.day14.chatper14_4;

public class TestBankCard {
	public static void main(String[] args) {
		//1创建银行卡
		BankCard card=new BankCard();
		//2创建两个操作
		Runnable add=new Runnable() {
			@Override
			public void run() {
				for(int i=0;i<10;i++) {
					synchronized (card) {
						card.setMoney(card.getMoney()+1000);
						System.out.println(Thread.currentThread().getName()+"存了1000,余额是:"+card.getMoney());
					}
					
				}
			}
		};
		Runnable sub=new Runnable() {
			@Override
			public void run() {
				for(int i=0;i<10;i++) {
					synchronized (card) {
						if(card.getMoney()>=1000) {
							card.setMoney(card.getMoney()-1000);
							System.out.println(Thread.currentThread().getName()+"取了1000,余额是:"+card.getMoney());
						}else {
							System.out.println("余额不足，请存取");
							i--;
						}
					}
					
				}
			}
		};
		
		//3创建两个线程对象
		Thread xiaoli=new Thread(add, "小李");
		Thread xiaoyue=new Thread(sub,"小月" );
		xiaoli.start();
		xiaoyue.start();
		
	}
}
