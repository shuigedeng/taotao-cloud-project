package com.taotao.cloud.rpc.base.service;

import com.taotao.cloud.rpc.base.HelloWorldService;
import com.taotao.cloud.rpc.base.pojo.BlogJSONResult;
import com.taotao.cloud.rpc.common.annotation.Service;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Service(name = "helloService", group = "1.0.0")
public class HelloWorldServiceImpl implements HelloWorldService {

	private AtomicInteger count = new AtomicInteger(0);

	@Override
	public BlogJSONResult sayHello(String message) {
		try {
			Thread.sleep(new Random().nextInt(500));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("调用次数" + count.incrementAndGet());
		System.out.println("服务端接收到数据： " + message);
		return BlogJSONResult.ok("服务端响应数据： " + message + " 已经被我们收到了");
	}
}
