package com.taotao.cloud.order.api.feign.fallback;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.order.api.dto.order_item.OrderItemSaveDTO;
import com.taotao.cloud.order.api.feign.RemoteOrderItemService;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class RemoteOrderItemFallbackImpl implements FallbackFactory<RemoteOrderItemService> {
	@Override
	public RemoteOrderItemService create(Throwable throwable) {
		return new RemoteOrderItemService() {
			@Override
			public Result<Boolean> saveOrderItem(OrderItemSaveDTO orderItemSaveDTO) {
				LogUtil.error("调用saveOrderItem异常：{0}", throwable, orderItemSaveDTO);
				return Result.fail(null, ResultEnum.ERROR.getCode());
			}
		};
	}
}
