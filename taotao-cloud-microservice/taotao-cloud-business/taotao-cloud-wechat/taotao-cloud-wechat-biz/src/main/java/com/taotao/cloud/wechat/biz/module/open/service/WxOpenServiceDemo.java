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

package com.taotao.cloud.wechat.biz.module.open.service;

import com.taotao.cloud.wechat.biz.module.open.config.RedisProperies;
import com.taotao.cloud.wechat.biz.module.open.config.WechatOpenProperties;
import jakarta.annotation.PostConstruct;
import me.chanjar.weixin.open.api.impl.WxOpenInRedisConfigStorage;
import me.chanjar.weixin.open.api.impl.WxOpenMessageRouter;
import me.chanjar.weixin.open.api.impl.WxOpenServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;

/**
 * @author <a href="https://github.com/007gzs">007</a>
 */
@Service
@EnableConfigurationProperties({WechatOpenProperties.class, RedisProperies.class})
public class WxOpenServiceDemo extends WxOpenServiceImpl {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WechatOpenProperties wechatOpenProperties;

    @Autowired
    private RedisProperies redisProperies;

    private static volatile JedisPool pool;
    private WxOpenMessageRouter wxOpenMessageRouter;

    @PostConstruct
    public void init() {
        WxOpenInRedisConfigStorage inRedisConfigStorage = new WxOpenInRedisConfigStorage(getJedisPool());
        inRedisConfigStorage.setComponentAppId(wechatOpenProperties.getComponentAppId());
        inRedisConfigStorage.setComponentAppSecret(wechatOpenProperties.getComponentSecret());
        inRedisConfigStorage.setComponentToken(wechatOpenProperties.getComponentToken());
        inRedisConfigStorage.setComponentAesKey(wechatOpenProperties.getComponentAesKey());
        setWxOpenConfigStorage(inRedisConfigStorage);
        wxOpenMessageRouter = new WxOpenMessageRouter(this);
        wxOpenMessageRouter
                .rule()
                .handler((wxMpXmlMessage, map, wxMpService, wxSessionManager) -> {
                    logger.info(
                            "\n接收到 {} 公众号请求消息，内容：{}",
                            wxMpService.getWxMpConfigStorage().getAppId(),
                            wxMpXmlMessage);
                    return null;
                })
                .next();
    }

    public WxOpenMessageRouter getWxOpenMessageRouter() {
        return wxOpenMessageRouter;
    }

    private JedisPool getJedisPool() {
        if (pool == null) {
            synchronized (WxOpenServiceDemo.class) {
                if (pool == null) {
                    pool = new JedisPool(
                            redisProperies,
                            redisProperies.getHost(),
                            redisProperies.getPort(),
                            redisProperies.getConnectionTimeout(),
                            redisProperies.getSoTimeout(),
                            redisProperies.getPassword(),
                            redisProperies.getDatabase(),
                            redisProperies.getClientName(),
                            redisProperies.isSsl(),
                            redisProperies.getSslSocketFactory(),
                            redisProperies.getSslParameters(),
                            redisProperies.getHostnameVerifier());
                }
            }
        }
        return pool;
    }
}
