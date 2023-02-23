package com.taotao.cloud.wechat.biz.wecom.core.notice.executor;

import cn.bootx.common.jackson.util.JacksonUtil;
import cn.hutool.http.HttpUtil;
import me.chanjar.weixin.common.enums.WxType;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.util.http.RequestExecutor;
import me.chanjar.weixin.common.util.http.ResponseHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static cn.bootx.starter.wecom.code.WeComCode.NOTICE_MSG_ID;

/**
* 消息撤回请求执行器.
* @author xxm
* @date 2022/7/23
*/
public class RecallNoticeRequestExecutor implements RequestExecutor<WxError,String> {

    @Override
    public WxError execute(String uri, String data, WxType wxType) throws WxErrorException, IOException {

        Map<String,String> map = new HashMap<>(1);
        map.put(NOTICE_MSG_ID,data);
        String response = HttpUtil.createPost(uri)
                .body(JacksonUtil.toJson(map))
                .execute()
                .body();

        WxError result = WxError.fromJson(response);
        if (result.getErrorCode() != 0) {
            throw new WxErrorException(result);
        }
        return result;
    }

    @Override
    public void execute(String uri, String data, ResponseHandler<WxError> handler, WxType wxType) throws WxErrorException, IOException {
        handler.handle(this.execute(uri, data, wxType));
    }

}
