package com.taotao.cloud.order.api.feign.fallback;

import com.taotao.cloud.order.api.enums.order.CommentStatusEnum;
import com.taotao.cloud.order.api.feign.IFeignOrderItemApi;
import com.taotao.cloud.order.api.model.dto.order_item.OrderItemSaveDTO;
import com.taotao.cloud.order.api.model.vo.order.OrderItemVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignOrderItemApiFallback implements FallbackFactory<IFeignOrderItemApi> {
	@Override
	public IFeignOrderItemApi create(Throwable throwable) {
		return new IFeignOrderItemApi() {
			@Override
			public Boolean saveOrderItem(OrderItemSaveDTO orderItemSaveDTO) {
				return null;
			}

			@Override
			public Boolean updateById(OrderItemVO orderItem) {
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
			public Boolean updateCommentStatus(String sn, CommentStatusEnum finished) {
				return null;
			}
		};
	}
}
