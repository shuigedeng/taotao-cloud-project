package com.taotao.cloud.payment.biz.daxpay.single.service.core.order.reconcile.conver;

import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.reconcile.entity.PayReconcileDetail;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.reconcile.entity.PayReconcileOrder;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.order.reconcile.PayReconcileDetailDto;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.order.reconcile.PayReconcileOrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/1/22
 */
@Mapper
public interface PayReconcileConvert {
    PayReconcileConvert CONVERT = Mappers.getMapper(PayReconcileConvert.class);

    PayReconcileDetailDto convert(PayReconcileDetail in);

    PayReconcileOrderDto convert(PayReconcileOrder in);
}
