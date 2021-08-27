package com.taotao.cloud.health.config;

import com.yh.csx.bsf.core.util.ContextUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: chejiangyi
 * @version: 2019-08-14 12:07
 **/
@ConfigurationProperties
@Data
public class ExportProperties {
    public static ExportProperties Default(){return ContextUtils.getApplicationContext().getBean(ExportProperties.class);}
    /**
     * 上传报表循环间隔时间 秒
     * @return
     */
    @Value("${bsf.health.export.timespan:30}")
    private int bsfHealthExportTimeSpan;

    @Value("${bsf.health.export.elk.destinations:${bsf.elk.destinations:}}")
    private String[] bsfElkDestinations;

    @Value("${bsf.health.export.elk.enabled:false}")
    private boolean bsfElkEnabled;

    @Value("${bsf.health.export.cat.enabled:false}")
    private boolean bsfCatEnabled;

    @Value("${bsf.health.export.cat.server.url:${cat.server.url:}}")
    private String bsfCatServerUrl;
}
