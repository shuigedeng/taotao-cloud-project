package com.taotao.cloud.quartz.other;

import java.io.IOException;
import java.util.Properties;
import javax.sql.DataSource;
import org.quartz.Scheduler;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfigure {

    /**
     * quartz配置文件路径
     */
    private static final String QUARTZ_CONFIG = "/quartz.properties";

    /**
     * JobFactory与schedulerFactoryBean中的JobFactory相互依赖,注意bean的名称
     * 在这里为JobFactory注入了Spring上下文
     *
     * @param applicationContext
     * @return
     */
    @Bean
    public JobFactory customJobFactory(ApplicationContext applicationContext) {
        QuartzJobFactory jobFactory = new QuartzJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory customJobFactory, DataSource dataSource) throws IOException {
        // 创建SchedulerFactoryBean
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setQuartzProperties(quartzProperties());
        // 支持在JOB实例中注入其他的业务对象
        factory.setJobFactory(customJobFactory);
        factory.setApplicationContextSchedulerContextKey("applicationContextKey");
        // 这样当spring关闭时，会等待所有已经启动的quartz job结束后spring才能完全shutdown。
        factory.setWaitForJobsToCompleteOnShutdown(true);
        // 是否覆盖己存在的Job
        factory.setOverwriteExistingJobs(false);
        // QuartzScheduler 延时启动，应用启动完后 QuartzScheduler 再启动
        factory.setStartupDelay(10);
        // 注入spring维护的DataSource
        factory.setDataSource(dataSource);
        return factory;
    }

    /**
     * 从quartz.properties文件中读取Quartz配置属性
     *
     * @return
     * @throws IOException
     */
    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource(QUARTZ_CONFIG));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    /**
     * 通过SchedulerFactoryBean获取Scheduler的实例
     *
     * @return
     * @throws IOException
     */
    @Bean(name = "scheduler")
    public Scheduler scheduler(JobFactory customJobFactory, DataSource dataSource) throws IOException {
        return schedulerFactoryBean(customJobFactory, dataSource).getScheduler();
    }

}
