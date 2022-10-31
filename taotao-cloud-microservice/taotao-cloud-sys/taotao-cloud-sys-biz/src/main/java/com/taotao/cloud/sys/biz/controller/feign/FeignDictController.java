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
package com.taotao.cloud.sys.biz.controller.feign;

import static com.taotao.cloud.web.version.VersionEnum.V2022_07;
import static com.taotao.cloud.web.version.VersionEnum.V2022_08;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.core.configuration.AsyncAutoConfiguration.AsyncThreadPoolTaskExecutor;
import com.taotao.cloud.idempotent.annotation.Idempotent;
import com.taotao.cloud.limit.annotation.GuavaLimit;
import com.taotao.cloud.limit.annotation.Limit;
import com.taotao.cloud.web.request.annotation.RequestLogger;
import com.taotao.cloud.security.annotation.NotAuth;
import com.taotao.cloud.sys.api.feign.IFeignDictApi;
import com.taotao.cloud.sys.api.feign.response.FeignDictResponse;
import com.taotao.cloud.sys.biz.model.convert.DictConvert;
import com.taotao.cloud.sys.biz.model.entity.dict.Dict;
import com.taotao.cloud.sys.biz.service.business.IDictService;
import com.taotao.cloud.web.base.controller.BaseFeignController;
import com.taotao.cloud.web.version.ApiInfo;
import com.yomahub.tlog.core.annotation.TLogAspect;
import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncTask;

/**
 * 内部服务端-字典API
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-10-09 14:24:19
 */
@Validated
@RestController
@RequestMapping("/sys/feign/dict")
public class FeignDictController extends BaseFeignController<IDictService, Dict, Long> {

	@Autowired
	private AsyncThreadPoolTaskExecutor asyncThreadPoolTaskExecutor;

	/**
	 * 字典列表code查询 {@link IFeignDictApi#findByCode(String)}
	 */
	@ApiInfo(
		create = @ApiInfo.Create(version = V2022_07, date = "2022-07-01 17:11:55"),
		update = {
			@ApiInfo.Update(version = V2022_07, content = "主要修改了配置信息的接口查询", date = "2022-07-01 17:11:55"),
			@ApiInfo.Update(version = V2022_08, content = "主要修改了配置信息的接口查询08", date = "2022-07-01 17:11:55")
		}
	)
	@NotAuth
	@Idempotent(perFix = "findByCode")
	@Limit(key = "limitTest", period = 10, count = 3)
	@SentinelResource("findByCode")
	@GetMapping("/code")
	public FeignDictResponse findByCode(@RequestParam(value = "code") String code) {
		if ("sd".equals(code)) {
			throw new BusinessException("我出错了");
			//try {
			//	Thread.sleep(100000000000L);
			//} catch (InterruptedException e) {
			//	throw new RuntimeException(e);
			//}
		}
		Dict dict = service().findByCode(code);
		return DictConvert.INSTANCE.convert(dict);
	}

	@Operation(summary = "test", description = "test")
	@RequestLogger
	@NotAuth
	@Idempotent(perFix = "test")
	@TLogAspect(value = {"code"}, pattern = "{{}}", joint = ",", str = "nihao")
	@Limit(key = "limitTest", period = 10, count = 3)
	@GuavaLimit
	@SentinelResource("test")
	@GetMapping("/test")
	public Dict test(@RequestParam(value = "code") String code) {
		LogUtils.info("sldfkslfdjalsdfkjalsfdjl");
		Dict dict = service().findByCode(code);

		Future<Dict> asyncByCode = service().findAsyncByCode(code);

		Dict dict1;
		try {
			dict1 = asyncByCode.get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}

		LogUtils.info("我在等待你");

		return dict1;
		//return IDictMapStruct.INSTANCE.dictToFeignDictRes(dict);
	}

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
