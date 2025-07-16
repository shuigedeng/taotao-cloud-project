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

package com.taotao.cloud.bff.api.service.mp;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.report.api.model.dto.StatisticsQueryParam;
import com.taotao.cloud.report.biz.model.entity.MemberStatisticsData;
import java.util.Date;
import java.util.List;

/** 会员统计业务层 */
public interface IMemberStatisticsService extends IService<MemberStatisticsData> {

    /**
     * 获取会员数量
     *
     * @return 会员统计
     */
    long getMemberCount();

    /**
     * 获取今日新增会员数量
     *
     * @return 今日新增会员数量
     */
    long todayMemberNum();

    /**
     * 获取指定结束时间前的会员数量
     *
     * @param endTime
     * @return
     */
    long memberCount(Date endTime);

    /**
     * 当天活跃会员数量
     *
     * @param startTime
     * @return
     */
    long activeQuantity(Date startTime);

    /**
     * 时间段内新增会员数量
     *
     * @param endTime
     * @param startTime
     * @return
     */
    long newlyAdded(Date endTime, Date startTime);

    /**
     * 根据参数，查询这段时间的会员统计
     *
     * @param statisticsQueryParam
     * @return
     */
    List<MemberStatisticsData> statistics(StatisticsQueryParam statisticsQueryParam);

    /**
     * 查看会员数据分布
     *
     * @return 会员数据分布
     */
    List<MemberDistributionVO> distribution();
}
