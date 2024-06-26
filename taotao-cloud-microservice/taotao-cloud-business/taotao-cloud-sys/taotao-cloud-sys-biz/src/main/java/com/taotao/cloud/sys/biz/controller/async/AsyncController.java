/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.sys.biz.controller.async;


import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.security.springsecurity.annotation.NotAuth;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncTask;

/**
 * 第一个请求/hello，会先将deferredResult存起来，前端页面是一直等待（转圈）状态。直到发第二个请求：setHelloToAll，所有的相关页面才会有响应。
 *
 * <p>整个执行流程如下：
 *
 * <p>controller返回一个DeferredResult，把它保存到内存里或者List里面（供后续访问）； Spring
 * MVC调用request.startAsync()，开启异步处理；与此同时将DispatcherServlet里的拦截器、Filter等等都马上退出主线程，但是response仍然保持打开的状态；
 * 应用通过另外一个线程（可能是MQ消息、定时任务等）给DeferredResult#setResult值。然后SpringMVC会把这个请求再次派发给servlet容器；
 * DispatcherServlet再次被调用，然后处理后续的标准流程；
 * 通过上述流程可以发现：利用DeferredResult可实现一些长连接的功能，比如当某个操作是异步时，可以先保存对应的DeferredResult对象，当异步通知回来时，再找到这个DeferredResult对象，在setResult处理结果即可。从而提高性能。
 * <p>
 * 基于Spring实现异步请求 基于Spring可以通过Callable、DeferredResult或者WebAsyncTask等方式实现异步请求。
 */
@Validated
@RestController
@RequestMapping("/file/async")
public class AsyncController {

	@Autowired
	@Qualifier("asyncThreadPoolTaskExecutor")
	private ThreadPoolTaskExecutor asyncThreadPoolTaskExecutor;

	private final List<DeferredResult<String>> deferredResultList = new ArrayList<>();

	@NotAuth
	@Operation(summary = "helloGet", description = "helloGet")
	@GetMapping("/helloGet")
	public DeferredResult<String> helloGet() throws Exception {
		DeferredResult<String> deferredResult = new DeferredResult<>();

		// 先存起来，等待触发
		deferredResultList.add(deferredResult);
		return deferredResult;
	}

	@NotAuth
	@Operation(summary = "setHelloToAll", description = "setHelloToAll")
	@GetMapping("/setHelloToAll")
	public void helloSet() throws Exception {
		// 让所有hold住的请求给与响应
		deferredResultList.forEach(d -> d.setResult("say hello to all"));
	}

	/*** 异步，不阻塞Tomcat的线程 ，提升Tomcat吞吐量***/
	@NotAuth
	@Operation(summary = "deferredResultasync", description = "deferredResultasync")
	@RequestMapping("/deferredResultasync")
	public DeferredResult<String> deferredResultasync() {
		LogUtils.info(" 当前线程 外部 " + Thread.currentThread().getName());
		DeferredResult<String> result = new DeferredResult<>();
		CompletableFuture.supplyAsync(() -> "sdfsaf", asyncThreadPoolTaskExecutor)
			.whenCompleteAsync((res, throwable) -> result.setResult(res));
		return result;
	}

	// *************************************************************

	/**
	 * @Async、WebAsyncTask、Callable、DeferredResult的区别 所在的包不同：
	 * @Async：org.springframework.scheduling.annotation;
	 * WebAsyncTask：org.springframework.web.context.request.async; Callable：java.util.concurrent；
	 * DeferredResult：org.springframework.web.context.request.async;
	 * 通过所在的包，我们应该隐隐约约感到一些区别，比如@Async是位于scheduling包中，而WebAsyncTask和DeferredResult是用于Web（Spring
	 * MVC）的，而Callable是用于concurrent（并发）处理的。
	 *
	 * <p>对于Callable，通常用于Controller方法的异步请求，当然也可以用于替换Runable的方式。在方法的返回上与正常的方法有所区别：
	 *
	 * <p>而WebAsyncTask是对Callable的封装，提供了一些事件回调的处理，本质上区别不大。
	 *
	 * <p>DeferredResult使用方式与Callable类似，重点在于跨线程之间的通信。
	 * @Async也是替换Runable的一种方式，可以代替我们自己创建线程。而且适用的范围更广，并不局限于Controller层，而可以是任何层的方法上。
	 */
	@NotAuth
	@Operation(summary = "asyncTask", description = "asyncTask")
	@RequestMapping("/asyncTask")
	public void asyncTask(HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		LogUtils.info("控制层线程:" + Thread.currentThread().getName());
		AsyncContext asyncContext = request.startAsync();
		// 设置监听器:可设置其开始、完成、异常、超时等事件的回调处理
		asyncContext.addListener(new AsyncListener() {
			@Override
			public void onComplete(AsyncEvent asyncEvent) throws IOException {
				// 异步执行完毕时
				LogUtils.info("异步执行完毕");
			}

			@Override
			public void onTimeout(AsyncEvent asyncEvent) throws IOException {
				// 异步线程执行超时
				LogUtils.info("异步线程执行超时");
			}

			@Override
			public void onError(AsyncEvent asyncEvent) throws IOException {
				// 异步线程出错时
				LogUtils.info("异步线程出错");
			}

			@Override
			public void onStartAsync(AsyncEvent asyncEvent) throws IOException {
				// 异步线程开始时
				LogUtils.info("异步线程开始");
			}
		});
		//设置超时时间
		asyncContext.setTimeout(3000);
		asyncContext.start(() -> {
			try {
				LogUtils.info("异步执行线程:" + Thread.currentThread().getName());
				// String str = piceaService.task2();
				Thread.sleep(1000);

				asyncContext.getResponse().setCharacterEncoding("UTF-8");
				asyncContext.getResponse().setContentType("text/html;charset=UTF-8");
				asyncContext.getResponse().getWriter().println("这是【异步】的请求返回: ");
			}
			catch (Exception e) {
				LogUtils.info("异步处理发生异常：" + e.getMessage());
			}

			// 异步请求完成通知，所有任务完成了，才执行
			asyncContext.complete();
		});
		//此时request的线程连接已经释放了
		LogUtils.info("主线程：" + Thread.currentThread().getName());
	}


	/*** 异步，不阻塞Tomcat的线程 ，提升Tomcat吞吐量***/
	@NotAuth
	@Operation(summary = "callableTest1", description = "callableTest1")
	@RequestMapping("/callableTest1")
	public Callable<String> callableTest1() {
		LogUtils.info(" 当前线程 外部 " + Thread.currentThread().getName());
		Callable<String> callable = () -> {
			LogUtils.info(" 当前线程 内部 " + Thread.currentThread().getName());
			return "success";
		};
		return callable;
	}

	@NotAuth
	@Operation(summary = "callableTest2", description = "callableTest2")
	@GetMapping("/callableTest2")
	public Callable<String> callableTest2() {
		LogUtils.info("主线程开始：" + Thread.currentThread().getName());
		Callable<String> result = () -> {
			LogUtils.info("副线程开始：" + Thread.currentThread().getName());
			Thread.sleep(1000);
			LogUtils.info("副线程返回：" + Thread.currentThread().getName());
			return "success";
		};

		LogUtils.info("主线程返回：" + Thread.currentThread().getName());
		return result;
	}

	@NotAuth
	@Operation(summary = "webAsyncTask", description = "webAsyncTask")
	@GetMapping("/webAsyncTask")
	public WebAsyncTask<String> webAsyncTask() {
		LogUtils.info("外部线程：" + Thread.currentThread().getName());
		WebAsyncTask<String> result = new WebAsyncTask<>(60 * 1000L, () -> {
			LogUtils.info("内部线程：" + Thread.currentThread().getName());
			return "success";
		});
		result.onTimeout(() -> {
			LogUtils.info("timeout callback");
			return "timeout callback";
		});
		result.onCompletion(() -> LogUtils.info("finish callback"));
		return result;
	}

}
