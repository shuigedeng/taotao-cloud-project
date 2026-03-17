package com.taotao.cloud.promotion.biz.domain.model.convert;

import com.taotao.cloud.promotion.api.model.vo.MemberCouponVO;
import com.taotao.cloud.promotion.biz.domain.model.entity.MemberCoupon;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberCouponConvert {
	/** 实例 */
	MemberCouponConvert INSTANCE = Mappers.getMapper(MemberCouponConvert.class);

	MemberCouponVO convert( MemberCoupon memberCoupon);
}
