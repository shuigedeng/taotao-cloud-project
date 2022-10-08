package com.taotao.cloud.lock.kylin.aop;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.lang.NonNull;

/**
 * 分布式锁aop通知
 *
 * @author wangjinkui
 */
public class LockAnnotationAdvisor extends AbstractPointcutAdvisor {
    private final Advice advice;
    private final Pointcut pointcut;

    public LockAnnotationAdvisor(@NonNull LockInterceptor lockInterceptor,
                                 @NonNull Pointcut pointcut,
                                 int order) {
        this.advice = lockInterceptor;
        this.pointcut = pointcut;
        setOrder(order);
    }

    /**
     * 切入点，定义了将被Advice增强的一个或多个Join Point，可以使用正则表达式或模式匹配。
     *
     * @return 切点
     */
    @SuppressWarnings("NullableProblems")
    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    /**
     * 所要做的增强处理
     *
     * @return 通知
     */
    @SuppressWarnings("NullableProblems")
    @Override
    public Advice getAdvice() {
        return this.advice;
    }

}
