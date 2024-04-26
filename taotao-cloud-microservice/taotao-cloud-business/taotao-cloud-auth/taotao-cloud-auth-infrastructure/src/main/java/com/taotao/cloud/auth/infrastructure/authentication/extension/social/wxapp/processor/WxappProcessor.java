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

package com.taotao.cloud.auth.infrastructure.authentication.extension.social.wxapp.processor;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaMediaAsyncCheckResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;
import com.google.common.collect.Maps;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.wxapp.properties.WxappProperties;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.error.WxRuntimeException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * <p>微信小程序核心基础代码 </p>
 *
 *
 * @since : 2021/5/27 20:29
 */
public class WxappProcessor implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(WxappProcessor.class);

    private WxappProperties wxappProperties;
    private WxappLogHandler wxappLogHandler;

    private Map<String, WxMaMessageRouter> wxMaMessagerouters = Maps.newHashMap();
    private Map<String, WxMaService> wxMaServices = Maps.newHashMap();

    public void setWxappProperties(WxappProperties wxappProperties) {
        this.wxappProperties = wxappProperties;
    }

    public void setWxappLogHandler(WxappLogHandler wxappLogHandler) {
        this.wxappLogHandler = wxappLogHandler;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<WxappProperties.Config> configs = this.wxappProperties.getConfigs();
        if (configs == null) {
            throw new WxRuntimeException("Weixin Mini App Configuraiton is not setting!");
        }

        wxMaServices = configs.stream()
                .map(a -> {
                    WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
                    //                WxMaDefaultConfigImpl config = new WxMaRedisConfigImpl(new JedisPool());
                    // 使用上面的配置时，需要同时引入jedis-lock的依赖，否则会报类无法找到的异常
                    config.setAppid(a.getAppId());
                    config.setSecret(a.getSecret());
                    config.setToken(a.getToken());
                    config.setAesKey(a.getAesKey());
                    config.setMsgDataFormat(a.getMessageDataFormat());

                    WxMaService service = new WxMaServiceImpl();
                    service.setWxMaConfig(config);
                    wxMaMessagerouters.put(a.getAppId(), this.newRouter(service));
                    return service;
                })
                .collect(Collectors.toMap(s -> s.getWxMaConfig().getAppid(), a -> a));

        log.info("Bean  Weixin Mini App] Auto Configure.");
    }

    private WxMaMessageRouter newRouter(WxMaService wxMaService) {
        final WxMaMessageRouter router = new WxMaMessageRouter(wxMaService);
        router.rule().handler(wxappLogHandler).next();
        return router;
    }

    public WxMaService getWxMaService(String appid) {
        WxMaService wxMaService = wxMaServices.get(appid);
        if (ObjectUtils.isEmpty(wxMaService)) {
            throw new IllegalArgumentException(
                    String.format("Cannot find the configuration of appid=[%s], please check!", appid));
        }

        return wxMaService;
    }

    public WxMaMessageRouter getWxMaMessageRouter(String appid) {
        return wxMaMessagerouters.get(appid);
    }

    public WxMaService getWxMaService() {
        String appId = wxappProperties.getDefaultAppId();
        if (StringUtils.isBlank(appId)) {
            log.error(
                    "Must set .platform.social.wxapp.default-app-id] property, or use getWxMaService(String appid)!");
            throw new IllegalArgumentException("Must set .platform.social.wxapp.default-app-id] property");
        }
        return this.getWxMaService(appId);
    }

    /**
     * 获取登录后的session信息.
     *
     * @param code        登录时获取的 code
     * @param wxMaService 微信小程序服务
     * @return {@link WxMaJscode2SessionResult}
     */
    private WxMaJscode2SessionResult getSessionInfo(String code, WxMaService wxMaService) {
        try {
            WxMaJscode2SessionResult sessionResult =
                    wxMaService.getUserService().getSessionInfo(code);
            log.debug("Weixin Mini App login successfully!");
            return sessionResult;
        } catch (WxErrorException e) {
            log.error("Weixin Mini App login failed! For reason: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 验证用户完整性
     *
     * @param sessionKey  会话密钥
     * @param rawData     微信用户基本信息
     * @param signature   数据签名
     * @param wxMaService 微信小程序服务
     * @return true 完整， false 不完整
     */
    private boolean checkUserInfo(String sessionKey, String rawData, String signature, WxMaService wxMaService) {
        if (wxMaService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            log.debug("Weixin Mini App user info is valid!");
            return true;
        } else {
            log.warn("Weixin Mini App user check failed!");
            return false;
        }
    }

    /**
     * 解密用户信息
     *
     * @param sessionKey    会话密钥
     * @param encryptedData 消息密文
     * @param iv            加密算法的初始向量
     * @param wxMaService   微信小程序服务
     * @return {@link WxMaUserInfo}
     */
    private WxMaUserInfo getUserInfo(String sessionKey, String encryptedData, String iv, WxMaService wxMaService) {
        WxMaUserInfo wxMaUserInfo = wxMaService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
        log.debug("Weixin Mini App get user info successfully!");
        return wxMaUserInfo;
    }

    /**
     * 解密手机号
     * <p>
     * 确认下前后端传递的参数有没有做UrlEncode/UrlDecode，因为encryptedData里会包含特殊字符在传递参数时被转义，可能服务器端实际拿到的参数encryptedData并不是前端实际获取到的值，导致SDK调用微信相应接口时无法解密而报错，只要保证前端实际获取到的encryptedData和服务器端调用SDK时传入的encryptedData一致就不会报错的，SDK中方法并无问题；建议让前后台都打印下日志，看下服务端最终使用的参数值是否还是前端获取到的原始值呢。PS：SpringBoot某些场景下form表单参数是会自动做UrlDecode的...
     * <p>
     * {@see :https://github.com/Wechat-Group/WxJava/issues/359}
     *
     * @param sessionKey    会话密钥
     * @param encryptedData 消息密文
     * @param iv            加密算法的初始向量
     * @param wxMaService   微信小程序服务
     * @return {@link WxMaPhoneNumberInfo}
     */
    private WxMaPhoneNumberInfo getPhoneNumberInfo(
            String sessionKey, String encryptedData, String iv, WxMaService wxMaService) {
        log.info("Weixin Mini App get encryptedData： {}", encryptedData);

        WxMaPhoneNumberInfo wxMaPhoneNumberInfo;
        try {

//            wxMaPhoneNumberInfo = wxMaService.getUserService().getPhoneNoInfo(sessionKey, encryptedData, iv);
            wxMaPhoneNumberInfo = new WxMaPhoneNumberInfo();
            log.debug("Weixin Mini App get phone number successfully!");
            log.debug("WxMaPhoneNumberInfo : {}", wxMaPhoneNumberInfo.toString());
            return wxMaPhoneNumberInfo;
        } catch (Exception e) {
            log.error("Weixin Mini App get phone number failed!");
            return null;
        }
    }

    private boolean checkUserInfo(String rawData, String signature) {
        return StringUtils.isNotBlank(rawData) && StringUtils.isNotBlank(signature);
    }

    public WxMaJscode2SessionResult login(String code, String appId) {
        WxMaService wxMaService = getWxMaService(appId);
        if (StringUtils.isNotBlank(code) && ObjectUtils.isNotEmpty(wxMaService)) {
            return this.getSessionInfo(code, wxMaService);
        } else {
            log.error("Weixin Mini App login failed, please check code param!");
            return null;
        }
    }

    public WxMaUserInfo getUserInfo(String appId, String sessionKey, String encryptedData, String iv) {
        return this.getUserInfo(appId, sessionKey, encryptedData, iv, null, null);
    }

    public WxMaUserInfo getUserInfo(
            String appId, String sessionKey, String encryptedData, String iv, String rawData, String signature) {

        WxMaService wxMaService = getWxMaService(appId);

        if (ObjectUtils.isNotEmpty(wxMaService)) {
            // 用户信息校验
            if (checkUserInfo(rawData, signature)) {
                if (checkUserInfo(sessionKey, rawData, signature, wxMaService)) {
                    return null;
                }
            }

            return this.getUserInfo(sessionKey, encryptedData, iv, wxMaService);
        } else {
            log.error("Weixin Mini App get user info failed!");
            return null;
        }
    }

    public WxMaPhoneNumberInfo getPhoneNumberInfo(String appId, String sessionKey, String encryptedData, String iv) {
        return this.getPhoneNumberInfo(appId, sessionKey, encryptedData, iv, null, null);
    }

    public WxMaPhoneNumberInfo getPhoneNumberInfo(
            String appId, String sessionKey, String encryptedData, String iv, String rawData, String signature) {

        WxMaService wxMaService = getWxMaService(appId);

        if (ObjectUtils.isNotEmpty(wxMaService)) {
            // 用户信息校验
            if (checkUserInfo(rawData, signature)) {
                if (checkUserInfo(sessionKey, rawData, signature, wxMaService)) {
                    return null;
                }
            }

            return this.getPhoneNumberInfo(sessionKey, encryptedData, iv, wxMaService);
        } else {
            log.error("Weixin Mini App get phone number info failed!");
            return null;
        }
    }

    /**
     * 根据直接创建的WxMaSubscribeMessage发送订阅消息
     *
     * @param appId            小程序appId
     * @param subscribeMessage 参见 {@link WxMaSubscribeMessage}
     * @return true 发送成功，false 发送失败，或者参数subscribeId配置不对，无法获取相应的WxMaSubscribeMessage
     */
    public boolean sendSubscribeMessage(String appId, WxMaSubscribeMessage subscribeMessage) {
        try {
            this.getWxMaService(appId).getMsgService().sendSubscribeMsg(subscribeMessage);
            log.debug("Send Subscribe Message Successfully!");
            return true;
        } catch (WxErrorException e) {
            log.debug("Send Subscribe Message Failed!", e);
            return false;
        }
    }

    /**
     * 检查一段文本是否含有违法违规内容。
     * 应用场景举例：
     * · 用户个人资料违规文字检测；
     * · 媒体新闻类用户发表文章，评论内容检测；
     * · 游戏类用户编辑上传的素材(如答题类小游戏用户上传的问题及答案)检测等。 频率限制：单个 appId 调用上限为 4000 次/分钟，2,000,000 次/天*
     * · 详情请见: https://developers.weixin.qq.com/miniprogram/dev/api/open-api/sec-check/msgSecCheck.html
     *
     * @param appId   小程序appId
     * @param message 需要检测的字符串
     * @return 是否违规 boolean
     */
    public boolean checkMessage(String appId, String message) {
        try {
            this.getWxMaService(appId).getSecurityService().checkMessage(message);
            log.debug("Check Message Successfully!");
            return true;
        } catch (WxErrorException e) {
            log.debug("Check Message Failed!", e);
            return false;
        }
    }

    /**
     * 校验一张图片是否含有违法违规内容
     *
     * @param appId   小程序appId
     * @param fileUrl 需要检测图片的网地址
     * @return 是否违规 boolean
     */
    public boolean checkImage(String appId, String fileUrl) {
        try {
            this.getWxMaService(appId).getSecurityService().checkImage(fileUrl);
            log.debug("Check Image Successfully!");
            return true;
        } catch (WxErrorException e) {
            log.debug("Check Image Failed! Detail is ：{}", e.getMessage());
            return false;
        }
    }

    /**
     * 校验一张图片是否含有违法违规内容.
     * <p>
     * 应用场景举例：
     * 1）图片智能鉴黄：涉及拍照的工具类应用(如美拍，识图类应用)用户拍照上传检测；电商类商品上架图片检测；媒体类用户文章里的图片检测等；
     * 2）敏感人脸识别：用户头像；媒体类用户文章里的图片检测；社交类用户上传的图片检测等。频率限制：单个 appId 调用上限为 1000 次/分钟，100,000 次/天
     * 详情请见: https://developers.weixin.qq.com/miniprogram/dev/api/open-api/sec-check/imgSecCheck.html
     *
     * @param appId 小程序appId
     * @param file  图片文件
     * @return 是否违规 boolean
     */
    public boolean checkImage(String appId, File file) {
        try {
            this.getWxMaService(appId).getSecurityService().checkImage(file);
            log.debug("Check Image Successfully!");
            return true;
        } catch (WxErrorException e) {
            log.debug("Check Image Failed! Detail is ：{}", e.getMessage());
            return false;
        }
    }

    /**
     * 异步校验图片/音频是否含有违法违规内容。
     * 应用场景举例：
     * 语音风险识别：社交类用户发表的语音内容检测；
     * 图片智能鉴黄：涉及拍照的工具类应用(如美拍，识图类应用)用户拍照上传检测；电商类商品上架图片检测；媒体类用户文章里的图片检测等；
     * 敏感人脸识别：用户头像；媒体类用户文章里的图片检测；社交类用户上传的图片检测等。
     * 频率限制：
     * 单个 appId 调用上限为 2000 次/分钟，200,000 次/天；文件大小限制：单个文件大小不超过10M
     * 详情请见:
     * https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/sec-check/security.mediaCheckAsync.html
     *
     * @param appId     小程序appId
     * @param mediaUrl  要检测的多媒体url
     * @param mediaType 媒体类型 {@link cn.binarywang.wx.miniapp.constant.WxMaConstants.SecCheckMediaType}
     * @return 微信检测结果 WxMaMediaAsyncCheckResult {@link WxMaMediaAsyncCheckResult}
     */
    public WxMaMediaAsyncCheckResult mediaAsyncCheck(String appId, String mediaUrl, int mediaType) {
        WxMaMediaAsyncCheckResult wxMaMediaAsyncCheckResult = null;
        try {
            wxMaMediaAsyncCheckResult =
                    this.getWxMaService(appId).getSecurityService().mediaCheckAsync(mediaUrl, mediaType);
            log.debug("Media Async Check Successfully!");
        } catch (WxErrorException e) {
            log.debug("Media Async Check Failed! Detail is ：{}", e.getMessage());
        }

        return wxMaMediaAsyncCheckResult;
    }
}
