package com.taotao.cloud.order.biz.mapper;

import com.taotao.cloud.order.api.OrderDO;
import com.taotao.cloud.order.api.OrderQueryDO;
import com.taotao.cloud.order.biz.entity.OrderInfo;
import com.taotao.cloud.web.base.mapper.BaseSuperMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 部门管理 Mapper 接口
 *
 * @author shuigedeng
 * @since 2020/4/30 11:12
 */
@Mapper
public interface OrderInfoMapper extends BaseSuperMapper<OrderInfo, Long> {

	@Select({"""
		select * from order_info where id = #{id}
		"""})
	OrderInfo getUserById(@Param("id") Long id);

	@Select({"""
		select o.member_id as memberId,
			   o.code as code,
			   o.main_status as mainStatus,
			   o.child_status as childStatus,
			   o.amount as amount,
			   o.receiver_name as receiverName
		from order_info o where o.code = #{code}
		"""})
	List<OrderDO> findOrderInfoByBo(@Param("code") String code);
}
