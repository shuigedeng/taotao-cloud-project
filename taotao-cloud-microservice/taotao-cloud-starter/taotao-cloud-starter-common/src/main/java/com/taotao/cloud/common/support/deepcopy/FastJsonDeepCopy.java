package com.taotao.cloud.common.support.deepcopy;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;

/**
 * FastJson 深度拷贝实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:08:11
 */
public class FastJsonDeepCopy extends AbstractDeepCopy {

    /**
     * 对象单例
     */
    private static final FastJsonDeepCopy INSTANCE = new FastJsonDeepCopy();

    /**
     * 获取
     */
    public static FastJsonDeepCopy getInstance() {
        return INSTANCE;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T doDeepCopy(T object) {
        final Class<?> clazz = object.getClass();
        String jsonString = JSON.toJSONString(object, JSONWriter.Feature.ReferenceDetection);
        return (T) JSON.parseObject(jsonString, clazz);
    }

}
