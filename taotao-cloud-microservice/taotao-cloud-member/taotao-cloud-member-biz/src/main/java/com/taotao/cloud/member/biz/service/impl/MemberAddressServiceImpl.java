package com.taotao.cloud.member.biz.service.impl;

import com.taotao.cloud.member.biz.repository.MemberAddressSuperRepository;
import com.taotao.cloud.member.biz.service.IMemberAddressService;
import org.springframework.stereotype.Service;

/**
 * 会员收货地址服务实现类
 *
 * @author shuigedeng
 * @since 2020-10-16 16:23:49
 * @since 1.0
 */
@Service
public class MemberAddressServiceImpl implements IMemberAddressService {
    private final MemberAddressSuperRepository memberAddressRepository;

	public MemberAddressServiceImpl(
		MemberAddressSuperRepository memberAddressRepository) {
		this.memberAddressRepository = memberAddressRepository;
	}
}
