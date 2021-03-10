package com.taotao.cloud.java.javaee.s2.c11_distributed.job.java.config;


import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.taotao.cloud.java.javaee.s2.c11_distributed.job.java.job.MyElasticJob;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticJobConfig {


    // 注册中心
    @Bean
    public CoordinatorRegistryCenter center(){
        CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(
                new ZookeeperConfiguration("192.168.199.109:2181,192.168.199.109:2182,192.168.199.109:2183", "elastic-job-demo"));
        regCenter.init();
        return regCenter;
    }


    // 执行任务调度信息
    @Bean
    public SpringJobScheduler scheduler(MyElasticJob job,CoordinatorRegistryCenter center){
        // 定义作业核心配置
        JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration.
                newBuilder("demoSimpleJob", "0/10 * * * * ?", 3)
                .shardingItemParameters("0=A,1=B,2=C").build();
        // 定义SIMPLE类型配置
        SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig, MyElasticJob.class.getCanonicalName());
        // 定义Lite作业根配置
        LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).build();
        // 定义SpringJobScheduler
        SpringJobScheduler scheduler = new SpringJobScheduler(job,center,simpleJobRootConfig);
        scheduler.init();
        return scheduler;
    }

}
