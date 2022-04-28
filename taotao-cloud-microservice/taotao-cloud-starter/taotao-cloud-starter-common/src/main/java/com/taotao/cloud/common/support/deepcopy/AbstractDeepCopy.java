package com.taotao.cloud.common.support.deepcopy;


/**
 * 抽象深度拷贝实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:08:19
 */
public abstract class AbstractDeepCopy implements IDeepCopy {

    /**
     * 实现深度拷贝
     * @param object 入参对象
     * @param <T> 泛型
     * @return 结果
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
