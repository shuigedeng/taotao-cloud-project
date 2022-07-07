package com.taotao.cloud.sys.biz.forest.model;

import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.interceptor.Interceptor;

public class MyRetryInterceptor implements Interceptor<Object> {

    /**
     * 在请重试前调用 onRetry 回调函数
     *
     * @param req Forest请求对象
     * @param res Forest响应对象
     */
    @Override
    public void onRetry(ForestRequest req, ForestResponse res) {
        // req.getCurrentRetryCount() 获取请求当前重试次数
        System.out.println("要重试了！当前重试次数：" + req.getCurrentRetryCount());
    }
}
