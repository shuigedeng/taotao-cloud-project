package com.taotao.cloud.payment.biz.infrastructure.daxpay.single.service.dao.order.allocation;

import com.github.yulichang.base.MPJBaseMapper;
import com.taotao.cloud.payment.biz.daxpay.service.entity.order.allocation.AllocOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 * @author xxm
 * @since 2024/4/7
 */
@Mapper
public interface AllocOrderMapper extends MPJBaseMapper<AllocOrder> {
}
