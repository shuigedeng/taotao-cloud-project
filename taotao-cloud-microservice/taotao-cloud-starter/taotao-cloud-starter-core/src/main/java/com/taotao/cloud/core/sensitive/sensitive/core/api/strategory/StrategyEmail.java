package com.taotao.cloud.core.sensitive.sensitive.core.api.strategory;

import com.taotao.cloud.core.heaven.util.lang.ObjectUtil;
import com.taotao.cloud.core.sensitive.sensitive.api.IContext;
import com.taotao.cloud.core.sensitive.sensitive.api.IStrategy;
import com.taotao.cloud.core.sensitive.sensitive.core.util.strategy.SensitiveStrategyUtil;

/**
 * 邮箱脱敏策略
 * 脱敏规则：
 * 保留前三位，中间隐藏4位。其他正常显示
 */
public class StrategyEmail implements IStrategy {

    @Override
    public Object des(Object original, IContext context) {
        return SensitiveStrategyUtil.email(ObjectUtil.objectToString(original));
    }

}
