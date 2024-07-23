package com.taotao.cloud.goods.domain.category.service.impl;

import com.taotao.cloud.goods.domain.category.service.CategoryDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryDomainServiceImpl implements CategoryDomainService {

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
