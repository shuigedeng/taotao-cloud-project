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
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 核心算法
 */
public class UserCF {

    /**
     * 方法描述: 推荐电影id列表
     *
     * @param userId 当前用户
     * @param list 用户电影评分数据
     * @return {@link List<Integer>}
     */
    public static List<Integer> recommend(Integer userId, List<RelateDTO> list) {
        // 按用户分组
        Map<Integer, List<RelateDTO>> userMap = list.stream().collect(Collectors.groupingBy(
						RelateDTO::getUseId));
        // 获取其他用户与当前用户的关系值
        Map<Integer, Double> userDisMap = CoreMath.computeNeighbor(userId, userMap, 0);
        // 获取关系最近的用户
        double maxValue = Collections.max(userDisMap.values());
        Set<Integer> userIds = userDisMap.entrySet().stream()
                .filter(e -> e.getValue() == maxValue)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        // 取关系最近的用户
        Integer nearestUserId = userIds.stream().findAny().orElse(null);
        if (nearestUserId == null) {
            return Collections.emptyList();
        }
        // 最近邻用户看过电影列表
        List<Integer> neighborItems =
                userMap.get(nearestUserId).stream().map(RelateDTO::getItemId).toList();
        // 指定用户看过电影列表
        List<Integer> userItems =
                userMap.get(userId).stream().map(RelateDTO::getItemId).toList();
        // 找到最近邻看过，但是该用户没看过的电影
        neighborItems.removeAll(userItems);
        return neighborItems;
    }
}
