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

package com.taotao.cloud.recommend.core;

import com.taotao.cloud.recommend.dto.RelateDTO;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 核心算法
 *
 *
 * @version 1.0
 * @since 2020/7/31$ 15:21$
 * @since JDK1.8
 */
public class ItemCF {

    /**
     * 方法描述: 推荐电影id列表
     *
     * @param itemId 当前电影id
     * @param list 用户电影评分数据
     * @return {@link List<Integer>}
     * @since 2023年02月02日 14:51:42
     */
    public static List<Integer> recommend(Integer itemId, List<RelateDTO> list) {
        // 按物品分组
        Map<Integer, List<RelateDTO>> itemMap = list.stream().collect(Collectors.groupingBy(
						RelateDTO::getItemId));
        // 获取其他物品与当前物品的关系值
        Map<Integer, Double> itemDisMap = CoreMath.computeNeighbor(itemId, itemMap, 1);
        // 获取关系最近物品
        double maxValue = Collections.max(itemDisMap.values());
        return itemDisMap.entrySet().stream()
                .filter(e -> e.getValue() == maxValue)
                .map(Map.Entry::getKey)
                .toList();
    }
}
