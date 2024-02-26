package com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.union.convert;

import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.union.entity.UnionPayConfig;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.channel.union.UnionPayConfigDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xxm
 * @since 2022/3/11
 */
@Mapper
public interface UnionPayConvert {

    UnionPayConvert CONVERT = Mappers.getMapper(UnionPayConvert.class);

    UnionPayConfigDto convert(UnionPayConfig in);

}
