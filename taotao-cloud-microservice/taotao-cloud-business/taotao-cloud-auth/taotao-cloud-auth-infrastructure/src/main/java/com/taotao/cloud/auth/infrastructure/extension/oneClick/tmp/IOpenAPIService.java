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

package com.taotao.cloud.auth.infrastructure.extension.oneClick.tmp;

import com.aliyun.dypnsapi20170525.models.GetMobileResponse;
import com.aliyun.dypnsapi20170525.models.VerifyMobileResponse;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 开放API接口 服务类
 *
 * <p>app实现阿里云手机号一键登录最佳实践
 *
 * <p>https://juejin.cn/post/7081945118202134542
 */
public interface IOpenAPIService {

    /**
     * 调用GetMobile完成一键登录取号
     *
     * @param accessToken APP端SDK获取的登录token，必填
     * @param outId 外部流水号，非必填
     */
    GetMobileResponse getMobile(String accessToken, String outId, HttpServletRequest request);

    /**
     * 调用verifyMobile完成本机号码校验认证
     *
     * @param accessCode APP端SDK获取的登录token，必填
     * @param phoneNumber 手机号，必填
     * @param outId 外部流水号，非必填
     */
    VerifyMobileResponse verifyMobile(String accessCode, String phoneNumber, String outId, HttpServletRequest request);

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数键名
     * @return configValue 参数键值
     */
    String selectConfigValueByKey(String configKey, HttpServletRequest request);
}
