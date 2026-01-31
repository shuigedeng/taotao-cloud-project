package com.taotao.cloud.sys.biz.controller.async;

import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson2.JSONArray;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.taotao.boot.common.extension.CollectionUtils;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.threadpool.configuration.properties.ThreadPoolProperties;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * LongPollingController
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@RestController
@RequestMapping
public class LongPollingController {

    //定时任务，阻塞的最大超时时间。
    private ScheduledExecutorService timeoutChecker = new ScheduledThreadPoolExecutor(1, threadFactory);

    private static final ThreadFactory threadFactory = new ThreadFactoryBuilder()
            .setNameFormat("longPolling-timeout-checker-%d")
            .build();

    private static final Multimap<String, AsyncTask> dataIdContext = Multimaps.synchronizedSetMultimap(
            HashMultimap.create());


    @GetMapping(path = "getConf")
    @ResponseBody
    public String getThreadPoolConf( HttpServletRequest request, HttpServletResponse response ) {
        String serviceName = request.getParameter("serviceName");
        String timeOut = request.getParameter("timeOut");
        if (timeOut == null) {
            //如果没设置过期时间则默认 29秒超时
            timeOut = "29";
        }
        // 开启异步
        AsyncContext asyncContext = request.startAsync(request, response);
        AsyncTask asyncTask = new AsyncTask(asyncContext, true);

        // 维护 serviceName 和异步请求上下文的关联
        dataIdContext.put(serviceName, asyncTask);

        // 启动定时器，30s 后写入 304 响应
        timeoutChecker.schedule(() -> {
            //触发定时后，判断任务是否被执行，即isTimeout为true（没有被执行）
            //则返回客户端304的状态码-即无修改。
            if (asyncTask.isTimeout()) {
                //清除缓存中的任务
                if (dataIdContext.remove(serviceName, asyncTask)) {
                    response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    asyncTask.getAsyncContext().complete();
                }
            }
        }, Integer.parseInt(timeOut), TimeUnit.SECONDS);
        return "";
    }


    //当配置被修改，触发事件后，会调用该方法。
    public void publishConfig( String serviceName, List<ThreadPoolProperties> result ) {
        if (StringUtil.isEmpty(serviceName) && CollectionUtils.isEmpty(result)) {
            LogUtils.error("publishConfig:serviceName:result all is null");
            return;
        }
        if (StringUtil.isNotEmpty(serviceName) && CollectionUtils.isEmpty(result)) {
            LogUtils.error("publishConfig result is null");
            //去持久化的存储源（es/mysql...）读取配置信息
//			result = threadPoolService.getConfigByServiceName(serviceName);
            result = new ArrayList<>();
            if (result == null) {
                LogUtils.error(
                        "publishConfig:serviceName:result all is null but not find threadPoolProperties serviceName:{}",
                        serviceName);
                return;
            }
        }

        //读取到的配置信息，json化
        String configInfo = JSONArray.toJSONString(result);

        //移除AsyncTask的缓存
        Collection<AsyncTask> asyncTasks = dataIdContext.removeAll(serviceName);
        if (CollectionUtils.isEmpty(asyncTasks)) {
            return;
        }

        //为每一个AsyncContext设置200的状态码以及响应数据。
        for (AsyncTask asyncTask : asyncTasks) {
            //表明未超时，已经进行处理了。
            asyncTask.setTimeout(false);
            try {
                HttpServletResponse response = (HttpServletResponse) asyncTask.getAsyncContext().getResponse();
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(configInfo);
            } catch (IOException e) {

            }
            asyncTask.getAsyncContext().complete();
        }
    }

    //自定义任务对象
    @Data
    private static class AsyncTask {

        // 长轮询请求的上下文，包含请求和响应体
        private AsyncContext asyncContext;
        // 超时标记
        private boolean timeout;

        public AsyncTask( AsyncContext asyncContext, boolean timeout ) {
            this.asyncContext = asyncContext;
            this.timeout = timeout;
        }
    }
}
