/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.xxljob.core.conf;

import com.taotao.cloud.xxljob.core.alarm.JobAlarmer;
import com.taotao.cloud.xxljob.core.scheduler.XxlJobScheduler;
import com.taotao.cloud.xxljob.dao.*;
import jakarta.annotation.Resource;
import java.util.Arrays;
import javax.sql.DataSource;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * xxl-job config
 *
 * @author xuxueli 2017-04-28
 */
@Component
public class XxlJobAdminConfig
        implements InitializingBean, DisposableBean, ApplicationEventPublisherAware {

    private static XxlJobAdminConfig adminConfig = null;

    public static XxlJobAdminConfig getAdminConfig() {
        return adminConfig;
    }

    // ---------------------- XxlJobScheduler ----------------------

    private XxlJobScheduler xxlJobScheduler;

    @Override
    public void afterPropertiesSet() throws Exception {
        adminConfig = this;

        xxlJobScheduler = new XxlJobScheduler();
        xxlJobScheduler.init();
    }

    @Override
    public void destroy() throws Exception {
        xxlJobScheduler.destroy();
    }

    // ---------------------- XxlJobScheduler ----------------------

    // conf
    @Value("${xxl.job.i18n}")
    private String i18n;

    @Value("${xxl.job.accessToken}")
    private String accessToken;

    @Value("${xxl.job.timeout}")
    private int timeout;

    @Value("${spring.mail.from}")
    private String emailFrom;

    @Value("${xxl.job.triggerpool.fast.max}")
    private int triggerPoolFastMax;

    @Value("${xxl.job.triggerpool.slow.max}")
    private int triggerPoolSlowMax;

    @Value("${xxl.job.logretentiondays}")
    private int logretentiondays;

    // dao, service

    @Resource private XxlJobLogDao xxlJobLogDao;
    @Resource private XxlJobInfoDao xxlJobInfoDao;
    @Resource private XxlJobRegistryDao xxlJobRegistryDao;
    @Resource private XxlJobGroupDao xxlJobGroupDao;
    @Resource private XxlJobLogReportDao xxlJobLogReportDao;
    @Resource private JavaMailSender mailSender;
    @Resource private DataSource dataSource;
    @Resource private JobAlarmer jobAlarmer;

    public String getI18n() {
        if (!Arrays.asList("zh_CN", "zh_TC", "en").contains(i18n)) {
            return "zh_CN";
        }
        return i18n;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getTimeout() {
        return timeout;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public int getTriggerPoolFastMax() {
        if (triggerPoolFastMax < 200) {
            return 200;
        }
        return triggerPoolFastMax;
    }

    public int getTriggerPoolSlowMax() {
        if (triggerPoolSlowMax < 100) {
            return 100;
        }
        return triggerPoolSlowMax;
    }

    public int getLogretentiondays() {
        if (logretentiondays < 3) {
            return -1; // Limit greater than or equal to 3, otherwise close
        }
        return logretentiondays;
    }

    public XxlJobLogDao getXxlJobLogDao() {
        return xxlJobLogDao;
    }

    public XxlJobInfoDao getXxlJobInfoDao() {
        return xxlJobInfoDao;
    }

    public XxlJobRegistryDao getXxlJobRegistryDao() {
        return xxlJobRegistryDao;
    }

    public XxlJobGroupDao getXxlJobGroupDao() {
        return xxlJobGroupDao;
    }

    public XxlJobLogReportDao getXxlJobLogReportDao() {
        return xxlJobLogReportDao;
    }

    public JavaMailSender getMailSender() {
        return mailSender;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public JobAlarmer getJobAlarmer() {
        return jobAlarmer;
    }

    private ApplicationEventPublisher applicationEventPublisher;

    public ApplicationEventPublisher getApplicationEventPublisher() {
        return applicationEventPublisher;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
