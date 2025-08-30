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

package com.taotao.cloud.xxljob.core.complete;

import com.taotao.cloud.xxljob.core.conf.XxlJobAdminConfig;
import com.taotao.cloud.xxljob.core.model.XxlJobInfo;
import com.taotao.cloud.xxljob.core.model.XxlJobLog;
import com.taotao.cloud.xxljob.core.thread.JobTriggerPoolHelper;
import com.taotao.cloud.xxljob.core.trigger.TriggerTypeEnum;
import com.taotao.cloud.xxljob.core.util.I18nUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobContext;
import java.text.MessageFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xuxueli 2020-10-30 20:43:10
 */
public class XxlJobCompleter {
    private static Logger logger = LoggerFactory.getLogger(XxlJobCompleter.class);

    /**
     * common fresh handle entrance (limit only once)
     *
     * @param xxlJobLog
     * @return
     */
    public static int updateHandleInfoAndFinish(XxlJobLog xxlJobLog) {

        // finish
        finishJob(xxlJobLog);

        // text最大64kb 避免长度过长
        if (xxlJobLog.getHandleMsg().length() > 15000) {
            xxlJobLog.setHandleMsg(xxlJobLog.getHandleMsg().substring(0, 15000));
        }

        // fresh handle
        return XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().updateHandleInfo(xxlJobLog);
    }

    /**
     * do somethind to finish job
     */
    private static void finishJob(XxlJobLog xxlJobLog) {

        // 1、handle success, to trigger child job
        String triggerChildMsg = null;
        if (XxlJobContext.HANDLE_CODE_SUCCESS == xxlJobLog.getHandleCode()) {
            XxlJobInfo xxlJobInfo =
                    XxlJobAdminConfig.getAdminConfig()
                            .getXxlJobInfoDao()
                            .loadById(xxlJobLog.getJobId());
            if (xxlJobInfo != null
                    && xxlJobInfo.getChildJobId() != null
                    && xxlJobInfo.getChildJobId().trim().length() > 0) {
                triggerChildMsg =
                        "<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>"
                                + I18nUtil.getString("jobconf_trigger_child_run")
                                + "<<<<<<<<<<< </span><br>";

                String[] childJobIds = xxlJobInfo.getChildJobId().split(",");
                for (int i = 0; i < childJobIds.length; i++) {
                    int childJobId =
                            (childJobIds[i] != null
                                            && childJobIds[i].trim().length() > 0
                                            && isNumeric(childJobIds[i]))
                                    ? Integer.valueOf(childJobIds[i])
                                    : -1;
                    if (childJobId > 0) {
                        // valid
                        if (childJobId == xxlJobLog.getJobId()) {
                            logger.debug(
                                    ">>>>>>>>>>> xxl-job, XxlJobCompleter-finishJob ignore childJobId,  childJobId {} is self.",
                                    childJobId);
                            continue;
                        }

                        // trigger child job
                        JobTriggerPoolHelper.trigger(
                                childJobId, TriggerTypeEnum.PARENT, -1, null, null, null);
                        ReturnT<String> triggerChildResult = ReturnT.ofSuccess();

                        // add msg
                        triggerChildMsg +=
                                MessageFormat.format(
                                        I18nUtil.getString("jobconf_callback_child_msg1"),
                                        (i + 1),
                                        childJobIds.length,
                                        childJobIds[i],
                                        (triggerChildResult.getCode() == ReturnT.ofSuccess()_CODE
                                                ? I18nUtil.getString("system_success")
                                                : I18nUtil.getString("system_fail")),
                                        triggerChildResult.getMsg());
                    } else {
                        triggerChildMsg +=
                                MessageFormat.format(
                                        I18nUtil.getString("jobconf_callback_child_msg2"),
                                        (i + 1),
                                        childJobIds.length,
                                        childJobIds[i]);
                    }
                }
            }
        }

        if (triggerChildMsg != null) {
            xxlJobLog.setHandleMsg(xxlJobLog.getHandleMsg() + triggerChildMsg);
        }

        // 2、fix_delay trigger next
        // on the way

    }

    private static boolean isNumeric(String str) {
        try {
            int result = Integer.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
