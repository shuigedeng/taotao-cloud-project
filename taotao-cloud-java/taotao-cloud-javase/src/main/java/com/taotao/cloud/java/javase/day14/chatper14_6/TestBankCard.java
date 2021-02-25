package com.taotao.cloud.java.javase.day14.chatper14_6;

public class TestBankCard {
	public static void main(String[] args) {
		//1创建银行卡
		BankCard card=new BankCard();
		//2创建操作
		AddMoney add=new AddMoney(card);
		SubMoney sub=new SubMoney(card);
		
		//3创建线程对象
		Thread chenchen=new Thread(add, "晨晨");
		Thread bingbing=new Thread(sub, "冰冰");
		
		Thread mingming=new Thread(add, "明明");
		Thread lili=new Thread(sub, "莉莉");
				
		
		//4启动
		chenchen.start();
		mingming.start();
		bingbing.start();
		lili.start();
		
	}
}
