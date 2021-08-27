package com.taotao.cloud.health.config;

import com.yh.csx.bsf.core.util.ContextUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: chejiangyi
 * @version: 2019-07-23 20:42
 **/
@Data
@ConfigurationProperties
public class WarnProperties {
    public static WarnProperties Default(){return ContextUtils.getApplicationContext().getBean(WarnProperties.class);}

    /**
     * 报警是否开启
     * @return
     */
    @Value("${bsf.health.warn.enabled:false}")
    private boolean bsfHealthWarnEnabled;
    //public static boolean WarnEnabled(){return PropertyUtils.getProperty("bsf.health.warn.enabled",false);}
    /**
     * 报警消息缓存数量
     * @return
     */
    @Value("${bsf.health.warn.cachecount:3}")
    private int bsfHealthWarnCacheCount;
    //public static int MaxCacheMessagesCount(){return PropertyUtils.getProperty("bsf.health.warn.cachecount",3);}

    /**
     * 报警消息循环间隔时间 秒
     * @return
     */
    @Value("${bsf.health.warn.timespan:10}")
    private int bsfHealthWarnTimeSpan;
    //public static int MaxNotifyTimeSpan(){return PropertyUtils.getProperty("bsf.health.warn.timespan",10);}

    /**
     * 报警重复过滤时间间隔 分钟
     * @return
     */
    @Value("${bsf.health.warn.duplicate.timespan:2}")
    private int bsfHealthWarnDuplicateTimeSpan;
    //public static int MaxNotifyDuplicateTimeSpan(){return PropertyUtils.getProperty("bsf.health.warn.duplicate.timespan",2);}

    /**
     * 钉钉报警系统token
     * @return
     */
    @Value("${bsf.health.warn.dingding.system.access_token:}")
    private String bsfHealthWarnDingdingSystemAccessToken;
    //public static String DingdingSystemAccessToken(){return PropertyUtils.getProperty("bsf.health.warn.dingding.system.access_token","");}

    /**
     * 钉钉报警项目token
     * @return
     */
    @Value("${bsf.health.warn.dingding.project.access_token:}")
    private String bsfHealthWarnDingdingProjectAccessToken;
    //public static String DingdingProjectAccessToken(){return PropertyUtils.getProperty("bsf.health.warn.dingding.project.access_token","");}

    /**
     * 钉钉报警过滤ip
     * @return
     */
    @Value("${bsf.health.warn.dingding.filter.ip:}")
    private String bsfHealthWarnDingdingFilterIP;
    //public static String DingdingFilterIp(){return PropertyUtils.getProperty("bsf.health.warn.dingding.filter.ip",""); }

    /**
     * 飞书报警系统token
     * @return
     */
    @Value("${bsf.health.warn.flybook.system.access_token:}")
    private String bsfHealthWarnFlybookSystemAccessToken;

    /**
     * 飞书报警项目token
     * @return
     */
    @Value("${bsf.health.warn.flybook.project.access_token:}")
    private String bsfHealthWarnFlybookProjectAccessToken;

    /**
     * 飞书报警过滤ip
     * @return
     */
    @Value("${bsf.health.warn.flybook.filter.ip:}")
    private String bsfHealthWarnFlybookFilterIP;
}
