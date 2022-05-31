package com.taotao.cloud.report.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.product.api.feign.fallback.RemoteProductFallbackImpl;
import com.taotao.cloud.report.api.dto.MemberStatisticsDTO;
import com.taotao.cloud.report.api.vo.MemberStatisticsVO;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.Date;

/**
 * 远程调用订单模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(contextId = "RemoteProductService", value = ServiceName.TAOTAO_CLOUD_GOODS, fallbackFactory = RemoteProductFallbackImpl.class)
public interface IFeignMemberStatisticsService {

	Result<MemberStatisticsVO> findMemberStatistics();

	Result<Boolean> saveMemberStatistics(MemberStatisticsDTO memberStatisticsDTO);

	Long newlyAdded(Date startTime, Date endTime);

	Long activeQuantity(Date startTime);

	Long memberCount(Date endTime);

}

