package com.taotao.cloud.core.sensitive.sensitive.core.api.strategory;

import com.taotao.cloud.core.heaven.util.lang.ObjectUtil;
import com.taotao.cloud.core.sensitive.sensitive.api.IContext;
import com.taotao.cloud.core.sensitive.sensitive.api.IStrategy;
import com.taotao.cloud.core.sensitive.sensitive.core.util.strategy.SensitiveStrategyUtil;

/**
 * 手机号脱敏
 * 脱敏规则：139****6631
 */
public class StrategyPhone implements IStrategy {

    @Override
    public Object des(Object original, IContext context) {
        return SensitiveStrategyUtil.phone(ObjectUtil.objectToString(original));
    }

}
