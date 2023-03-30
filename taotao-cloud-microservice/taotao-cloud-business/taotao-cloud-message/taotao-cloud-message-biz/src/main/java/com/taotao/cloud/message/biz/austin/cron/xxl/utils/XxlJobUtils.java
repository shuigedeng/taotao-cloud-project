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

package com.taotao.cloud.message.biz.austin.cron.xxl.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.message.biz.austin.common.constant.CommonConstant;
import com.taotao.cloud.message.biz.austin.common.enums.RespStatusEnum;
import com.taotao.cloud.message.biz.austin.common.vo.BasicResultVO;
import com.taotao.cloud.message.biz.austin.cron.xxl.constants.XxlJobConstant;
import com.taotao.cloud.message.biz.austin.cron.xxl.entity.XxlJobGroup;
import com.taotao.cloud.message.biz.austin.cron.xxl.entity.XxlJobInfo;
import com.taotao.cloud.message.biz.austin.cron.xxl.enums.ExecutorRouteStrategyEnum;
import com.taotao.cloud.message.biz.austin.cron.xxl.enums.MisfireStrategyEnum;
import com.taotao.cloud.message.biz.austin.cron.xxl.enums.ScheduleTypeEnum;
import com.taotao.cloud.message.biz.austin.cron.xxl.service.CronTaskService;
import com.taotao.cloud.message.biz.austin.support.domain.MessageTemplate;
import java.util.Date;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * xxlJob工具类
 *
 * @author 3y
 */
@Component
public class XxlJobUtils {

    @Value("${xxl.job.executor.appname}")
    private String appName;

    @Value("${xxl.job.executor.jobHandlerName}")
    private String jobHandlerName;

    @Autowired private CronTaskService cronTaskService;

    /**
     * 构建xxlJobInfo信息
     *
     * @param messageTemplate
     * @return
     */
    public XxlJobInfo buildXxlJobInfo(MessageTemplate messageTemplate) {

        String scheduleConf = messageTemplate.getExpectPushTime();
        // 如果没有指定cron表达式，说明立即执行(给到xxl-job延迟5秒的cron表达式)
        if (messageTemplate.getExpectPushTime().equals(String.valueOf(CommonConstant.FALSE))) {
            scheduleConf =
                    DateUtil.format(
                            DateUtil.offsetSecond(new Date(), XxlJobConstant.DELAY_TIME),
                            CommonConstant.CRON_FORMAT);
        }

        XxlJobInfo xxlJobInfo =
                XxlJobInfo.builder()
                        .jobGroup(queryJobGroupId())
                        .jobDesc(messageTemplate.getName())
                        .author(messageTemplate.getCreator())
                        .scheduleConf(scheduleConf)
                        .scheduleType(ScheduleTypeEnum.CRON.name())
                        .misfireStrategy(MisfireStrategyEnum.DO_NOTHING.name())
                        .executorRouteStrategy(ExecutorRouteStrategyEnum.CONSISTENT_HASH.name())
                        .executorHandler(XxlJobConstant.JOB_HANDLER_NAME)
                        .executorParam(String.valueOf(messageTemplate.getId()))
                        .executorBlockStrategy(ExecutorBlockStrategyEnum.SERIAL_EXECUTION.name())
                        .executorTimeout(XxlJobConstant.TIME_OUT)
                        .executorFailRetryCount(XxlJobConstant.RETRY_COUNT)
                        .glueType(GlueTypeEnum.BEAN.name())
                        .triggerStatus(CommonConstant.FALSE)
                        .glueRemark(StrUtil.EMPTY)
                        .glueSource(StrUtil.EMPTY)
                        .alarmEmail(StrUtil.EMPTY)
                        .childJobId(StrUtil.EMPTY)
                        .build();

        if (Objects.nonNull(messageTemplate.getCronTaskId())) {
            xxlJobInfo.setId(messageTemplate.getCronTaskId());
        }
        return xxlJobInfo;
    }

    /**
     * 根据就配置文件的内容获取jobGroupId，没有则创建
     *
     * @return
     */
    private Integer queryJobGroupId() {
        BasicResultVO basicResultVO = cronTaskService.getGroupId(appName, jobHandlerName);
        if (Objects.isNull(basicResultVO.getData())) {
            XxlJobGroup xxlJobGroup =
                    XxlJobGroup.builder()
                            .appname(appName)
                            .title(jobHandlerName)
                            .addressType(CommonConstant.FALSE)
                            .build();
            if (RespStatusEnum.SUCCESS
                    .getCode()
                    .equals(cronTaskService.createGroup(xxlJobGroup).getStatus())) {
                return (int) cronTaskService.getGroupId(appName, jobHandlerName).getData();
            }
        }
        return (Integer) basicResultVO.getData();
    }
}
