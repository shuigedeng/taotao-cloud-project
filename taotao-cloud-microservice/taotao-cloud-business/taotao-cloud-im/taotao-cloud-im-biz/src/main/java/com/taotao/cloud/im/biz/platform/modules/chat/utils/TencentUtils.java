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

package com.taotao.cloud.im.biz.platform.modules.chat.utils;

import com.platform.common.exception.BaseException;
import com.platform.modules.chat.config.TencentConfig;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.nlp.v20190408.NlpClient;
import com.tencentcloudapi.nlp.v20190408.models.ChatBotRequest;
import com.tencentcloudapi.nlp.v20190408.models.ChatBotResponse;
import com.tencentcloudapi.tmt.v20180321.TmtClient;
import com.tencentcloudapi.tmt.v20180321.models.SpeechTranslateRequest;
import com.tencentcloudapi.tmt.v20180321.models.SpeechTranslateResponse;
import com.tencentcloudapi.tmt.v20180321.models.TextTranslateRequest;
import com.tencentcloudapi.tmt.v20180321.models.TextTranslateResponse;

/** 腾讯工具类 */
public class TencentUtils {

    /** 腾讯翻译 */
    public static String translation(TencentConfig tencentConfig, String content) {
        String target = "zh";
        String source = "auto";
        if (ReUtil.contains("[\\u4e00-\\u9fa5]", content)) {
            target = "en";
            source = "zh";
        }
        Credential cred = new Credential(tencentConfig.getAppKey(), tencentConfig.getAppSecret());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("tmt.tencentcloudapi.com");
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        TmtClient client = new TmtClient(cred, "ap-beijing", clientProfile);
        // 实例化一个请求对象,每个接口都会对应一个request对象
        TextTranslateRequest req = new TextTranslateRequest();
        req.setSourceText(content);
        req.setSource(source);
        req.setTarget(target);
        req.setProjectId(0L);
        TextTranslateResponse resp;
        try {
            resp = client.TextTranslate(req);
        } catch (TencentCloudSDKException e) {
            throw new BaseException("翻译机器人接口调用异常，请稍后再试");
        }
        StringBuilder builder = new StringBuilder();
        builder.append("翻译结果：");
        builder.append("\n");
        builder.append("原文：");
        builder.append(content);
        builder.append("\n");
        builder.append("译文：");
        builder.append(resp.getTargetText());
        return builder.toString();
    }

    /** 图灵机器人调用 */
    public static String turing(TencentConfig tencentConfig, Long userId, String content) {
        try {
            Credential cred = new Credential(tencentConfig.getAppKey(), tencentConfig.getAppSecret());
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("nlp.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            NlpClient client = new NlpClient(cred, "ap-guangzhou", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            ChatBotRequest req = new ChatBotRequest();
            req.setOpenId(NumberUtil.toStr(userId));
            req.setQuery(content);
            // 返回的resp是一个ChatBotResponse的实例，与请求对象对应
            ChatBotResponse resp = client.ChatBot(req);
            // 输出json格式的字符串回包
            return resp.getReply();
        } catch (Exception e) {
            throw new BaseException("图灵机器人接口调用异常，请稍后再试");
        }
    }

    /** 语音识别 */
    public static String audio2Text(TencentConfig tencentConfig, String content) {
        try {
            Credential cred = new Credential(tencentConfig.getAppKey(), tencentConfig.getAppSecret());
            ClientProfile clientProfile = new ClientProfile();
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("tmt.tencentcloudapi.com");
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            TmtClient client = new TmtClient(cred, "ap-beijing", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            SpeechTranslateRequest req = new SpeechTranslateRequest();
            req.setSessionUuid("1");
            req.setSource("zh");
            req.setTarget("zh");
            req.setAudioFormat(83886080L);
            req.setSeq(0L);
            req.setIsEnd(1L);
            req.setData(content);
            SpeechTranslateResponse resp = client.SpeechTranslate(req);
            return resp.getSourceText();
        } catch (Exception e) {
            throw new BaseException("语音识别接口调用异常，请稍后再试");
        }
    }
}
