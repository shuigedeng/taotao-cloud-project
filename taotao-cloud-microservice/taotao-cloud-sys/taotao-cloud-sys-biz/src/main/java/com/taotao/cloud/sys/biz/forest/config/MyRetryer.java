package com.taotao.cloud.sys.biz.forest.config;

import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.retryer.BackOffRetryer;

// 自定义重试器
// 继承 BackOffRetryer 类
public class MyRetryer extends BackOffRetryer {

    public MyRetryer(ForestRequest request) {
        super(request);
    }

    /**
     * 重写 nextInterval 方法
     * 该方法用于指定每次重试的时间间隔
     * @param currentCount 当前重试次数
     * @return 时间间隔 (时间单位为毫秒)
     */
    @Override
    protected long nextInterval(int currentCount) {
        // 每次重试时间间隔恒定为 1s (1000ms)
        return 1000;
    }
}
