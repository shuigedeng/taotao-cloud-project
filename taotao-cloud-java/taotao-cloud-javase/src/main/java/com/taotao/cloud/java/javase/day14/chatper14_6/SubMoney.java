package com.taotao.cloud.java.javase.day14.chatper14_6;

public class SubMoney implements Runnable{
	private BankCard card;
	public SubMoney(BankCard card) {
		this.card=card;
	}
	@Override
	public void run() {
		for(int i=0;i<10;i++) {
			card.take(1000);
		}
	}
}
