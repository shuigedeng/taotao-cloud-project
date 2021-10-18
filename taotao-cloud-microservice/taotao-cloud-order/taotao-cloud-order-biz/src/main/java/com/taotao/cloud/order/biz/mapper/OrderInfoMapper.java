package com.taotao.cloud.order.biz.mapper;

import com.taotao.cloud.order.biz.entity.OrderInfo;
import com.taotao.cloud.web.base.mapper.BaseSuperMapper;
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
}
