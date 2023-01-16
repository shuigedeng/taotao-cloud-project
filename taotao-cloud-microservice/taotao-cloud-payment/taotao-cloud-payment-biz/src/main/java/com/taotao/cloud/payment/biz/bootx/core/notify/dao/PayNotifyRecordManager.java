package com.taotao.cloud.payment.biz.bootx.core.notify.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.payment.biz.bootx.core.notify.entity.PayNotifyRecord;
import com.taotao.cloud.payment.biz.bootx.dto.notify.PayNotifyRecordDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Objects;

/**
 * 支付消息通知回调
 * @author xxm
 * @date 2021/6/22
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayNotifyRecordManager extends BaseManager<PayNotifyRecordMapper, PayNotifyRecord> {

    public Page<PayNotifyRecord> page(PageQuery PageQuery, PayNotifyRecordDto param){
        Page<PayNotifyRecord> mpPage = MpUtil.getMpPage(PageQuery, PayNotifyRecord.class);
        return lambdaQuery()
                .orderByDesc(MpBaseEntity::getId)
                .like(Objects.nonNull(param.getPaymentId()),PayNotifyRecord::getPaymentId,param.getPaymentId())
                .eq(Objects.nonNull(param.getPayChannel()),PayNotifyRecord::getPayChannel,param.getPayChannel())
                .eq(Objects.nonNull(param.getStatus()),PayNotifyRecord::getStatus,param.getStatus())
                .page(mpPage);
    }
}
