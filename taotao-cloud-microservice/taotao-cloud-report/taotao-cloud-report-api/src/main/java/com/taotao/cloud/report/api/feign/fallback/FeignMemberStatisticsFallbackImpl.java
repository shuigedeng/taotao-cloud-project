package com.taotao.cloud.report.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.report.api.web.dto.MemberStatisticsDTO;
import com.taotao.cloud.report.api.feign.IFeignMemberStatisticsService;
import com.taotao.cloud.report.api.web.vo.MemberStatisticsVO;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.Date;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignMemberStatisticsFallbackImpl implements FallbackFactory<IFeignMemberStatisticsService> {

	@Override
	public IFeignMemberStatisticsService create(Throwable throwable) {
		return new IFeignMemberStatisticsService() {

			@Override
			public Result<MemberStatisticsVO> findMemberStatistics() {
				return null;
			}

			@Override
			public Result<Boolean> saveMemberStatistics(MemberStatisticsDTO memberStatisticsDTO) {
				return null;
			}

			@Override
			public Result<Long> newlyAdded(Date startTime, Date endTime) {
				return null;
			}

			@Override
			public Result<Long> activeQuantity(Date startTime) {
				return null;
			}

			@Override
			public Result<Long> memberCount(Date endTime) {
				return null;
			}
		};
	}
}
