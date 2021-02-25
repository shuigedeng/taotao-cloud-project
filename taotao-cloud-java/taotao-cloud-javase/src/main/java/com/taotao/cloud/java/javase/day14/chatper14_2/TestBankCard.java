package com.taotao.cloud.java.javase.day14.chatper14_2;

public class TestBankCard {
	public static void main(String[] args) {
		//1创建一张银行卡
		BankCard card=new BankCard();
		//2创建存钱、取钱
		AddMoney add=new AddMoney(card);
		SubMoney sub=new SubMoney(card);
		//3创建两个线程
		Thread chenchen=new Thread(add, "晨晨");
		Thread bingbing=new Thread(sub, "冰冰");
		//4启动线程
		chenchen.start();
		bingbing.start();
	}
}
