package com.taotao.cloud.sensitive.sensitive.sensitive.core.support.config;

import com.taotao.cloud.common.support.deepcopy.IDeepCopy;
import com.taotao.cloud.sensitive.sensitive.sensitive.api.ISensitiveConfig;

/**
 * 默认脱敏配置实现
 */
public class DefaultSensitiveConfig implements ISensitiveConfig {

    /**
     * 深度拷贝实现
     */
    private IDeepCopy deepCopy;

    /**
     * 新建对象实例
     * @return 实例
     */
    public static DefaultSensitiveConfig newInstance() {
        return new DefaultSensitiveConfig();
    }

    public DefaultSensitiveConfig deepCopy(IDeepCopy deepCopy) {
        this.deepCopy = deepCopy;
        return this;
    }

    @Override
    public IDeepCopy deepCopy() {
        return deepCopy;
    }

}
