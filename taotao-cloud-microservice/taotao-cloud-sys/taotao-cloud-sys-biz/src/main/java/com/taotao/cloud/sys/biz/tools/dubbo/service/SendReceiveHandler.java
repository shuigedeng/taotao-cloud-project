/*
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 *
 */
package com.taotao.cloud.sys.biz.tools.dubbo.service;

import com.alibaba.dubbo.remoting.Channel;
import com.alibaba.dubbo.remoting.ChannelHandler;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.TimeoutException;
import com.alibaba.dubbo.remoting.exchange.Request;
import com.alibaba.dubbo.remoting.exchange.Response;
import com.alibaba.dubbo.rpc.RpcResult;
import com.alibaba.fastjson.JSON;
import com.taotao.cloud.common.utils.LogUtil;

public class SendReceiveHandler implements ChannelHandler {


    @Override
    public void connected(Channel channel) throws RemotingException {
	    LogUtil.info("SendReceiveHandler.connected");
    }

    @Override
    public void disconnected(Channel channel) throws RemotingException {
	    LogUtil.info("SendReceiveHandler.disconnected");
    }

    @Override
    public void sent(Channel channel, Object message) throws RemotingException {

	    LogUtil.info("SendReceiveHandler.sent");

        if (message instanceof Request) {

            Request req = (Request) message;
            ResponseDispatcher.getDispatcher().register(req);

        }
    }

    @Override
    public void received(Channel channel, Object message) {

	    LogUtil.info("SendReceiveHandler.received({})", JSON.toJSONString(message));

        if (message instanceof Response) {

            Response res = (Response) message;

            if (res.getStatus() == Response.OK) {
                try {

                    if (res.getResult() instanceof RpcResult) {
                        ResponseDispatcher.getDispatcher().dispatch(res);
                    }

                } catch (Exception e) {
	                LogUtil.error("callback invoke error .result:" + res.getResult() + ",url:" + channel.getUrl(), e);
                }
            } else if (res.getStatus() == Response.CLIENT_TIMEOUT || res.getStatus() == Response.SERVER_TIMEOUT) {
                try {
                    TimeoutException te = new TimeoutException(res.getStatus() == Response.SERVER_TIMEOUT, channel, res.getErrorMessage());
//                    callbackCopy.caught(te);
                } catch (Exception e) {
	                LogUtil.error("callback invoke error ,url:" + channel.getUrl(), e);
                }
            } else {
                try {
                    RuntimeException re = new RuntimeException(res.getErrorMessage());
//                    callbackCopy.caught(re);
                } catch (Exception e) {
	                LogUtil.error("callback invoke error ,url:" + channel.getUrl(), e);
                }
            }
        }
    }

    @Override
    public void caught(Channel channel, Throwable exception) throws RemotingException {
	    LogUtil.error("SendReceiveHandler.caught", exception);
    }

}
