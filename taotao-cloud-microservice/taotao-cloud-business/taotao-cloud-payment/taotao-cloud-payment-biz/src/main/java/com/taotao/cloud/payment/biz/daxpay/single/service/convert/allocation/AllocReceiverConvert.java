package com.taotao.cloud.payment.biz.daxpay.single.service.convert.allocation;

import com.taotao.cloud.payment.biz.daxpay.core.param.allocation.receiver.AllocReceiverAddParam;
import com.taotao.cloud.payment.biz.daxpay.core.result.allocation.receiver.AllocReceiverResult;
import com.taotao.cloud.payment.biz.daxpay.service.bo.allocation.AllocReceiverResultBo;
import com.taotao.cloud.payment.biz.daxpay.service.entity.allocation.receiver.AllocReceiver;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 *
 * @author xxm
 * @since 2024/3/28
 */
@Mapper
public interface AllocReceiverConvert {
    AllocReceiverConvert CONVERT = Mappers.getMapper(AllocReceiverConvert.class);

    AllocReceiver convert(AllocReceiverAddParam in);

    AllocReceiverResultBo toBo(AllocReceiver in);

    AllocReceiverResult toResult(AllocReceiver in);

    List<AllocReceiverResult.Receiver> toList(List<AllocReceiver> in);
}
