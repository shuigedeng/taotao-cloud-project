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

import com.google.common.collect.Lists;
import com.taotao.cloud.recommend.dto.RelateDTO;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;

/**
 * 核心算法
 */
public class CoreMath {

    /**
     * 计算相关系数并排序
     * @param key
     * @param map
     * @return Map<Integer,Double>
     */
    public static Map<Integer, Double> computeNeighbor(Integer key, Map<Integer, List<RelateDTO>> map, int type) {
        Map<Integer, Double> distMap = new TreeMap<>();
        List<RelateDTO> userItems = map.get(key);
        map.forEach((k, v) -> {
            // 排除此用户
            if (!k.equals(key)) {
                // 关系系数
                double coefficient = relateDist(v, userItems, type);
                // 关系距离
                double distance = Math.abs(coefficient);
                distMap.put(k, distance);
            }
        });
        return distMap;
    }

    /**
     * 计算两个序列间的相关系数
     *
     * @param xList
     * @param yList
     * @param type 类型0基于用户推荐 1基于物品推荐
     * @return double
     */
    private static double relateDist(List<RelateDTO> xList, List<RelateDTO> yList, int type) {
        List<Integer> xs = Lists.newArrayList();
        List<Integer> ys = Lists.newArrayList();
        xList.forEach(x -> {
            yList.forEach(y -> {
                if (type == 0) {
                    if (x.getItemId().equals(y.getItemId())) {
                        xs.add(x.getIndex());
                        ys.add(y.getIndex());
                    }
                } else {
                    if (x.getUseId().equals(y.getUseId())) {
                        xs.add(x.getIndex());
                        ys.add(y.getIndex());
                    }
                }
            });
        });
        return getRelate(xs, ys);
    }

    /**
     * 方法描述: 皮尔森（pearson）相关系数计算
     *
     * @param xs x集合
     * @param ys y集合
     * @Return {@link double}
     *
     * @since 2020年07月31日 17:03:20
     */
    public static double getRelate(List<Integer> xs, List<Integer> ys) {
        int n = xs.size();
        // 至少有两个元素
        if (n < 2) {
            return 0D;
        }
        double ex = xs.stream().mapToDouble(x -> x).sum();
        double ey = ys.stream().mapToDouble(y -> y).sum();
        double ex2 = xs.stream().mapToDouble(x -> Math.pow(x, 2)).sum();
        double ey2 = ys.stream().mapToDouble(y -> Math.pow(y, 2)).sum();
        double exy =
                IntStream.range(0, n).mapToDouble(i -> xs.get(i) * ys.get(i)).sum();

        double numerator = exy - ex * ey / n;
        double denominator = Math.sqrt((ex2 - Math.pow(ex, 2) / n) * (ey2 - Math.pow(ey, 2) / n));
        if (denominator == 0) {
            return 0D;
        }
        return numerator / denominator;
    }
}
