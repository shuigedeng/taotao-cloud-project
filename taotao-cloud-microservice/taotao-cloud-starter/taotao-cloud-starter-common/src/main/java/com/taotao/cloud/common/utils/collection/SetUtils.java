package com.taotao.cloud.common.utils.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Set 工具类
 */
public final class SetUtils {

    private SetUtils(){}

    /**
     * 获取第一个元素
     * @param set set 集合
     * @param <T> 泛型
     * @return 结果
     */
    public static <T> T getFirst(final Set<T> set) {
        if(CollectionUtils.isEmpty(set)) {
            return null;
        }

        List<T> list = new ArrayList<>(set);
        return list.get(0);
    }

}
