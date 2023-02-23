package com.taotao.cloud.sys.api.feign.fallback;

import com.taotao.cloud.sys.api.feign.IFeignLogisticsApi;
import com.taotao.cloud.sys.api.model.vo.logistics.LogisticsVO;
import com.taotao.cloud.sys.api.model.vo.logistics.TracesVO;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignLogisticsApiFallback implements FallbackFactory<IFeignLogisticsApi> {
	@Override
	public IFeignLogisticsApi create(Throwable throwable) {
		return new IFeignLogisticsApi() {

			@Override
			public LogisticsVO getById(Long logisticsId) {
				return null;
			}

			@Override
			public TracesVO getLogistic(Long logisticsId, String logisticsNo) {
				return null;
			}

			@Override
			public List<LogisticsVO> list() {
				return null;
			}
		};
	}
}
