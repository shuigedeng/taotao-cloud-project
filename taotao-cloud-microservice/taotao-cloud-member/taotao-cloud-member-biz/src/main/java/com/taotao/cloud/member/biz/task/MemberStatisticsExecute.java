package com.taotao.cloud.member.biz.task;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.job.xxl.timetask.EveryDayExecute;
import com.taotao.cloud.report.api.feign.IFeignMemberStatisticsService;
import com.taotao.cloud.report.api.web.dto.MemberStatisticsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * 会员数据统计
 */

@Component
public class MemberStatisticsExecute implements EveryDayExecute {

	/**
	 * 会员统计
	 */
	@Autowired
	private IFeignMemberStatisticsService memberStatisticsService;

	@Override
	public void execute() {
		try {
			//统计的时间（开始。结束时间）
			Date startTime, endTime;
			//初始值
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 1);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			endTime = calendar.getTime();
			//-1天，即为开始时间
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
			startTime = calendar.getTime();

			MemberStatisticsDTO memberStatisticsData = new MemberStatisticsDTO();
			memberStatisticsData.setMemberCount(memberStatisticsService.memberCount(endTime));
			memberStatisticsData.setCreateDate(startTime);
			memberStatisticsData.setActiveQuantity(memberStatisticsService.activeQuantity(startTime));
			memberStatisticsData.setNewlyAdded(memberStatisticsService.newlyAdded(startTime, endTime));
			memberStatisticsService.saveMemberStatistics(memberStatisticsData);
		} catch (Exception e) {
			LogUtils.error("每日会员统计功能异常：", e);
		}
	}
}
