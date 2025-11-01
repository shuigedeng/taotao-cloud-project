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

package com.taotao.cloud.wechat.biz.wechat.core.login.service;

import cn.bootx.common.redis.RedisClient;
import cn.bootx.starter.auth.exception.LoginFailureException;
import cn.bootx.starter.wechat.dto.login.WeChatLoginQrCode;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpQrcodeService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.stereotype.Service;

/**
 * 扫码事件
 *
 * @author xxm
 * @since 2022/8/4
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatQrLoginService {
    private final RedisClient redisClient;
    private final WxMpService wxMpService;

    private final String PREFIX_KEY = "third:wechat:login:qr:";

    /** 申请待扫描的二维码 */
    @SneakyThrows
    public WeChatLoginQrCode applyQrCode() {
        WxMpQrcodeService qrcodeService = wxMpService.getQrcodeService();
        long timeout = 5 * 60 * 1000;
        String qrCodeKey = IdUtil.getSnowflakeNextIdStr();
        WxMpQrCodeTicket wxMpQrCodeTicket = qrcodeService.qrCodeCreateTmpTicket(qrCodeKey, (int) timeout);
        String url = wxMpQrCodeTicket.getUrl();
        redisClient.setWithTimeout(PREFIX_KEY + qrCodeKey, "", timeout);
        return new WeChatLoginQrCode(qrCodeKey, url);
    }

    /** 查询二维码状态 等待扫码/登录成功/过期 */
    public String getStatus(String key) {
        String openId = redisClient.get(PREFIX_KEY + key);

        if (Objects.isNull(openId)) {
            return "expired";
        } else if (StrUtil.isBlank(openId)) {
            return "wait";
        } else {
            return "ok";
        }
    }

    /** 设置微信openId */
    public void setOpenId(String key, String openId) {
        if (redisClient.exists(PREFIX_KEY + key)) {
            redisClient.set(PREFIX_KEY + key, openId);
        }
    }

    /** 获取 openId */
    public String getOpenId(String key) {
        String openId = redisClient.get(PREFIX_KEY + key);
        if (StrUtil.isBlank(openId)) {
            throw new LoginFailureException("数据已过期或不存在");
        }
        return openId;
    }

    /** 清除扫码信息 */
    public void clear(String key) {
        redisClient.deleteKey(PREFIX_KEY + key);
    }
}
