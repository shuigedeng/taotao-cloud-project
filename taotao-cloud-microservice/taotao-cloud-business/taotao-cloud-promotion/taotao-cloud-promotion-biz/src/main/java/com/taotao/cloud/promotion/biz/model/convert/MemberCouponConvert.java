package com.taotao.cloud.promotion.biz.model.convert;

import com.taotao.cloud.promotion.api.model.vo.MemberCouponVO;
import com.taotao.cloud.promotion.biz.model.entity.MemberCoupon;
import com.taotao.cloud.sys.api.model.vo.dept.DeptTreeVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberCouponConvert {
	/** 实例 */
	MemberCouponConvert INSTANCE = Mappers.getMapper(MemberCouponConvert.class);

	MemberCouponVO convert(MemberCoupon memberCoupon);
}
