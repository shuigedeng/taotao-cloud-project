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

package com.taotao.cloud.xxljob.dao;

import com.taotao.cloud.xxljob.core.model.XxlJobLog;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * job log
 * @author xuxueli 2016-1-12 18:03:06
 */
@Mapper
public interface XxlJobLogDao {

    // exist jobId not use jobGroup, not exist use jobGroup
    public List<XxlJobLog> pageList(
            @Param("offset") int offset,
            @Param("pagesize") int pagesize,
            @Param("jobGroup") int jobGroup,
            @Param("jobId") int jobId,
            @Param("triggerTimeStart") Date triggerTimeStart,
            @Param("triggerTimeEnd") Date triggerTimeEnd,
            @Param("logStatus") int logStatus);

    public int pageListCount(
            @Param("offset") int offset,
            @Param("pagesize") int pagesize,
            @Param("jobGroup") int jobGroup,
            @Param("jobId") int jobId,
            @Param("triggerTimeStart") Date triggerTimeStart,
            @Param("triggerTimeEnd") Date triggerTimeEnd,
            @Param("logStatus") int logStatus);

    public XxlJobLog load(@Param("id") long id);

    public long save(XxlJobLog xxlJobLog);

    public int updateTriggerInfo(XxlJobLog xxlJobLog);

    public int updateHandleInfo(XxlJobLog xxlJobLog);

    public int delete(@Param("jobId") int jobId);

    public Map<String, Object> findLogReport(@Param("from") Date from, @Param("to") Date to);

    public List<Long> findClearLogIds(
            @Param("jobGroup") int jobGroup,
            @Param("jobId") int jobId,
            @Param("clearBeforeTime") Date clearBeforeTime,
            @Param("clearBeforeNum") int clearBeforeNum,
            @Param("pagesize") int pagesize);

    public int clearLog(@Param("logIds") List<Long> logIds);

    public List<Long> findFailJobLogIds(@Param("pagesize") int pagesize);

    public int updateAlarmStatus(
            @Param("logId") long logId,
            @Param("oldAlarmStatus") int oldAlarmStatus,
            @Param("newAlarmStatus") int newAlarmStatus);

    public List<Long> findLostJobIds(@Param("losedTime") Date losedTime);
}
