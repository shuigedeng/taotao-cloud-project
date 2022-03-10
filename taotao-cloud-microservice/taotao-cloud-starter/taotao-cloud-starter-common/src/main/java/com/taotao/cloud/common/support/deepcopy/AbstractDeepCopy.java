package com.taotao.cloud.common.support.deepcopy;


/**
 * 抽象深度拷贝实现
 */
public abstract class AbstractDeepCopy implements IDeepCopy {

    /**
     * 实现深度拷贝
     * @param object 入参对象
     * @param <T> 泛型
     * @return 结果
     * @since 0.0.1
     */
    protected abstract <T> T doDeepCopy(T object);

    @Override
    public <T> T deepCopy(T object) {
        if(null == object) {
            return null;
        }
        
        return doDeepCopy(object);
    }

}
