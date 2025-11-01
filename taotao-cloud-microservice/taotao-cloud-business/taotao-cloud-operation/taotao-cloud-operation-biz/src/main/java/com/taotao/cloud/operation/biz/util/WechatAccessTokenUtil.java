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

package com.taotao.cloud.operation.biz.util; // package com.taotao.cloud.message.biz.util;
//
// import com.google.gson.Gson;
// 
// import com.taotao.boot.common.enums.ClientTypeEnum;
// import com.taotao.boot.common.enums.ResultEnum;
// import com.taotao.boot.common.exception.BusinessException;
// import com.taotao.cloud.redis.repository.RedisRepository;
// import com.taotao.cloud.sys.api.enums.SettingCategoryEnum;
// import com.taotao.cloud.sys.api.setting.connect.WechatConnectSetting;
// import com.taotao.cloud.sys.api.setting.connect.dto.WechatConnectSettingItem;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;
//
// /**
//  * 微信API交互token
//  */
//
// @Component
// public class WechatAccessTokenUtil {
//     @Autowired
//     private RedisRepository redisRepository;
//     @Autowired
//     private SettingService settingService;
//
//     /**
//      * 获取某一平台等cgi token 用于业务调用，例如发送公众号消息
//      *
//      * @param clientTypeEnum h5 公众号 / wechatMP 微信小程序
//      */
//     public String cgiAccessToken(ClientTypeEnum clientTypeEnum) {
//         //h5 和MP 才有获取token的能力
//         if (clientTypeEnum.equals(ClientTypeEnum.H5) ||
// clientTypeEnum.equals(ClientTypeEnum.WECHAT_MP)) {
//
//             //缓存一下token
//             String token = cache.getString(CachePrefix.WECHAT_CGI_ACCESS_TOKEN.getPrefix() +
// clientTypeEnum.name());
//             if (token != null) {
//                 return token;
//             }
//             //获取微信配置
//             Setting setting = settingService.get(SettingCategoryEnum.WECHAT_CONNECT.name());
//             if (setting == null) {
//                 log.error("获取token客户端异常" + clientTypeEnum.name() +
// ",客户端未配置微信参数，请前往后台=》联合登陆，进行对应微信配置");
//                 return null;
//             }
//             //获取配置，获取对应的配置
//             WechatConnectSetting wechatConnectSetting = new
// Gson().fromJson(setting.getSettingValue(), WechatConnectSetting.class);
//             WechatConnectSettingItem item = null;
//             for (WechatConnectSettingItem wechatConnectSettingItem :
// wechatConnectSetting.getWechatConnectSettingItems()) {
//                 if (wechatConnectSettingItem.getClientType().equals(clientTypeEnum.name())) {
//                     item = wechatConnectSettingItem;
//                 }
//             }
//             //微信h5配置与否
//             if (item == null) {
//                 return null;
//             }
//             //获取token
//             String content =
// HttpUtil.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential" +
//                     "&appid=" + item.getAppId() + "&secret=" + item.getAppSecret());
//
//             JSONObject object = new JSONObject(content);
//             log.info("token获取【" + clientTypeEnum.name() + "】返回" + object.toString());
//             String accessToken = object.get("access_token").toString();
//             cache.put(CachePrefix.WECHAT_CGI_ACCESS_TOKEN.getPrefix() + clientTypeEnum.name(),
//                     object.getStr("access_token"), object.getLong("expires_in"));
//             return accessToken;
//         } else {
//             log.error("获取token客户端异常" + clientTypeEnum.name());
//             return null;
//         }
//     }
//
//     /**
//      * 获取某一平台等cgi token 用于业务调用，例如发送公众号消息
//      *
//      * @param clientTypeEnum
//      * @return
//      */
//     public String cgiJsApiTicket(ClientTypeEnum clientTypeEnum) {
//         //缓存一下token
//         String token = cache.getString(CachePrefix.WECHAT_JS_API_TOKEN.getPrefix() +
// clientTypeEnum.name());
//         if (token != null) {
//             return token;
//         }
//         String accessToken = this.cgiAccessToken(clientTypeEnum);
//         try {
//             String content = new
// HttpUtils().get("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + accessToken
// + "&type=jsapi");
//
//             JSONObject object = new JSONObject(content);
//             String ticket = object.getStr("ticket");
//             Long expires = object.getLong("expires_in");
//             cache.put(CachePrefix.WECHAT_JS_API_TOKEN.getPrefix() + clientTypeEnum.name(),
// ticket, expires);
//             return ticket;
//         } catch (Exception e) {
//             log.error("微信JsApi签名异常", e);
//             throw new BusinessException(ResultEnum.WECHAT_JSAPI_SIGN_ERROR);
//         }
//
//     }
//
//     /**
//      * 清除 token
//      * @param clientTypeEnum
//      */
//     public void removeAccessToken(ClientTypeEnum clientTypeEnum) {
//         cache.remove(CachePrefix.WECHAT_CGI_ACCESS_TOKEN.getPrefix() + clientTypeEnum.name());
//     }
//
// }
