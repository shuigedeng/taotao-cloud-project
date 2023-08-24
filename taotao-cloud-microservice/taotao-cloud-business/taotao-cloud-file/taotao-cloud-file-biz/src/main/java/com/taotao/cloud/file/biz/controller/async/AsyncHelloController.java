package com.taotao.cloud.file.biz.controller.async;

import com.taotao.cloud.core.configuration.AsyncAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

//第一个请求/hello，会先将deferredResult存起来，前端页面是一直等待（转圈）状态。
// 直到发第二个请求：setHelloToAll，所有的相关页面才会有响应。
@RestController
@RequestMapping("/file/async/hello")
public class AsyncHelloController {

	@Autowired
	private AsyncAutoConfiguration.AsyncThreadPoolTaskExecutor asyncThreadPoolTaskExecutor;

    private List<DeferredResult<String>> deferredResultList = new ArrayList<>();


    @GetMapping("/hello")
    public DeferredResult<String> helloGet() throws Exception {
        DeferredResult<String> deferredResult = new DeferredResult<>();

        //先存起来，等待触发
        deferredResultList.add(deferredResult);
        return deferredResult;
    }

    @GetMapping("/setHelloToAll")
    public void helloSet() throws Exception {
        // 让所有hold住的请求给与响应
        deferredResultList.forEach(d -> d.setResult("say hello to all"));
    }

////////////////////////////////

	@RequestMapping(value = "/email/deferredResultReq", method = GET)
	public DeferredResult<String> deferredResultReq () {
		System.out.println("外部线程：" + Thread.currentThread().getName());
		//设置超时时间
		DeferredResult<String> result = new DeferredResult<String>(60*1000L);

		//处理超时事件 采用委托机制
		result.onTimeout(new Runnable() {
			@Override
			public void run() {
				System.out.println("DeferredResult超时");
				result.setResult("超时了!");
			}
		});
		result.onCompletion(new Runnable() {
			@Override
			public void run() {
				//完成后
				System.out.println("调用完成");
			}
		});

		asyncThreadPoolTaskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				//处理业务逻辑
				System.out.println("内部线程：" + Thread.currentThread().getName());
				//返回结果
				result.setResult("DeferredResult!!");
			}
		});
		return result;
	}
}
