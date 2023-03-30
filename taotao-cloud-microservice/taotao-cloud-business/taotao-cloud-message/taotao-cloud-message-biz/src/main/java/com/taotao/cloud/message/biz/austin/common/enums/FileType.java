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

package com.taotao.cloud.message.biz.austin.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 文件类型
 *
 * @author 3y
 */
@Getter
@ToString
@AllArgsConstructor
public enum FileType {
    /** 图片 */
    IMAGE("10", "image"),
    /** 语音 */
    VOICE("20", "voice"),
    /** 普通文件 */
    COMMON_FILE("30", "file"),
    /** 视频 */
    VIDEO("40", "video"),
    ;
    private final String code;
    private final String name;

    public static String getNameByCode(String code) {
        for (FileType fileType : FileType.values()) {
            if (fileType.getCode().equals(code)) {
                return fileType.getName();
            }
        }
        return null;
    }
}
