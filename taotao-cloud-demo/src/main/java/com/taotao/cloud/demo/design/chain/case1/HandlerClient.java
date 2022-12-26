package com.taotao.cloud.demo.design.chain.case1;


import com.taotao.cloud.demo.design.chain.case1.handler.AbstractCheckHandler;

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
    public static Result executeChain(AbstractCheckHandler handler, ProductVO param) {
        //执行处理器
        Result handlerResult = handler.handle(param);
        if (!handlerResult.isSuccess()) {
            System.out.println("HandlerClient 责任链执行失败返回：" + handlerResult.toString());
            return handlerResult;
        }
        return Result.success();
    }
}
