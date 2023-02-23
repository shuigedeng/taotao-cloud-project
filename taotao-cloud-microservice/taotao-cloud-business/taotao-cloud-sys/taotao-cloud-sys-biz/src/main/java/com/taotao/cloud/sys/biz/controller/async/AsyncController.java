package com.taotao.cloud.sys.biz.controller.async;

/**
 * 第一个请求/hello，会先将deferredResult存起来，前端页面是一直等待（转圈）状态。直到发第二个请求：setHelloToAll，所有的相关页面才会有响应。
 * <p>
 * 整个执行流程如下：
 * <p>
 * controller返回一个DeferredResult，把它保存到内存里或者List里面（供后续访问）； Spring
 * MVC调用request.startAsync()，开启异步处理；与此同时将DispatcherServlet里的拦截器、Filter等等都马上退出主线程，但是response仍然保持打开的状态；
 * 应用通过另外一个线程（可能是MQ消息、定时任务等）给DeferredResult#setResult值。然后SpringMVC会把这个请求再次派发给servlet容器；
 * DispatcherServlet再次被调用，然后处理后续的标准流程；
 * 通过上述流程可以发现：利用DeferredResult可实现一些长连接的功能，比如当某个操作是异步时，可以先保存对应的DeferredResult对象，当异步通知回来时，再找到这个DeferredResult对象，在setResult处理结果即可。从而提高性能。
 */

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.core.configuration.AsyncAutoConfiguration.AsyncThreadPoolTaskExecutor;
import com.taotao.cloud.sys.biz.model.entity.dict.Dict;
import com.taotao.cloud.sys.biz.service.business.IDictService;
import com.taotao.cloud.sys.biz.service.feign.IFeignDictService;
import com.taotao.cloud.web.base.controller.BaseFeignController;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.context.request.async.WebAsyncTask;

@Validated
@RestController
@RequestMapping("/sys/async/hello")
public class AsyncController extends BaseFeignController<IDictService, Dict, Long> {

	@Autowired
	private IFeignDictService feignDictService;
	@Autowired
	private AsyncThreadPoolTaskExecutor asyncThreadPoolTaskExecutor;

	private final List<DeferredResult<String>> deferredResultList = new ArrayList<>();

	@ResponseBody
	@GetMapping("/hello")
	public DeferredResult<String> helloGet() throws Exception {
		DeferredResult<String> deferredResult = new DeferredResult<>();

		//先存起来，等待触发
		deferredResultList.add(deferredResult);
		return deferredResult;
	}

	@ResponseBody
	@GetMapping("/setHelloToAll")
	public void helloSet() throws Exception {
		// 让所有hold住的请求给与响应
		deferredResultList.forEach(d -> d.setResult("say hello to all"));
	}


	//*************************************************************


	/**
	 * @Async、WebAsyncTask、Callable、DeferredResult的区别 所在的包不同：
	 * @Async：org.springframework.scheduling.annotation;
	 * WebAsyncTask：org.springframework.web.context.request.async; Callable：java.util.concurrent；
	 * DeferredResult：org.springframework.web.context.request.async;
	 * 通过所在的包，我们应该隐隐约约感到一些区别，比如@Async是位于scheduling包中，而WebAsyncTask和DeferredResult是用于Web（Spring
	 * MVC）的，而Callable是用于concurrent（并发）处理的。
	 * <p>
	 * 对于Callable，通常用于Controller方法的异步请求，当然也可以用于替换Runable的方式。在方法的返回上与正常的方法有所区别：
	 * <p>
	 * 而WebAsyncTask是对Callable的封装，提供了一些事件回调的处理，本质上区别不大。
	 * <p>
	 * DeferredResult使用方式与Callable类似，重点在于跨线程之间的通信。
	 * @Async也是替换Runable的一种方式，可以代替我们自己创建线程。而且适用的范围更广，并不局限于Controller层，而可以是任何层的方法上。
	 */
	@RequestMapping("/asyncTask")
	public void asyncTask(HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		System.out.println("控制层线程:" + Thread.currentThread().getName());
		AsyncContext asyncContext = request.startAsync();
		asyncContext.addListener(new AsyncListener() {
			@Override
			public void onComplete(AsyncEvent asyncEvent) throws IOException {
				// 异步执行完毕时
				System.out.println("异步执行完毕");
			}

			@Override
			public void onTimeout(AsyncEvent asyncEvent) throws IOException {
				//异步线程执行超时
				System.out.println("异步线程执行超时");
			}

			@Override
			public void onError(AsyncEvent asyncEvent) throws IOException {
				//异步线程出错时
				System.out.println("异步线程出错");
			}

			@Override
			public void onStartAsync(AsyncEvent asyncEvent) throws IOException {
				//异步线程开始时
				System.out.println("异步线程开始");
			}
		});
		asyncContext.setTimeout(3000);
		asyncContext.start(() -> {
			try {
				System.out.println("异步执行线程:" + Thread.currentThread().getName());
				//String str = piceaService.task2();
				Thread.sleep(1000);
				asyncContext.getResponse().setCharacterEncoding("UTF-8");
				asyncContext.getResponse().setContentType("text/html;charset=UTF-8");
				asyncContext.getResponse().getWriter().println("这是【异步】的请求返回: ");
			} catch (Exception e) {
				e.printStackTrace();
			}
			//异步请求完成通知，所有任务完成了，才执行
			asyncContext.complete();
		});

	}

	/*** 异步，不阻塞Tomcat的线程 ，提升Tomcat吞吐量***/
	@RequestMapping("/async")
	public DeferredResult<String> async() {
		System.out.println(" 当前线程 外部 " + Thread.currentThread().getName());
		DeferredResult<String> result = new DeferredResult<>();
		CompletableFuture.supplyAsync(() -> service().async(), asyncThreadPoolTaskExecutor)
			.whenCompleteAsync((res, throwable) -> result.setResult(res));
		return result;
	}

	/*** 异步，不阻塞Tomcat的线程 ，提升Tomcat吞吐量***/
	@RequestMapping("/async2")
	public Callable<String> async2() {
		System.out.println(" 当前线程 外部 " + Thread.currentThread().getName());
		Callable<String> callable = () -> {
			System.out.println(" 当前线程 内部 " + Thread.currentThread().getName());
			return "success";
		};
		return callable;
	}

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

	@GetMapping("/email")
	public Callable<String> order() {
		System.out.println("主线程开始：" + Thread.currentThread().getName());
		Callable<String> result = () -> {
			System.out.println("副线程开始：" + Thread.currentThread().getName());
			Thread.sleep(1000);
			System.out.println("副线程返回：" + Thread.currentThread().getName());
			return "success";
		};

		System.out.println("主线程返回：" + Thread.currentThread().getName());
		return result;
	}
}
