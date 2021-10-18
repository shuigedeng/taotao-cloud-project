package com.taotao.cloud.order.biz.mapper;

import com.taotao.cloud.order.biz.entity.OrderItem;
import com.taotao.cloud.web.base.mapper.BaseSuperMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 部门管理 Mapper 接口
 *
 * @author shuigedeng
 * @since 2020/4/30 11:12
 */
@Mapper
public interface OrderItemMapper extends BaseSuperMapper<OrderItem, Long> {

}
