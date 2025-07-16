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

package com.taotao.cloud.bff.api.service.miniapp.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.bff.api.util.StatisticsDateUtil;
import com.taotao.cloud.report.api.enums.SearchTypeEnum;
import com.taotao.cloud.report.api.model.dto.StatisticsQueryParam;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;

/** 会员统计业务层实现 */
@Service
public class MemberStatisticsServiceImpl
        extends ServiceImpl<MemberStatisticsMapper, MemberStatisticsData>
        implements MemberStatisticsService {

    @Override
    public long getMemberCount() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("disabled", true);
        return this.baseMapper.customSqlQuery(queryWrapper);
    }

    @Override
    public long todayMemberNum() {
        QueryWrapper queryWrapper = Wrappers.query();
        queryWrapper.ge("create_time", DateUtil.beginOfDay(new Date()));
        return this.baseMapper.customSqlQuery(queryWrapper);
    }

    @Override
    public long memberCount(Date endTime) {
        QueryWrapper queryWrapper = Wrappers.query();
        queryWrapper.le("create_time", endTime);
        return this.baseMapper.customSqlQuery(queryWrapper);
    }

    @Override
    public long activeQuantity(Date startTime) {

        QueryWrapper queryWrapper = Wrappers.query();
        queryWrapper.ge("last_login_date", startTime);
        return this.baseMapper.customSqlQuery(queryWrapper);
    }

    @Override
    public long newlyAdded(Date startTime, Date endTime) {
        QueryWrapper queryWrapper = Wrappers.query();
        queryWrapper.between("create_time", startTime, endTime);
        return this.baseMapper.customSqlQuery(queryWrapper);
    }

    @Override
    public List<MemberStatisticsData> statistics(StatisticsQueryParam statisticsQueryParam) {

        Date[] dates = StatisticsDateUtil.getDateArray(statisticsQueryParam);
        Date startTime = dates[0];
        Date endTime = dates[1];

        // 如果统计今天，则自行构造数据
        if (statisticsQueryParam.getSearchType().equals(SearchTypeEnum.TODAY.name())) {
            // 构建数据，然后返回集合，提供给前端展示
            MemberStatisticsData memberStatisticsData = new MemberStatisticsData();
            memberStatisticsData.setMemberCount(this.memberCount(endTime));
            memberStatisticsData.setCreateDate(startTime);
            memberStatisticsData.setActiveQuantity(this.activeQuantity(startTime));
            memberStatisticsData.setNewlyAdded(this.newlyAdded(startTime, endTime));
            List result = new ArrayList<MemberStatisticsData>();
            result.add(memberStatisticsData);
            return result;
        }

        QueryWrapper queryWrapper = Wrappers.query();
        queryWrapper.between("create_date", startTime, endTime);

        return list(queryWrapper);
    }

    @Override
    public List<MemberDistributionVO> distribution() {
        return this.baseMapper.distribution();
    }
}
