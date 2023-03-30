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

package com.taotao.cloud.workflow.biz.common.database.model.dto;

import java.util.List;
import java.util.Map;
import lombok.Data;

/** jdbc工具类数据传输对象 */
@Data
public class JdbcDTO<T> {

    /** 返回类型 */
    private String returnType;

    /** 单个自定义对象 */
    private Class<T> modType;

    /** 默认不开启小写模式 */
    private Boolean lowercaseFlag = false;

    /** 字段别名开关 */
    private Boolean aliasFlag = false;

    /** 数据库基础对象 */
    private DbBase dbBase;

    /** ============== 4种返回类型 ================= */
    /** 表字段信息 */
    List<List<DbFieldMod>> tableFieldMods;

    /** 结果集的map集合 */
    List<Map<String, Object>> mapMods;

    /** 包含字段信息模型集合 */
    List<List<DbFieldMod>> includeFieldMods;

    /** 自定模型集合 */
    List<T> customMods;

    /** 页面模型 */
    JdbcPageMod<T> pageMods;

    /**
     * 根据returnType 返回不同的数据
     *
     * @return
     *     <pre>
     * MAP_MOD:List<Map<String, Object>>,
     * TABLE_FIELD_MOD: List<List<DbFieldMod>>,
     * CUSTOM_MOD:List<T>,
     * INCLUDE_FIELD_MOD: List<List<DbFieldMod>>
     * </pre>
     */
    public List<?> getData() {
        switch (returnType) {
            case DbConst.MAP_MOD:
                return this.mapMods;
            case DbConst.TABLE_FIELD_MOD:
                return this.tableFieldMods;
            case DbConst.CUSTOM_MOD:
                return this.customMods;
            case DbConst.INCLUDE_FIELD_MOD:
                return this.includeFieldMods;
            default:
                return null;
        }
    }
}
