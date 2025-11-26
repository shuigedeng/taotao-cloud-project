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

package com.taotao.cloud.payment.biz.demo.test;

import com.alibaba.fastjson2.JSONObject;
import com.yungouos.pay.entity.WxOauthInfo;
import com.yungouos.pay.entity.WxWebLoginBiz;
import com.yungouos.pay.wxapi.WxApi;

/**
 * 微信开放API相关接口示例
 *
 * @author YunGouOS技术部-029
 */
public class WxApiTest {

    public static void main(String[] args) {
        String mch_id = "微信支付商户号";
        String key = "微信支付支付密钥";
        String callback_url = "http://www.baidu.com";
        JSONObject params = new JSONObject();
        params.put("desc", "附加数据，授权结束后可以返回");
        params.put("test", "你可以组装任何你想临时存储的数据");

        /**
         * 授权类型 mp-base：基础授权，不会有授权页面，用户无感知，可获取openid。
         *
         * <p>mp-info：详细授权，首次授权会弹出授权页面，可获取用户昵称、头像等信息。
         *
         * <p>open-url：微信PC端扫码登录url
         */
        String type = "mp-base";
        String oauthUrl = WxApi.getWxOauthUrl(mch_id, callback_url, type, params, key);
        LogUtils.info(oauthUrl);

        String code = "9F6501CA055545259E20D2301EB3AFD9";
        WxOauthInfo wxOauthInfo = WxApi.getWxOauthInfo(mch_id, code, key);
        LogUtils.info(wxOauthInfo);

        WxWebLoginBiz wxWebLoginBiz = WxApi.getWebLogin(mch_id, callback_url, params, key);

        LogUtils.info(wxWebLoginBiz.toString());
    }
}
