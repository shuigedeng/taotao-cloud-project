package com.taotao.cloud.sys.domain.dict.service.impl;

import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.sys.domain.dict.entity.Dict;
import com.taotao.cloud.sys.domain.dict.repository.DictRepository;
import com.taotao.cloud.sys.domain.dict.service.DictDomainService;

public class DictDomainServiceImpl implements DictDomainService {

	private DictRepository dictRepository;

	@Override
	public Boolean insert(Dict dict) {
		return null;
	}

	@Override
	public Boolean update(Dict dict) {
		return null;
	}

	@Override
	public Dict getById(Long id) {
		return null;
	}

	@Override
	public Boolean deleteById(Long id) {
		return null;
	}

	@Override
	public Datas<Dict> list(Dict dict, PageQuery pageQuery) {
		return dictRepository.list(dict, pageQuery);
	}
}
