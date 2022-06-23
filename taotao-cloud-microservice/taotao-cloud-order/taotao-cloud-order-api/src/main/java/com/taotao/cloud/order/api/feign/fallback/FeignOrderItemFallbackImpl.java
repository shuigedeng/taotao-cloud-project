package com.taotao.cloud.order.api.feign.fallback;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.order.api.web.dto.order_item.OrderItemSaveDTO;
import com.taotao.cloud.order.api.enums.order.CommentStatusEnum;
import com.taotao.cloud.order.api.feign.IFeignOrderItemService;
import com.taotao.cloud.order.api.web.vo.order.OrderItemVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignOrderItemFallbackImpl implements FallbackFactory<IFeignOrderItemService> {
	@Override
	public IFeignOrderItemService create(Throwable throwable) {
		return new IFeignOrderItemService() {
			@Override
			public Result<Boolean> saveOrderItem(OrderItemSaveDTO orderItemSaveDTO) {
				LogUtil.error("调用saveOrderItem异常：{0}", throwable, orderItemSaveDTO);
				return Result.fail(null, ResultEnum.ERROR.getCode());
			}

			@Override
			public Result<Boolean> updateById(OrderItemVO orderItem) {
				return null;
			}

			@Override
			public OrderItemVO getByOrderSnAndSkuId(String orderSn, String skuId) {
				return null;
			}

			@Override
			public OrderItemVO getBySn(String orderItemSn) {
				return null;
			}

			@Override
			public Result<Boolean> updateCommentStatus(String sn, CommentStatusEnum finished) {
				return null;
			}
		};
	}
}
