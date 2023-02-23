package com.taotao.cloud.file.biz.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.report.api.enums.SearchTypeEnum;
import com.taotao.cloud.report.api.model.dto.StatisticsQueryParam;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 会员统计业务层实现
 */
@Service
public class MemberStatisticsServiceImpl extends
		ServiceImpl<MemberStatisticsMapper, MemberStatisticsData> implements
		MemberStatisticsService {

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

		//如果统计今天，则自行构造数据
		if (statisticsQueryParam.getSearchType().equals(SearchTypeEnum.TODAY.name())) {
			//构建数据，然后返回集合，提供给前端展示
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
