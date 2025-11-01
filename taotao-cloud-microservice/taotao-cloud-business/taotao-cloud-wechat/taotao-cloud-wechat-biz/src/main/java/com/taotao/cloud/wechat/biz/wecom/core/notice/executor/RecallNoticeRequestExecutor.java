/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.wechat.biz.wecom.core.notice.executor;

import static cn.bootx.starter.wecom.code.WeComCode.NOTICE_MSG_ID;

import cn.bootx.common.jackson.util.JacksonUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import me.chanjar.weixin.common.enums.WxType;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.util.http.RequestExecutor;
import me.chanjar.weixin.common.util.http.ResponseHandler;

/**
 * 消息撤回请求执行器.
 *
 * @author xxm
 * @since 2022/7/23
 */
public class RecallNoticeRequestExecutor implements RequestExecutor<WxError, String> {

    @Override
    public WxError execute(String uri, String data, WxType wxType) throws WxErrorException, IOException {

        Map<String, String> map = new HashMap<>(1);
        map.put(NOTICE_MSG_ID, data);
        String response =
                HttpUtil.createPost(uri).body(JacksonUtil.toJson(map)).execute().body();

        WxError result = WxError.fromJson(response);
        if (result.getErrorCode() != 0) {
            throw new WxErrorException(result);
        }
        return result;
    }

    @Override
    public void execute(String uri, String data, ResponseHandler<WxError> handler, WxType wxType)
            throws WxErrorException, IOException {
        handler.handle(this.execute(uri, data, wxType));
    }
}
