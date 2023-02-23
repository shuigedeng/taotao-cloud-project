package com.taotao.cloud.file.api.feign.fallback;

import com.taotao.cloud.file.api.feign.IFeignMemberStatisticsApi;
import com.taotao.cloud.file.api.model.dto.MemberStatisticsDTO;
import com.taotao.cloud.file.api.model.vo.MemberStatisticsVO;
import java.util.Date;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignMemberStatisticsFallbackImpl implements FallbackFactory<IFeignMemberStatisticsApi> {

	@Override
	public IFeignMemberStatisticsApi create(Throwable throwable) {
		return new IFeignMemberStatisticsApi() {
			@Override
			public MemberStatisticsVO findMemberStatistics() {
				return null;
			}

			@Override
			public Boolean saveMemberStatistics(MemberStatisticsDTO memberStatisticsDTO) {
				return null;
			}

			@Override
			public Long newlyAdded(Date startTime, Date endTime) {
				return null;
			}

			@Override
			public Long activeQuantity(Date startTime) {
				return null;
			}

			@Override
			public Long memberCount(Date endTime) {
				return null;
			}
		};
	}
}
