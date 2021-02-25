package com.taotao.cloud.java.mq.queue;


public class ConsumerTest implements Runnable {
	static Thread t1 = null;

	public static void main(String[] args) throws InterruptedException {

		t1 = new Thread(new ConsumerTest());
		t1.start();

		while (true) {
			System.out.println(t1.isAlive());
			if (!t1.isAlive()) {
				t1 = new Thread(new ConsumerTest());
				t1.start();
				System.out.println("重新启动");
			}
			Thread.sleep(5000);
		}
		// 延时500毫秒之后停止接受消息
		// Thread.sleep(500);
		// consumer.close();
	}

	@Override
	public void run() {
		try {
			ConsumerTool consumer = new ConsumerTool();
			consumer.consumeMessage();
			while (ConsumerTool.isconnection) {
				//System.out.println(123);
			}
		} catch (Exception e) {
		}

	}
}
