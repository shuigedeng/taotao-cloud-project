/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.common.enums;

/**
 * 用户性别类型
 *
 * @author dengtao
 * @date 2020/4/30 10:25
 * @since v1.0
 */
public enum UserSexTypeEnum implements BaseEnum {
    /**
     * 未知
     */
    UNKNOWN((byte) 0, "未知"),
    /**
     * 男
     */
    MALE((byte) 1, "男"),
    /**
     * 女
     */
    FEMALE((byte) 2, "女");

    private byte code;
    private String description;

    UserSexTypeEnum(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public void setCode(Byte code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getNameByCode(int code) {
        for (UserSexTypeEnum result : UserSexTypeEnum.values()) {
            if (result.getCode() == code) {
                return result.name().toLowerCase();
            }
        }
        return null;
    }

    @Override
    public Integer getCode() {
        return (int) code;
    }
}
