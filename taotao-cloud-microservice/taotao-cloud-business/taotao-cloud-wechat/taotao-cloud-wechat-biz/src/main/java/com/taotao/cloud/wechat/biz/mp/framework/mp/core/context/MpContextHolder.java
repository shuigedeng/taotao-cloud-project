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

package com.taotao.cloud.wechat.biz.mp.framework.mp.core.context;

import cn.iocoder.yudao.module.mp.controller.admin.open.vo.MpOpenHandleMessageReqVO;
import com.alibaba.ttl.TransmittableThreadLocal;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;

/**
 * 微信上下文 Context
 *
 * <p>目的：解决微信多公众号的问题，在 {@link WxMpMessageHandler} 实现类中，可以通过 {@link #getAppId()} 获取到当前的 appId
 *
 * @see cn.iocoder.yudao.module.mp.controller.admin.open.MpOpenController#handleMessage(String,
 *     String, MpOpenHandleMessageReqVO)
 * @author 芋道源码
 */
public class MpContextHolder {

    /** 微信公众号的 appId 上下文 */
    private static final ThreadLocal<String> APPID = new TransmittableThreadLocal<>();

    public static void setAppId(String appId) {
        APPID.set(appId);
    }

    public static String getAppId() {
        return APPID.get();
    }

    public static void clear() {
        APPID.remove();
    }
}
