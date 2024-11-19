package com.taotao.cloud.payment.biz.daxpay.single.service.convert.allocation;

import com.taotao.cloud.payment.biz.daxpay.service.entity.allocation.receiver.AllocGroup;
import com.taotao.cloud.payment.biz.daxpay.service.param.allocation.group.AllocGroupParam;
import com.taotao.cloud.payment.biz.daxpay.service.bo.allocation.AllocGroupResultBo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/4/1
 */
@Mapper
public interface AllocGroupConvert {
    AllocGroupConvert CONVERT = Mappers.getMapper(AllocGroupConvert.class);

    AllocGroupResultBo convert(AllocGroup in);

    AllocGroup convert(AllocGroupParam in);
}
