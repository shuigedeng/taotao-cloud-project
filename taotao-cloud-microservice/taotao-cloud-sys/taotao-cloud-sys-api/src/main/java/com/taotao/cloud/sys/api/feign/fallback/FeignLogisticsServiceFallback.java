package com.taotao.cloud.sys.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.sys.api.feign.IFeignLogisticsService;
import com.taotao.cloud.sys.api.model.vo.logistics.LogisticsVO;
import com.taotao.cloud.sys.api.model.vo.logistics.TracesVO;
import java.util.List;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignLogisticsServiceFallback implements FallbackFactory<IFeignLogisticsService> {
	@Override
	public IFeignLogisticsService create(Throwable throwable) {
		return new IFeignLogisticsService() {

			@Override
			public Result<LogisticsVO> getById(Long logisticsId) {
				return null;
			}

			@Override
			public Result<TracesVO> getLogistic(Long logisticsId, String logisticsNo) {
				return null;
			}

			@Override
			public List<LogisticsVO> list() {
				return null;
			}
		};
	}
}
