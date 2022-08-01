/*
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 *
 */
package com.taotao.cloud.sys.biz.modules.dubbo.service;

import com.alibaba.dubbo.remoting.exchange.Request;
import com.alibaba.dubbo.remoting.exchange.Response;
import com.alibaba.dubbo.rpc.RpcResult;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Joey
 * @date 2018/6/11 12:33
 */
public class ResponseDispatcher {

    private Map<Long, CompletableFuture> futures = new ConcurrentHashMap<>();

    private ResponseDispatcher() {

    }

    @SuppressWarnings("uncheck")
    public CompletableFuture<RpcResult> getFuture(Request req) {
        return futures.get(req.getId());
    }

    public void register(Request req) {

        CompletableFuture future = new CompletableFuture();
        futures.put(req.getId(), future);
    }

    public void dispatch(Response res) {

        CompletableFuture future = futures.get(res.getId());
        if (null == future) {
            throw new RuntimeException();
        }
        future.complete(res.getResult());
    }

    public CompletableFuture removeFuture(Request req) {
        return futures.remove(req.getId());
    }

    static class ResponseDispatcherHolder {
        static final ResponseDispatcher instance = new ResponseDispatcher();
    }

    public static ResponseDispatcher getDispatcher() {
        return ResponseDispatcherHolder.instance;
    }
}
