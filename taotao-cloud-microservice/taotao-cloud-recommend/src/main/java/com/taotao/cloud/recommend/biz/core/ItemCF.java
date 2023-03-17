package com.taotao.cloud.recommend.biz.core;


import com.taotao.cloud.recommend.biz.dto.RelateDTO;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 核心算法
 *
 * 
 * @version 1.0
 * @date 2020/7/31$ 15:21$
 * @since JDK1.8
 */
public class ItemCF {

    /**
     * 方法描述: 推荐电影id列表
     *
     * @param itemId 当前电影id
     * @param list 用户电影评分数据
     * @return {@link List<Integer>}
     * @date 2023年02月02日 14:51:42
     */
    public static List<Integer> recommend(Integer itemId, List<RelateDTO> list) {
        //按物品分组
        Map<Integer, List<RelateDTO>>  itemMap=list.stream().collect(Collectors.groupingBy(RelateDTO::getItemId));
        //获取其他物品与当前物品的关系值
        Map<Integer,Double>  itemDisMap = CoreMath.computeNeighbor(itemId, itemMap,1);
        //获取关系最近物品
        double maxValue=Collections.max(itemDisMap.values());
        return itemDisMap.entrySet().stream().filter(e->e.getValue()==maxValue).map(Map.Entry::getKey).collect(Collectors.toList());
    }


}
