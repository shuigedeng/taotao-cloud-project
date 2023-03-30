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

/** 数据字典分类id */
public enum DictionaryDataEnum {
    /** 功能设计 */
    VISUALDEV(1, "765929a127f44a5b80e773d65d58f96c"),
    /** 移动设计 */
    VISUALDEV_APP(2, "573c31998bc04a23a769f9a9eff67d00"),
    /** 流程表单 */
    VISUALDEV_GENERATER_FLOWWORK(3, "037ba904515348eaad1c4bd462fc80a6"),
    /** 功能表单 */
    VISUALDEV_GENERATER(4, "4173579c31e74a2b8749e65a23e5c957"),
    /** 移动表单 */
    VISUALDEV_GENERATER_APP(5, "1cff2b60fd7a4c9f82273163b956268c"),
    /** 门户设计 */
    VISUALDEV_PORTAL(6, "02ad722fd1914c338d51597236ad2339"),
    /** 数据连接 */
    SYSTEM_DBLINK(7, "9e7b2d0c690f4562b13e1215e449c222"),
    /** 打印模板 */
    SYSTEM_PRINTDEV(8, "202931027482510597"),
    /** 数据接口 */
    SYSTEM_DATAINTERFACE(12, "9c43287481364d348c0ea0d0f64b38be"),
    /** 角色类型 */
    PERMISSION_ROLE(9, "4501f6f26a384757bce12d4c4b03342c"),
    /** 分组id */
    PERMISSION_GROUP(12, "271905527003350725"),
    /** 报表设计 */
    VISUALDEV_REPORT(10, "65e7917344fa460e8c487e45bbbab26f"),
    /** 流程设计 */
    FLOWWOEK_ENGINE(11, "507f4f5df86b47588138f321e0b0dac7");

    private Integer type;
    private String dictionaryTypeId;

    DictionaryDataEnum(Integer type, String dictionaryTypeId) {
        this.type = type;
        this.dictionaryTypeId = dictionaryTypeId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDictionaryTypeId() {
        return dictionaryTypeId;
    }

    public void setDictionaryTypeId(String dictionaryTypeId) {
        this.dictionaryTypeId = dictionaryTypeId;
    }

    /**
     * 获取通过type获取数据字典分类id
     *
     * @param type 类型
     * @return
     */
    public static String getTypeId(Integer type) {
        for (DictionaryDataEnum value : DictionaryDataEnum.values()) {
            if (type.equals(value.getType())) {
                return value.getDictionaryTypeId();
            }
        }
        return "";
    }
}
