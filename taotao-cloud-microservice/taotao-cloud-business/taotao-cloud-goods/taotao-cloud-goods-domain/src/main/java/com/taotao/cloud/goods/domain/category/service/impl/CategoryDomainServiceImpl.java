package com.taotao.cloud.goods.domain.category.service.impl;

import com.taotao.cloud.goods.domain.category.entity.CategoryEntity;
import com.taotao.cloud.goods.domain.category.repository.CategoryDomainRepository;
import com.taotao.cloud.goods.domain.category.service.CategoryDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryDomainServiceImpl implements CategoryDomainService {

	private CategoryDomainRepository categoryDomainRepository;

	@Override
	public void create(CategoryEntity dept) {

	}

	@Override
	public void modify(CategoryEntity dept) {

	}

	@Override
	public void remove(Long[] ids) {

	}
}
