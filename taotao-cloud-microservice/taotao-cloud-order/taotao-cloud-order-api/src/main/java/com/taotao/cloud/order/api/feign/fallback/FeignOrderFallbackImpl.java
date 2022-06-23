package com.taotao.cloud.order.api.feign.fallback;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.order.api.web.dto.order_info.OrderSaveDTO;
import com.taotao.cloud.order.api.feign.IFeignOrderService;
import com.taotao.cloud.order.api.web.vo.order.OrderDetailVO;
import com.taotao.cloud.order.api.web.vo.order.OrderVO;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignOrderFallbackImpl implements FallbackFactory<IFeignOrderService> {

	@Override
	public IFeignOrderService create(Throwable throwable) {
		return new IFeignOrderService() {
			@Override
			public Result<OrderVO> findOrderInfoByCode(String code) {
				LogUtil.error("调用findOrderInfoByCode异常：{}", throwable, code);
				return Result.fail(null, ResultEnum.ERROR.getCode());
			}

			@Override
			public Result<OrderVO> saveOrder(OrderSaveDTO orderDTO) {
				LogUtil.error("调用saveOrder异常：{}", throwable, orderDTO);
				return Result.fail(null, ResultEnum.ERROR.getCode());
			}

			@Override
			public Result<OrderDetailVO> queryDetail(String sn) {
				return null;
			}

			@Override
			public Result<Boolean> payOrder(String sn, String paymentMethod, String receivableNo) {
				return null;
			}

			@Override
			public Result<OrderVO> getBySn(String sn) {
				return null;
			}

			@Override
			public Result<List<OrderVO>> getByTradeSn(String sn) {
				return null;
			}
		};
	}
}
