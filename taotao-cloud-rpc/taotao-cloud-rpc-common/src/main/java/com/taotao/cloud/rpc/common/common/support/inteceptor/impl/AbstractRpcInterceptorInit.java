package com.taotao.cloud.rpc.common.common.support.inteceptor.impl;
import com.taotao.cloud.rpc.common.common.support.inteceptor.RpcInterceptor;
import com.taotao.cloud.rpc.common.common.support.inteceptor.RpcInterceptorContext;

import java.util.List;

/**
 * 抽象的初始化拦截器
 *
 * @author shuigedeng
 * @since 0.2.2
 */
public abstract class AbstractRpcInterceptorInit extends RpcInterceptorAdaptor {

    private static final Log log = LogFactory.getLog(AbstractRpcInterceptorInit.class);

    /**
     * 初始化监听器列表
     * @param pipeline 泳道
     * @param context 重试信息
     * @since 0.2.0
     */
    protected abstract void init(final Pipeline<RpcInterceptor> pipeline,
                                 final RpcInterceptorContext context);

    @Override
    public void before(RpcInterceptorContext context) {
        Pipeline<RpcInterceptor> pipeline = new DefaultPipeline<>();
        this.init(pipeline, context);

        List<RpcInterceptor> filterList = pipeline.list();

        for(RpcInterceptor filter : filterList) {
            filter.before(context);
        }
    }

    @Override
    public void after(RpcInterceptorContext context) {
        Pipeline<RpcInterceptor> pipeline = new DefaultPipeline<>();
        this.init(pipeline, context);

        List<RpcInterceptor> filterList = pipeline.list();

        for(RpcInterceptor filter : filterList) {
            filter.after(context);
        }
    }

}
