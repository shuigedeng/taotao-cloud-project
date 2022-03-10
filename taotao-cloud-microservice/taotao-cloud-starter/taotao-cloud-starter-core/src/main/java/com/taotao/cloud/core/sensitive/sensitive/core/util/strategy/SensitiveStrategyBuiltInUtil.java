package com.taotao.cloud.core.sensitive.sensitive.core.util.strategy;


import com.taotao.cloud.common.utils.lang.ObjectUtil;
import com.taotao.cloud.common.utils.reflect.ClassUtil;
import com.taotao.cloud.core.sensitive.sensitive.annotation.metadata.SensitiveStrategy;
import com.taotao.cloud.core.sensitive.sensitive.annotation.strategy.SensitiveStrategyCardId;
import com.taotao.cloud.core.sensitive.sensitive.annotation.strategy.SensitiveStrategyChineseName;
import com.taotao.cloud.core.sensitive.sensitive.annotation.strategy.SensitiveStrategyEmail;
import com.taotao.cloud.core.sensitive.sensitive.annotation.strategy.SensitiveStrategyPassword;
import com.taotao.cloud.core.sensitive.sensitive.annotation.strategy.SensitiveStrategyPhone;
import com.taotao.cloud.core.sensitive.sensitive.api.IStrategy;
import com.taotao.cloud.core.sensitive.sensitive.api.impl.SensitiveStrategyBuiltIn;
import com.taotao.cloud.core.sensitive.sensitive.core.api.strategory.StrategyCardId;
import com.taotao.cloud.core.sensitive.sensitive.core.api.strategory.StrategyChineseName;
import com.taotao.cloud.core.sensitive.sensitive.core.api.strategory.StrategyEmail;
import com.taotao.cloud.core.sensitive.sensitive.core.api.strategory.StrategyPassword;
import com.taotao.cloud.core.sensitive.sensitive.core.api.strategory.StrategyPhone;
import com.taotao.cloud.core.sensitive.sensitive.core.exception.SensitiveRuntimeException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 系统中内置的策略映射
 * 1. 注解和实现之间映射
 */
public final class SensitiveStrategyBuiltInUtil {

    private SensitiveStrategyBuiltInUtil(){}

    /**
     * 注解和实现策略的映射关系
     */
    private static final Map<Class<? extends Annotation>, IStrategy> MAP = new HashMap<>();

    static {
        MAP.put(SensitiveStrategyCardId.class, new StrategyCardId());
        MAP.put(SensitiveStrategyPassword.class, new StrategyPassword());
        MAP.put(SensitiveStrategyPhone.class, new StrategyPhone());
        MAP.put(SensitiveStrategyChineseName.class, new StrategyChineseName());
        MAP.put(SensitiveStrategyEmail.class, new StrategyEmail());
    }

    /**
     * 获取对应的系统内置实现
     * @param annotationClass 注解实现类
     * @return 对应的实现方式
     */
    public static IStrategy require(final Class<? extends Annotation> annotationClass) {
        IStrategy strategy = MAP.get(annotationClass);
        if(ObjectUtil.isNull(strategy)) {
            throw new SensitiveRuntimeException("不支持的系统内置方法，用户请勿在自定义注解中使用[SensitiveStrategyBuiltIn]!");
        }
        return strategy;
    }

    /**
     * 获取策略
     *
     * @param annotations 字段对应注解
     * @return 策略
     * @since 0.0.6
     */
    public static Optional<IStrategy> getStrategyOpt(final Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            SensitiveStrategy sensitiveStrategy = annotation.annotationType().getAnnotation(SensitiveStrategy.class);
            if (ObjectUtil.isNotNull(sensitiveStrategy)) {
                Class<? extends IStrategy> clazz = sensitiveStrategy.value();
                IStrategy strategy = null;
                if (SensitiveStrategyBuiltIn.class.equals(clazz)) {
                    strategy = SensitiveStrategyBuiltInUtil.require(annotation.annotationType());
                } else {
                    strategy = ClassUtil.newInstance(clazz);
                }
                return Optional.ofNullable(strategy);
            }
        }
        return Optional.empty();
    }

}
