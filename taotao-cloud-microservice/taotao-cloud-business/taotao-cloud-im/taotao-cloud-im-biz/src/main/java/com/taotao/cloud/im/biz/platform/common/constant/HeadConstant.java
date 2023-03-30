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

package com.taotao.cloud.im.biz.platform.common.constant;

/** 头部常量 */
public class HeadConstant {

    /** 令牌 */
    public static final String TOKEN_KEY = "Authorization";

    /** 版本号 */
    public static final String VERSION = "version";

    /** 设备类型 */
    public static final String DEVICE = "device";

    /** 设备 */
    private static final String DEVICE_PREFIX = DEVICE + "=";

    /** 设备android */
    public static final String ANDROID = DEVICE_PREFIX + "android";

    /** 设备ios */
    public static final String IOS = DEVICE_PREFIX + "ios";
}
