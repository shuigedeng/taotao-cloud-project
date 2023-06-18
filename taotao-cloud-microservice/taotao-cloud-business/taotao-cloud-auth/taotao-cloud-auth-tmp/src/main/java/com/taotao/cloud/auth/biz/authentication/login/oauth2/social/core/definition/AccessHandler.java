/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.authentication.login.oauth2.social.core.definition;


import com.taotao.cloud.security.springsecurity.core.definition.domain.AccessPrincipal;

/**
 * <p>Description: 外部应用接入处理器 </p>
 *
 * 
 * @date : 2022/1/25 16:20
 */
public interface AccessHandler {

    /**
     * 外部应用接入预处理
     * 比如 微信小程序需要传入Code 和 AppId
     * 比如 手机登录需要传入手机号码等
     *
     * @param core   对于只需要一个参数就可以进行预处理操作的核心值。
     * @param params 核心值以外的其它参数
     * @return {@link  AccessResponse}
     */
    AccessResponse preProcess(String core, String... params);

    /**
     * 获取接入系统中的用户信息，并转换为系统可以识别的 {@link AccessUserDetails} 类型
     *
     * @param source          类别
     * @param accessPrincipal 外部系统接入所需信息
     * @return 外部系统用户信息 {@link AccessUserDetails}
     */
    AccessUserDetails loadUserDetails(String source, AccessPrincipal accessPrincipal);
}
