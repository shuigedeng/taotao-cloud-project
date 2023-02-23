package com.taotao.cloud.generator.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.generator.api.feign.fallback.FeignMemberStatisticsFallbackImpl;
import com.taotao.cloud.generator.api.model.dto.MemberStatisticsDTO;
import com.taotao.cloud.generator.api.model.vo.MemberStatisticsVO;
import java.util.Date;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 远程调用订单模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(contextId = "RemoteProductService", value = ServiceName.TAOTAO_CLOUD_GOODS, fallbackFactory = FeignMemberStatisticsFallbackImpl.class)
public interface IFeignMemberStatisticsApi {
	@PostMapping(value = "/order")
	MemberStatisticsVO findMemberStatistics();

	@PostMapping(value = "/order")
	Boolean saveMemberStatistics(MemberStatisticsDTO memberStatisticsDTO);

	@PostMapping(value = "/order")
	Long newlyAdded(Date startTime, Date endTime);

	@PostMapping(value = "/order")
	Long activeQuantity(Date startTime);

	@PostMapping(value = "/order")
	Long memberCount(Date endTime);

}

