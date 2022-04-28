package com.taotao.cloud.common.support.cache.impl;


import com.taotao.cloud.common.utils.reflect.ClassUtil;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 类字段列表缓存
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:07:41
 */
public class ClassFieldListCache extends AbstractCache<Class, List<Field>> {

    /**
     * 新建单例
     */
    private static final ClassFieldListCache INSTANCE = new ClassFieldListCache();

    /**
     * 私有化构造器
     */
    private ClassFieldListCache(){}

    /**
     * 获取单例
     * @return 单例
     */
    public static ClassFieldListCache getInstance() {
        return INSTANCE;
    }

    @Override
    protected List<Field> buildValue(Class key) {
        return ClassUtil.getAllFieldList(key);
    }

}
