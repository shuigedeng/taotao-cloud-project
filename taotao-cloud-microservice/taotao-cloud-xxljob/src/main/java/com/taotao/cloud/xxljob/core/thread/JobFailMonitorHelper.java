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

package com.taotao.cloud.xxljob.core.thread;

import com.taotao.cloud.xxljob.core.conf.XxlJobAdminConfig;
import com.taotao.cloud.xxljob.core.model.XxlJobInfo;
import com.taotao.cloud.xxljob.core.model.XxlJobLog;
import com.taotao.cloud.xxljob.core.trigger.TriggerTypeEnum;
import com.taotao.cloud.xxljob.core.util.I18nUtil;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * job monitor instance
 *
 * @author xuxueli 2015-9-1 18:05:56
 */
public class JobFailMonitorHelper {
    private static Logger logger = LoggerFactory.getLogger(JobFailMonitorHelper.class);

    private static JobFailMonitorHelper instance = new JobFailMonitorHelper();

    public static JobFailMonitorHelper getInstance() {
        return instance;
    }

    // ---------------------- monitor ----------------------

    private Thread monitorThread;
    private volatile boolean toStop = false;

    public void start() {
        monitorThread =
                new Thread(
                        new Runnable() {

                            @Override
                            public void run() {

                                // monitor
                                while (!toStop) {
                                    try {

                                        List<Long> failLogIds =
                                                XxlJobAdminConfig.getAdminConfig()
                                                        .getXxlJobLogDao()
                                                        .findFailJobLogIds(1000);
                                        if (failLogIds != null && !failLogIds.isEmpty()) {
                                            for (long failLogId : failLogIds) {

                                                // lock log
                                                int lockRet =
                                                        XxlJobAdminConfig.getAdminConfig()
                                                                .getXxlJobLogDao()
                                                                .updateAlarmStatus(
                                                                        failLogId, 0, -1);
                                                if (lockRet < 1) {
                                                    continue;
                                                }
                                                XxlJobLog log =
                                                        XxlJobAdminConfig.getAdminConfig()
                                                                .getXxlJobLogDao()
                                                                .load(failLogId);
                                                XxlJobInfo info =
                                                        XxlJobAdminConfig.getAdminConfig()
                                                                .getXxlJobInfoDao()
                                                                .loadById(log.getJobId());

                                                // 1、fail retry monitor
                                                if (log.getExecutorFailRetryCount() > 0) {
                                                    JobTriggerPoolHelper.trigger(
                                                            log.getJobId(),
                                                            TriggerTypeEnum.RETRY,
                                                            (log.getExecutorFailRetryCount() - 1),
                                                            log.getExecutorShardingParam(),
                                                            log.getExecutorParam(),
                                                            null);
                                                    String retryMsg =
                                                            "<br><br><span style=\"color:#F39C12;\" > >>>>>>>>>>>"
                                                                    + I18nUtil.getString(
                                                                            "jobconf_trigger_type_retry")
                                                                    + "<<<<<<<<<<< </span><br>";
                                                    log.setTriggerMsg(
                                                            log.getTriggerMsg() + retryMsg);
                                                    XxlJobAdminConfig.getAdminConfig()
                                                            .getXxlJobLogDao()
                                                            .updateTriggerInfo(log);
                                                }

                                                // 2、fail alarm monitor
                                                int newAlarmStatus =
                                                        0; // 告警状态：0-默认、-1=锁定状态、1-无需告警、2-告警成功、3-告警失败
                                                if (info != null) {
                                                    boolean alarmResult =
                                                            XxlJobAdminConfig.getAdminConfig()
                                                                    .getJobAlarmer()
                                                                    .alarm(info, log);
                                                    newAlarmStatus = alarmResult ? 2 : 3;
                                                } else {
                                                    newAlarmStatus = 1;
                                                }

                                                XxlJobAdminConfig.getAdminConfig()
                                                        .getXxlJobLogDao()
                                                        .updateAlarmStatus(
                                                                failLogId, -1, newAlarmStatus);
                                            }
                                        }

                                    } catch (Throwable e) {
                                        if (!toStop) {
                                            logger.error(
                                                    ">>>>>>>>>>> xxl-job, job fail monitor thread error:{}",
                                                    e);
                                        }
                                    }

                                    try {
                                        TimeUnit.SECONDS.sleep(10);
                                    } catch (Throwable e) {
                                        if (!toStop) {
                                            logger.error(e.getMessage(), e);
                                        }
                                    }
                                }

                                logger.info(">>>>>>>>>>> xxl-job, job fail monitor thread stop");
                            }
                        });
        monitorThread.setDaemon(true);
        monitorThread.setName("xxl-job, admin JobFailMonitorHelper");
        monitorThread.start();
    }

    public void toStop() {
        toStop = true;
        // interrupt and wait
        monitorThread.interrupt();
        try {
            monitorThread.join();
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
    }
}
