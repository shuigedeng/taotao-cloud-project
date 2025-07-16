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

package com.taotao.cloud.xxljob.service;

import com.taotao.cloud.xxljob.core.model.XxlJobInfo;
import com.taotao.cloud.xxljob.core.model.XxlJobUser;
import com.xxl.job.core.biz.model.ReturnT;
import java.util.Date;
import java.util.Map;

/**
 * core job action for xxl-job
 *
 * @author xuxueli 2016-5-28 15:30:33
 */
public interface XxlJobService {

    /**
     * page list
     *
     * @param start
     * @param length
     * @param jobGroup
     * @param jobDesc
     * @param executorHandler
     * @param author
     * @return
     */
    public Map<String, Object> pageList(
            int start,
            int length,
            int jobGroup,
            int triggerStatus,
            String jobDesc,
            String executorHandler,
            String author);

    /**
     * add job
     *
     * @param jobInfo
     * @return
     */
    public ReturnT<String> add(XxlJobInfo jobInfo, XxlJobUser loginUser);

    /**
     * update job
     *
     * @param jobInfo
     * @return
     */
    public ReturnT<String> update(XxlJobInfo jobInfo, XxlJobUser loginUser);

    /**
     * remove job
     * 	 *
     * @param id
     * @return
     */
    public ReturnT<String> remove(int id);

    /**
     * start job
     *
     * @param id
     * @return
     */
    public ReturnT<String> start(int id);

    /**
     * stop job
     *
     * @param id
     * @return
     */
    public ReturnT<String> stop(int id);

    /**
     * trigger
     *
     * @param loginUser
     * @param jobId
     * @param executorParam
     * @param addressList
     * @return
     */
    public ReturnT<String> trigger(
            XxlJobUser loginUser, int jobId, String executorParam, String addressList);

    /**
     * dashboard info
     *
     * @return
     */
    public Map<String, Object> dashboardInfo();

    /**
     * chart info
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public ReturnT<Map<String, Object>> chartInfo(Date startDate, Date endDate);
}
