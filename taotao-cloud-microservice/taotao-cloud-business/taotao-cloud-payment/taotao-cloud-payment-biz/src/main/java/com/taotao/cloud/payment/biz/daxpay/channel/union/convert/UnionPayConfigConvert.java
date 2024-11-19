package com.taotao.cloud.payment.biz.daxpay.channel.union.convert;

import com.taotao.cloud.payment.biz.daxpay.channel.union.entity.config.UnionPayConfig;
import com.taotao.cloud.payment.biz.daxpay.channel.union.param.config.UnionPayConfigParam;
import com.taotao.cloud.payment.biz.daxpay.channel.union.result.UnionPayConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/9/6
 */
@Mapper
public interface UnionPayConfigConvert {

    UnionPayConfigConvert CONVERT = Mappers.getMapper(UnionPayConfigConvert.class);

    UnionPayConfigResult toResult(UnionPayConfig unionPayConfig);

    UnionPayConfig toEntity(UnionPayConfigParam param);

    UnionPayConfig copy(UnionPayConfig unionPayConfig);


}
