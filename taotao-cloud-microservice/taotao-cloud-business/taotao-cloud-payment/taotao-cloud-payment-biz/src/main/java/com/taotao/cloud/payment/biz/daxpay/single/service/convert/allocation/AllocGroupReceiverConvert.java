package com.taotao.cloud.payment.biz.daxpay.single.service.convert.allocation;

import com.taotao.cloud.payment.biz.daxpay.service.bo.allocation.AllocGroupReceiverResultBo;
import com.taotao.cloud.payment.biz.daxpay.service.entity.allocation.receiver.AllocGroupReceiver;
import com.taotao.cloud.payment.biz.daxpay.service.param.allocation.group.AllocGroupReceiverParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/4/1
 */
@Mapper
public interface AllocGroupReceiverConvert {
    AllocGroupReceiverConvert CONVERT = Mappers.getMapper(AllocGroupReceiverConvert.class);

    AllocGroupReceiverResultBo convert(AllocGroupReceiver in);

    AllocGroupReceiver convert(AllocGroupReceiverParam in);
}
