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

package com.taotao.cloud.auth.infrastructure.extension.qrcocde.tmp.entity;

public enum QrCodeStatusEnum {
    WAITING(0, "待扫描"),

    SCANNED(1, "待确认"),

    CONFIRMED(2, "已确认"),

    INVALID(-1, "二维码已失效");

    private final Integer status;

    private final String message;

    QrCodeStatusEnum(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public static QrCodeStatusEnum parse(Integer status) {
        for (QrCodeStatusEnum entityTypeEnum : QrCodeStatusEnum.values()) {
            if (entityTypeEnum.status.equals(status)) {
                return entityTypeEnum;
            }
        }
        return INVALID;
    }
}
