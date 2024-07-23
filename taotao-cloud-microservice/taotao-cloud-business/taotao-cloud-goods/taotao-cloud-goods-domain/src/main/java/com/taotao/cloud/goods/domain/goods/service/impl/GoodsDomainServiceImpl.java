package com.taotao.cloud.goods.domain.goods.service.impl;

import com.taotao.cloud.goods.domain.goods.service.GoodsDomainService;
import com.taotao.cloud.member.domain.member.entity.MemberEntity;
import com.taotao.cloud.member.domain.member.repository.MemberDomainRepository;
import com.taotao.cloud.member.domain.member.service.MemberDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GoodsDomainServiceImpl implements GoodsDomainService {

	private MemberDomainRepository deptDomainRepository;

	@Override
	public void create(MemberEntity dept) {

	}

	@Override
	public void modify(MemberEntity dept) {

	}

	@Override
	public void remove(Long[] ids) {

	}
}
