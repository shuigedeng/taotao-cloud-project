package com.taotao.cloud.java.javase.day14.chatper14_7;

public class Test {
	public static void main(String[] args) {
		//容器
		BreadCon con=new BreadCon();
		//生产和消费
		Prodcut prodcut=new Prodcut(con);
		Consume consume=new Consume(con);
		//创建线程对象
		Thread chenchen=new Thread(prodcut, "晨晨");
		Thread bingbing=new Thread(consume, "消费");
		Thread mingming=new Thread(prodcut, "明明");
		Thread lili=new Thread(consume, "莉莉");
		//启动线程
		chenchen.start();
		bingbing.start();
		mingming.start();
		lili.start();
	}
}
