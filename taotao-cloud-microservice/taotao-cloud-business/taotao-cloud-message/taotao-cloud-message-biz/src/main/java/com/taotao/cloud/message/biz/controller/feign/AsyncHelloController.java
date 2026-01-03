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

package com.taotao.cloud.message.biz.controller.feign;

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
 */

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * AsyncHelloController
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Controller
@RequestMapping("/async/controller")
public class AsyncHelloController {

    private final List<DeferredResult<String>> deferredResultList = new ArrayList<>();

    @ResponseBody
    @GetMapping("/hello")
    public DeferredResult<String> helloGet() throws Exception {
        DeferredResult<String> deferredResult = new DeferredResult<>();

        // 先存起来，等待触发
        deferredResultList.add(deferredResult);
        return deferredResult;
    }

    @ResponseBody
    @GetMapping("/setHelloToAll")
    public void helloSet() throws Exception {
        // 让所有hold住的请求给与响应
        deferredResultList.forEach(d -> d.setResult("say hello to all"));
    }
}
