package com.taotao.cloud.quartz.other;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 * 为JobFactory注入SpringBean,否则Job无法使用Spring创建的bean
 * 
 * @author Ric
 */
@Component
public class QuartzJobFactory extends AdaptableJobFactory implements ApplicationContextAware {

    @Autowired
    private AutowireCapableBeanFactory capableBeanFactory;

    @Override
    @NonNull
    protected Object createJobInstance(@NonNull TriggerFiredBundle bundle) throws Exception {
        // 调用父类的方法
        Object jobInstance = super.createJobInstance(bundle);
        // 进行注入
        capableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        capableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
    }
}
