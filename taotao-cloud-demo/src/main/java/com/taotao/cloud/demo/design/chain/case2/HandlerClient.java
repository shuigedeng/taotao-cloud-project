package com.taotao.cloud.demo.design.chain.case2;

/**
 * 责任链模式之客户端
 */
public class HandlerClient {

    /**
     * 执行链路
     * @param handler 处理器
     * @param param 商品参数
     * @return
     */
    public static boolean executeChain(AbstractFlowHandler handler, MoneyPayVO param) {
        //执行处理器
        return true;
    }
}
