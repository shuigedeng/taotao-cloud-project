package com.taotao.cloud.core.heaven.support.cache.impl;


import com.taotao.cloud.core.heaven.util.lang.reflect.ClassUtil;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 类字段列表缓存
 *
 * <p> project: heaven-ClassFieldListCache </p>
 * <p> create on 2019/12/16 23:22 </p>
 *
 * @author Administrator
 * @since 0.1.61
 */
public class ClassFieldListCache extends AbstractCache<Class, List<Field>> {

    /**
     * 新建单例
     * @since 0.1.63
     */
    private static final ClassFieldListCache INSTANCE = new ClassFieldListCache();

    /**
     * 私有化构造器
     * @since 0.1.63
     */
    private ClassFieldListCache(){}

    /**
     * 获取单例
     * @return 单例
     * @since 0.1.63
     */
    public static ClassFieldListCache getInstance() {
        return INSTANCE;
    }

    @Override
    protected List<Field> buildValue(Class key) {
        return ClassUtil.getAllFieldList(key);
    }

}
