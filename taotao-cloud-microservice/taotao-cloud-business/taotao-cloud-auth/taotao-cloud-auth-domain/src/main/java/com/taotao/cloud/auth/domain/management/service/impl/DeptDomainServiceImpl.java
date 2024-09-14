package com.taotao.cloud.auth.domain.management.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.auth.domain.management.entity.DeptEntity;
import com.taotao.cloud.auth.domain.management.repository.DeptRepository;
import com.taotao.cloud.auth.domain.management.service.DeptDomainService;
import com.taotao.boot.common.model.PageQuery;

public class DeptDomainServiceImpl implements DeptDomainService {

	private DeptRepository deptRepository;

	@Override
	public Boolean insert(DeptEntity deptEntity) {
		return null;
	}

	@Override
	public Boolean update(DeptEntity deptEntity) {
		return null;
	}

	@Override
	public DeptEntity getById(Long id) {
		return null;
	}

	@Override
	public Boolean deleteById(Long id) {
		return null;
	}

	@Override
	public IPage<DeptEntity> list(DeptEntity deptEntity, PageQuery pageQuery) {
//		return deptRepository.list(deptEntity, pageQuery);
		return null;
	}
}
