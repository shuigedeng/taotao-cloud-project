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

package com.taotao.cloud.auth.infrastructure.extension.qrcocde.tmp1;

/**
 * 二维码工具类
 */
public interface CodeUtil {
    /**
     * 获取过期二维码存储信息
     *
     * @return 二维码值对象
     */
    static CodeData getExpireCodeInfo() {
        return new CodeData(CodeStatusEnum.EXPIRE, "二维码已更新");
    }

    /**
     * 获取未使用二维码存储信息
     *
     * @return 二维码值对象
     */
    static CodeData getUnusedCodeInfo() {
        return new CodeData(CodeStatusEnum.UNUSED, "二维码等待扫描");
    }

    /**
     * 获取已扫码二维码存储信息
     */
    static CodeData getConfirmingCodeInfo() {
        return new CodeData(CodeStatusEnum.CONFIRMING, "二维码扫描成功，等待确认");
    }

    /**
     * 获取已扫码确认二维码存储信息
     *
     * @return 二维码值对象
     */
    static CodeData getConfirmedCodeInfo(String token) {
        return new CodeData(CodeStatusEnum.CONFIRMED, "二维码已确认", token);
    }
}
