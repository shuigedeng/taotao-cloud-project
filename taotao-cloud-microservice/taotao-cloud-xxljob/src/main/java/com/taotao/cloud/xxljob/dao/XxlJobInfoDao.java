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

import com.taotao.cloud.xxljob.core.model.XxlJobInfo;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * job info
 * @author xuxueli 2016-1-12 18:03:45
 */
@Mapper
public interface XxlJobInfoDao {

    public List<XxlJobInfo> pageList(
            @Param("offset") int offset,
            @Param("pagesize") int pagesize,
            @Param("jobGroup") int jobGroup,
            @Param("triggerStatus") int triggerStatus,
            @Param("jobDesc") String jobDesc,
            @Param("executorHandler") String executorHandler,
            @Param("author") String author);

    public int pageListCount(
            @Param("offset") int offset,
            @Param("pagesize") int pagesize,
            @Param("jobGroup") int jobGroup,
            @Param("triggerStatus") int triggerStatus,
            @Param("jobDesc") String jobDesc,
            @Param("executorHandler") String executorHandler,
            @Param("author") String author);

    public int save(XxlJobInfo info);

    public XxlJobInfo loadById(@Param("id") int id);

    public int update(XxlJobInfo xxlJobInfo);

    public int delete(@Param("id") long id);

    public List<XxlJobInfo> getJobsByGroup(@Param("jobGroup") int jobGroup);

    public int findAllCount();

    /**
     * find schedule job, limit "trigger_status = 1"
     *
     * @param maxNextTime
     * @param pagesize
     * @return
     */
    public List<XxlJobInfo> scheduleJobQuery(
            @Param("maxNextTime") long maxNextTime, @Param("pagesize") int pagesize);

    /**
     * update schedule job
     *
     * 	1、can only update "trigger_status = 1", Avoid stopping tasks from being opened
     * 	2、valid "triggerStatus gte 0", filter illegal state
     *
     * @param xxlJobInfo
     * @return
     */
    public int scheduleUpdate(XxlJobInfo xxlJobInfo);
}
