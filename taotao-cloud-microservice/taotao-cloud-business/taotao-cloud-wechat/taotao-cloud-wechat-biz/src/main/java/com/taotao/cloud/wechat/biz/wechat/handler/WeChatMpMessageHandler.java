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

package com.taotao.cloud.wechat.biz.wechat.handler;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.constant.WxMpEventConstants;

/**
 * 处理微信推送消息的处理器接口 (进行微封装)
 *
 * @author xxm
 * @since 2022/7/16
 */
public interface WeChatMpMessageHandler extends WxMpMessageHandler {

    /**
     * 消息类型
     *
     * @see WxConsts.XmlMsgType
     */
    default String getMsgType() {
        return WxConsts.XmlMsgType.EVENT;
    }

    /**
     * event值
     *
     * @see WxMpEventConstants
     * @see WxConsts.XmlMsgType
     */
    String getEvent();
}
