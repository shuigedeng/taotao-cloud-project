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

package com.taotao.cloud.workflow.biz.common.util.enums;

/** 功能分类枚举 */
public enum ModuleTypeEnum {
    /** 数据接口类型 */
    SYSTEM_DATAINTEFASE("bd"),
    /** 单据规则 */
    SYSTEM_BILLRULE("bb"),
    /** 菜单 */
    SYSTEM_MODULE("bm"),
    /** 数据建模 */
    SYSTEM_DBTABLE("bdb"),
    /** 数据字典 */
    SYSTEM_DICTIONARYDATA("bdd"),
    /** 打印模板 */
    SYSTEM_PRINT("bp"),
    /** 大屏导出 */
    VISUAL_DATA("vd"),
    /** 在线开发 */
    VISUAL_DEV("vdd"),
    /** APP导出 */
    VISUAL_APP("va"),
    /** 门户导出 */
    VISUAL_PORTAL("vp"),
    /** 流程设计 */
    FLOW_FLOWENGINE("ffe"),
    ;

    ModuleTypeEnum(String moduleName) {
        this.tableName = moduleName;
    }

    private String tableName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
