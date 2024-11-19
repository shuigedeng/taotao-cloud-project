package com.taotao.cloud.payment.biz.daxpay.single.service.convert.reconcile;

import com.taotao.cloud.payment.biz.daxpay.service.bo.reconcile.ChannelReconcileTradeBo;
import com.taotao.cloud.payment.biz.daxpay.service.entity.reconcile.ChannelReconcileTrade;
import com.taotao.cloud.payment.biz.daxpay.service.entity.reconcile.ReconcileDiscrepancy;
import com.taotao.cloud.payment.biz.daxpay.service.entity.reconcile.ReconcileStatement;
import com.taotao.cloud.payment.biz.daxpay.service.result.reconcile.ReconcileDiscrepancyResult;
import com.taotao.cloud.payment.biz.daxpay.service.result.reconcile.ReconcileStatementResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 对账转换类
 * @author xxm
 * @since 2024/8/6
 */
@Mapper
public interface ReconcileConvert {
    ReconcileConvert CONVERT = Mappers.getMapper(ReconcileConvert.class);

    ReconcileStatementResult toResult(ReconcileStatement in);

    ReconcileDiscrepancyResult toResult(ReconcileDiscrepancy in);

    ChannelReconcileTrade toEntity(ChannelReconcileTradeBo in);

    List<ChannelReconcileTrade> toList(List<ChannelReconcileTradeBo> in);

}
