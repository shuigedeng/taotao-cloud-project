package com.taotao.cloud.order.api.feign.fallback;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.order.api.dto.OrderDTO;
import com.taotao.cloud.order.api.feign.RemoteOrderService;
import com.taotao.cloud.order.api.vo.OrderVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class RemoteOrderFallbackImpl implements FallbackFactory<RemoteOrderService> {

	@Override
	public RemoteOrderService create(Throwable throwable) {
		return new RemoteOrderService() {
			@Override
			public Result<OrderVO> findOrderInfoByCode(String code) {
				LogUtil.error("调用findOrderInfoByCode异常：{}", throwable, code);
				return Result.fail(null, ResultEnum.ERROR.getCode());
			}

			@Override
			public Result<OrderVO> saveOrder(OrderDTO orderDTO) {
				LogUtil.error("调用saveOrder异常：{}", throwable, orderDTO);
				return Result.fail(null, ResultEnum.ERROR.getCode());
			}
		};
	}
}
