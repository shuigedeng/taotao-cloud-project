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

package com.taotao.cloud.wechat.biz.mp.framework.mp.core;

import cn.iocoder.yudao.module.mp.dal.dataobject.account.MpAccountDO;
import java.util.List;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;

/**
 * {@link WxMpService} 工厂接口
 *
 * @author 芋道源码
 */
public interface MpServiceFactory {

    /**
     * 基于微信公众号的账号，初始化对应的 WxMpService 与 WxMpMessageRouter 实例
     *
     * @param list 公众号的账号列表
     */
    void init(List<MpAccountDO> list);

    /**
     * 获得 id 对应的 WxMpService 实例
     *
     * @param id 微信公众号的编号
     * @return WxMpService 实例
     */
    WxMpService getMpService(Long id);

    default WxMpService getRequiredMpService(Long id) {
        WxMpService wxMpService = getMpService(id);
        Assert.notNull(wxMpService, "找到对应 id({}) 的 WxMpService，请核实！", id);
        return wxMpService;
    }

    /**
     * 获得 appId 对应的 WxMpService 实例
     *
     * @param appId 微信公众号 appId
     * @return WxMpService 实例
     */
    WxMpService getMpService(String appId);

    default WxMpService getRequiredMpService(String appId) {
        WxMpService wxMpService = getMpService(appId);
        Assert.notNull(wxMpService, "找到对应 appId({}) 的 WxMpService，请核实！", appId);
        return wxMpService;
    }

    /**
     * 获得 appId 对应的 WxMpMessageRouter 实例
     *
     * @param appId 微信公众号 appId
     * @return WxMpMessageRouter 实例
     */
    WxMpMessageRouter getMpMessageRouter(String appId);

    default WxMpMessageRouter getRequiredMpMessageRouter(String appId) {
        WxMpMessageRouter wxMpMessageRouter = getMpMessageRouter(appId);
        Assert.notNull(wxMpMessageRouter, "找到对应 appId({}) 的 WxMpMessageRouter，请核实！", appId);
        return wxMpMessageRouter;
    }
}
