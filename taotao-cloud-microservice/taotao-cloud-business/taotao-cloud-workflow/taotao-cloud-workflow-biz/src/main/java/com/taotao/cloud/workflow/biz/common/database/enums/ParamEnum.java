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

package com.taotao.cloud.workflow.biz.common.database.enums;

/** 数据库 结构、参数 替换枚举 structure */
public enum ParamEnum {

    /** 数据库 */
    DB_URL("{dbUrl}"),
    /** 数据库名 */
    DB_NAME("{dbName}"),
    /** 用户名 */
    USER_NAME("{userName}"),
    /** 模式 schema关键字,加前缀 */
    DB_SCHEMA("{dbSchema}"),
    /** 表空间 */
    TABLE_SPACE("{tableSpace}"),
    /** 表 */
    TABLE("{table}"),
    /** 替换符 */
    SPLIT("split"),
    /** 字段名 */
    FILED("{filed}"),
    /** 字段长度 */
    DATA_LENGTH("{dataLength}"),
    /** 字段注释 */
    COMMENT("{comment}");

    /** 替换目标 */
    private final String target;

    public String getTarget() {
        return this.target;
    }

    public String getParamSign() {
        return "?";
    }

    ParamEnum(String target) {
        this.target = target;
    }
}
