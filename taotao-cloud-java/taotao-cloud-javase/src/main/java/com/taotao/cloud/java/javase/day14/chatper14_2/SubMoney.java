package com.taotao.cloud.java.javase.day14.chatper14_2;
/**
 * 取钱
 * @author shuigedeng
 *
 */
public class SubMoney implements Runnable{

	private BankCard card;
	
	public SubMoney(BankCard card) {
		this.card = card;
	}

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

}
