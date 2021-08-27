package com.taotao.cloud.health.config;

import com.yh.csx.bsf.core.util.ContextUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: chejiangyi
 * @version: 2019-07-24 21:03
 **/
@ConfigurationProperties
@Data
public class HealthProperties {
    public static String Project="Health";
    public static String SpringApplictionName="spring.application.name";
    public static String BsfHealthEnabled="bsf.health.enabled";
    public static String BsfEnv = "bsf.env";

    public static HealthProperties Default(){return ContextUtils.getApplicationContext().getBean(HealthProperties.class);}

    /**
     * 是否开启健康检查
     * @return
     */
    @Value("${bsf.health.enabled:false}")
    private boolean bsfHealthEnabled;

    /**
     * 健康检查时间间隔 秒
     * @return
     */
    @Value("${bsf.health.timespan:10}")
    private int bsfHealthTimeSpan;
    //public static int getHealthCheckTimeSpan(){return PropertyUtils.getProperty("bsf.health.timespan",10);}

}
