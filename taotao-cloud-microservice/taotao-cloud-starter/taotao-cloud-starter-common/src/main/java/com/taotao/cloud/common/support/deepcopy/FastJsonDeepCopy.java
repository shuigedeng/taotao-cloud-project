package com.taotao.cloud.common.support.deepcopy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * FastJson 深度拷贝实现
 */
public class FastJsonDeepCopy extends AbstractDeepCopy {

    /**
     * 对象单例
     * @since 0.0.1
     */
    private static final FastJsonDeepCopy INSTANCE = new FastJsonDeepCopy();

    /**
     * 获取
     * @since 0.0.1
     */
    public static FastJsonDeepCopy getInstance() {
        return INSTANCE;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T doDeepCopy(T object) {
        final Class<?> clazz = object.getClass();
        String jsonString = JSON.toJSONString(object, SerializerFeature.DisableCircularReferenceDetect);
        return (T) JSON.parseObject(jsonString, clazz);
    }

}
