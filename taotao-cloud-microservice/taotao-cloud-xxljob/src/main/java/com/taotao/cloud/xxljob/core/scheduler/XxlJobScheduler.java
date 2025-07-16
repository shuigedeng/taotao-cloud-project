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

package com.taotao.cloud.xxljob.core.scheduler;

import com.taotao.cloud.xxljob.core.conf.XxlJobAdminConfig;
import com.taotao.cloud.xxljob.core.thread.*;
import com.taotao.cloud.xxljob.core.util.I18nUtil;
import com.xxl.job.core.biz.ExecutorBiz;
import com.xxl.job.core.biz.client.ExecutorBizClient;
import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xuxueli 2018-10-28 00:18:17
 */
public class XxlJobScheduler {
    private static final Logger logger = LoggerFactory.getLogger(XxlJobScheduler.class);

    public void init() throws Exception {
        // init i18n
        initI18n();

        // admin trigger pool start
        JobTriggerPoolHelper.toStart();

        // admin registry monitor run
        JobRegistryHelper.getInstance().start();

        // admin fail-monitor run
        JobFailMonitorHelper.getInstance().start();

        // admin lose-monitor run ( depend on JobTriggerPoolHelper )
        JobCompleteHelper.getInstance().start();

        // admin log report start
        JobLogReportHelper.getInstance().start();

        // start-schedule  ( depend on JobTriggerPoolHelper )
        JobScheduleHelper.getInstance().start();

        logger.info(">>>>>>>>> init xxl-job admin success.");
    }

    public void destroy() throws Exception {

        // stop-schedule
        JobScheduleHelper.getInstance().toStop();

        // admin log report stop
        JobLogReportHelper.getInstance().toStop();

        // admin lose-monitor stop
        JobCompleteHelper.getInstance().toStop();

        // admin fail-monitor stop
        JobFailMonitorHelper.getInstance().toStop();

        // admin registry stop
        JobRegistryHelper.getInstance().toStop();

        // admin trigger pool stop
        JobTriggerPoolHelper.toStop();
    }

    // ---------------------- I18n ----------------------

    private void initI18n() {
        for (ExecutorBlockStrategyEnum item : ExecutorBlockStrategyEnum.values()) {
            item.setTitle(I18nUtil.getString("jobconf_block_".concat(item.name())));
        }
    }

    // ---------------------- executor-client ----------------------
    private static ConcurrentMap<String, ExecutorBiz> executorBizRepository =
            new ConcurrentHashMap<String, ExecutorBiz>();

    public static ExecutorBiz getExecutorBiz(String address) throws Exception {
        // valid
        if (address == null || address.trim().length() == 0) {
            return null;
        }

        // load-cache
        address = address.trim();
        ExecutorBiz executorBiz = executorBizRepository.get(address);
        if (executorBiz != null) {
            return executorBiz;
        }

        // set-cache
        executorBiz =
                new ExecutorBizClient(
                        address,
                        XxlJobAdminConfig.getAdminConfig().getAccessToken(),
                        XxlJobAdminConfig.getAdminConfig().getTimeout());

        executorBizRepository.put(address, executorBiz);
        return executorBiz;
    }
}
