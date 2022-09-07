package com.taotao.cloud.order.biz.mapper.order;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.order.api.model.vo.order.OrderSimpleVO;
import com.taotao.cloud.order.biz.model.entity.order.OrderItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 子订单数据处理层
 */
public interface IOrderItemMapper extends BaseSuperMapper<OrderItem> {

	/**
	 * 获取等待操作订单子项目
	 *
	 * @param queryWrapper 查询条件
	 * @return 订单子项列表
	 */
	@Select("""
		SELECT *
		FROM tt_order_item AS oi INNER JOIN tt_order AS o ON oi.order_sn=o.sn
		${ew.customSqlSegment}
		""")
	List<OrderItem> waitOperationOrderItem(
		@Param(Constants.WRAPPER) Wrapper<OrderSimpleVO> queryWrapper);
}
