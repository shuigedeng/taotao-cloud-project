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

package com.taotao.cloud.im.biz.platform.common.enums;

import lombok.Getter;

/** 返回码枚举 */
@Getter
public enum ResultCodeEnum {

    /** 操作成功 */
    SUCCESS(200, "操作成功"),
    /** 未授权 */
    UNAUTHORIZED(401, "token失效，请重新登录"),
    /** 资源/服务未找到 */
    NOT_FOUND(404, "路径不存在，请检查路径是否正确"),
    /** 操作失败 */
    FAIL(500, "系统异常，请联系管理员"),
    /** 版本号 */
    VERSION(601, "版本过低，请升级"),
    ;

    private final Integer code;
    private final String info;

    ResultCodeEnum(Integer code, String info) {
        this.code = code;
        this.info = info;
    }
}
