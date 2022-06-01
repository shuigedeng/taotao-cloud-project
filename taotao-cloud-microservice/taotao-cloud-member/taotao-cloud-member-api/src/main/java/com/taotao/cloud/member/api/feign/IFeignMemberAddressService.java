package com.taotao.cloud.member.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.member.api.feign.fallback.FeignMemberServiceFallback;
import com.taotao.cloud.member.api.vo.MemberAddressVO;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = ServiceName.TAOTAO_CLOUD_MEMBER_CENTER, fallbackFactory = FeignMemberServiceFallback.class)
public interface IFeignMemberAddressService {

	MemberAddressVO getById(String shippingAddressId);

}
