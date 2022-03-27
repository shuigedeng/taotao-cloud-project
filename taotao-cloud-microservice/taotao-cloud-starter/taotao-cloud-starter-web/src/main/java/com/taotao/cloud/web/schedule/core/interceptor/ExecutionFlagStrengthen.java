package com.taotao.cloud.web.schedule.core.interceptor;

import com.taotao.cloud.web.schedule.common.annotation.ScheduledInterceptorOrder;
import com.taotao.cloud.web.schedule.common.utils.proxy.Point;
import com.taotao.cloud.web.schedule.model.ScheduledRunningContext;
import java.lang.reflect.Method;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ExecutionFlagStrengthen 
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 15:17:48
 */
@ScheduledInterceptorOrder
public class ExecutionFlagStrengthen implements BaseStrengthen {
    protected final Log logger = LogFactory.getLog(getClass());

    /**
     * 前置强化方法
     *
     * @param bean    bean实例（或者是被代理的bean）
     * @param method  执行的方法对象
     * @param args    方法参数
     * @param context 任务线程运行时的上下文
     */
    @Override
    public void before(Object bean, Method method, Object[] args, ScheduledRunningContext context) {
        Point point = (Point) bean;
        logger.info("定时任务" + point.getSuperScheduledName() + "开始执行");
    }

    /**
     * 后置强化方法
     *
     * @param bean    bean实例（或者是被代理的bean）
     * @param method  执行的方法对象
     * @param args    方法参数
     * @param context 任务线程运行时的上下文
     */
    @Override
    public void after(Object bean, Method method, Object[] args, ScheduledRunningContext context) {
        Point point = (Point) bean;
        logger.info("定时任务" + point.getSuperScheduledName() + "执行结束");
    }

    /**
     * 异常强化方法
     *
     * @param bean    bean实例（或者是被代理的bean）
     * @param method  执行的方法对象
     * @param args    方法参数
     * @param context 任务线程运行时的上下文
     */
    @Override
    public void exception(Object bean, Method method, Object[] args, ScheduledRunningContext context) {
	    Point point = (Point) bean;
	    logger.info("定时任务" + point.getSuperScheduledName() + "执行异常");
    }

    /**
     * Finally强化方法，出现异常也会执行
     *
     * @param bean    bean实例（或者是被代理的bean）
     * @param method  执行的方法对象
     * @param args    方法参数
     * @param context 任务线程运行时的上下文
     */
    @Override
    public void afterFinally(Object bean, Method method, Object[] args, ScheduledRunningContext context) {
	    Point point = (Point) bean;
	    logger.info("定时任务" + point.getSuperScheduledName() + "执行完成");
    }

}
