package com.taotao.cloud.payment.biz.bootx.core.paymodel.union.convert;

import cn.bootx.payment.core.paymodel.union.entity.UnionPayConfig;
import cn.bootx.payment.dto.paymodel.union.UnionPayConfigDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**   
*
* @author xxm  
* @date 2022/3/11 
*/
@Mapper
public interface UnionPayConvert {
    UnionPayConvert CONVERT = Mappers.getMapper(UnionPayConvert.class);

    UnionPayConfigDto convert(UnionPayConfig in);

}
