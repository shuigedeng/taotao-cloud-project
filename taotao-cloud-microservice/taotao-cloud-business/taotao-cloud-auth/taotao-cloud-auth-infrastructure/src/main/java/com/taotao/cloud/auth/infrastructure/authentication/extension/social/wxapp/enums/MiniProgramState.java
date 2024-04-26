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

package com.taotao.cloud.auth.infrastructure.authentication.extension.social.wxapp.enums;

/**
 * <p>跳转小程序类型 </p>
 * <p>
 * developer为开发版；trial为体验版；formal为正式版；默认为正式版
 *
 *
 * @since : 2021/4/9 16:09
 */
public enum MiniProgramState {

    /**
     * 开发版
     */
    developer,

    /**
     * 体验版
     */
    trial,

    /**
     * 正式版
     */
    formal;
}
