package com.taotao.cloud.core.heaven.util.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Set 工具类
 * <p> project: heaven-SetUtil </p>
 * <p> create on 2019/11/27 21:42 </p>
 *
 * @author Administrator
 * @since 0.1.51
 */
public final class SetUtil {

    private SetUtil(){}

    /**
     * 获取第一个元素
     * @param set set 集合
     * @param <T> 泛型
     * @return 结果
     * @since 0.1.51
     */
    public static <T> T getFirst(final Set<T> set) {
        if(CollectionUtil.isEmpty(set)) {
            return null;
        }

        List<T> list = new ArrayList<>(set);
        return list.get(0);
    }

}
