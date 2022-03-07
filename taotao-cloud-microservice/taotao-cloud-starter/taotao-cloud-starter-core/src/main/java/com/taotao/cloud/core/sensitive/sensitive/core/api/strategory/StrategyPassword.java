package com.taotao.cloud.core.sensitive.sensitive.core.api.strategory;

import com.taotao.cloud.core.heaven.util.lang.ObjectUtil;
import com.taotao.cloud.core.sensitive.sensitive.api.IContext;
import com.taotao.cloud.core.sensitive.sensitive.api.IStrategy;
import com.taotao.cloud.core.sensitive.sensitive.core.util.strategy.SensitiveStrategyUtil;

/**
 * 密码的脱敏策略：
 * 1. 直接返回 null
 */
public class StrategyPassword implements IStrategy {

    @Override
    public Object des(Object original, IContext context) {
        return SensitiveStrategyUtil.password(ObjectUtil.objectToString(original));
    }

}
