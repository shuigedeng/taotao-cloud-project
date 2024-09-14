package com.taotao.cloud.sys.domain.dict.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.PageQuery;
import com.taotao.cloud.sys.domain.dict.entity.DictEntity;
import com.taotao.cloud.sys.domain.dict.repository.DictRepository;
import com.taotao.cloud.sys.domain.dict.service.DictDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DictDomainServiceImpl implements DictDomainService {

	private DictRepository dictRepository;

	@Override
	public Boolean insert(DictEntity dictEntity) {
		return null;
	}

	@Override
	public Boolean update(DictEntity dictEntity) {
		return null;
	}

	@Override
	public DictEntity getById(Long id) {
		return null;
	}

	@Override
	public Boolean deleteById(Long id) {
		return null;
	}

	@Override
	public IPage<DictEntity> list(DictEntity dictEntity, PageQuery pageQuery) {
//		return dictRepository.list(dictEntity, pageQuery);
		return null;
	}
}
