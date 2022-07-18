package com.taotao.cloud.third.client.support.forest.config;

import com.dtflys.forest.callback.RetryWhen;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;

// 自定义重试条件类
// 需要实现 RetryWhen 接口
public class MyRetryCondition implements RetryWhen {
    /**
     * 请求重试条件
     * @param req Forest请求对象
     * @param res Forest响应对象
     * @return true 重试，false 不重试
     */
    @Override
    public boolean retryWhen(ForestRequest req, ForestResponse res) {
        // 响应状态码为 203 就重试
        return res.statusIs(203);
    }
}
