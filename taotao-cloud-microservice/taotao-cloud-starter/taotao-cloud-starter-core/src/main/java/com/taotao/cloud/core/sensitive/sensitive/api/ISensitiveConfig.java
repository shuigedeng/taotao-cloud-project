package com.taotao.cloud.core.sensitive.sensitive.api;


import com.taotao.cloud.common.support.deepcopy.IDeepCopy;

/**
 * 脱敏配置接口
 */
public interface ISensitiveConfig {

    /**
     * 深度拷贝
     * @return 深度拷贝
     * @since 0.0.9
     */
    IDeepCopy deepCopy();

}

