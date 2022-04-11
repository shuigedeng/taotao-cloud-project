package com.taotao.cloud.sys.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.sys.api.feign.IFeignLogisticsService;
import com.taotao.cloud.sys.api.feign.IFeignMenuService;
import com.taotao.cloud.sys.api.vo.logistics.LogisticsVO;
import com.taotao.cloud.sys.api.vo.logistics.TracesVO;
import com.taotao.cloud.sys.api.vo.menu.MenuQueryVO;
import java.util.List;
import java.util.Set;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignLogisticsFallback implements FallbackFactory<IFeignLogisticsService> {
	@Override
	public IFeignLogisticsService create(Throwable throwable) {
		return new IFeignLogisticsService() {

			@Override
			public Result<LogisticsVO> getById(
				String logisticsId) {
				return null;
			}

			@Override
			public Result<TracesVO> getLogistic(
				Long logisticsId, String logisticsNo) {
				return null;
			}
		};
	}
}
