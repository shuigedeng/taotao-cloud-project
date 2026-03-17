package com.taotao.cloud.payment.biz.infrastructure.daxpay.single.service.dao.reconcile;

import com.github.yulichang.base.MPJBaseMapper;
import com.taotao.cloud.payment.biz.daxpay.service.entity.reconcile.ReconcileDiscrepancy;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 * @author xxm
 * @since 2024/8/5
 */
@Mapper
public interface ReconcileDiscrepancyMapper extends MPJBaseMapper<ReconcileDiscrepancy> {
}
