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

package com.taotao.cloud.goods.infrastructure.util;

import cn.hutool.core.util.ReflectUtil;
import com.taotao.cloud.goods.biz.elasticsearch.entity.EsGoodsIndex;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * es指数跑龙套
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:03:25
 */
public class EsIndexUtil {

    /** 忽视领域 */
    private static final String IGNORE_FIELD = "serialVersionUID,promotionMap,id,goodsId";

    /**
     * 更新索引字段映射
     *
     * @param queryGoodsIndex 查询商品指数
     * @param updateGoodsIndex 更新商品指数
     * @return {@link Map }<{@link String }, {@link Object }>
     * @since 2022-04-27 17:03:25
     */
    public static Map<String, Object> getUpdateIndexFieldsMap(
            EsGoodsIndex queryGoodsIndex, EsGoodsIndex updateGoodsIndex) {
        Map<String, Object> queryFieldsMap = new HashMap<>();
        Map<String, Object> updateFieldsMap = new HashMap<>();

        for (Map.Entry<String, Field> entry :
                ReflectUtil.getFieldMap(EsGoodsIndex.class).entrySet()) {
            Object queryFieldValue = ReflectUtil.getFieldValue(queryGoodsIndex, entry.getValue());
            Object updateFieldValue = ReflectUtil.getFieldValue(updateGoodsIndex, entry.getValue());
            if (queryFieldValue != null && !IGNORE_FIELD.contains(entry.getKey())) {
                ReflectUtil.setFieldValue(queryFieldsMap, entry.getValue(), queryFieldValue);
            }
            if (updateFieldValue != null && !IGNORE_FIELD.contains(entry.getKey())) {
                ReflectUtil.setFieldValue(updateFieldsMap, entry.getValue(), updateFieldValue);
            }
        }

        return getUpdateIndexFieldsMap(queryFieldsMap, updateFieldsMap);
    }

    /**
     * 更新索引字段映射
     *
     * @param queryFieldsMap 查询字段映射
     * @param updateFieldsMap 更新字段映射
     * @return {@link Map }<{@link String }, {@link Object }>
     * @since 2022-04-27 17:03:25
     */
    public static Map<String, Object> getUpdateIndexFieldsMap(
            Map<String, Object> queryFieldsMap, Map<String, Object> updateFieldsMap) {
        Map<String, Object> updateIndexMap = new HashMap<>();

        updateIndexMap.put("queryFields", queryFieldsMap);
        updateIndexMap.put("updateFields", updateFieldsMap);
        return updateIndexMap;
    }
}
