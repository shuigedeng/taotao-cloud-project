package com.taotao.cloud.member.domain.wallet.service.impl;

import com.taotao.cloud.member.domain.wallet.entity.MemberWalletEntity;
import com.taotao.cloud.member.domain.wallet.repository.MemberWalletDomainRepository;
import com.taotao.cloud.member.domain.wallet.service.MemberWalletDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MemberWalletDomainServiceImpl implements MemberWalletDomainService {

	private MemberWalletDomainRepository memberWalletDomainRepository;

	@Override
	public void create(MemberWalletEntity dept) {

	}

	@Override
	public void modify(MemberWalletEntity dept) {

	}

	@Override
	public void remove(Long[] ids) {

	}
}
