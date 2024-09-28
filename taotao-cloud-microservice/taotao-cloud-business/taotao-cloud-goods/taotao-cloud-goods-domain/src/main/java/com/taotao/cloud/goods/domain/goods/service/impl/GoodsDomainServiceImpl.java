package com.taotao.cloud.goods.domain.goods.service.impl;

import com.taotao.cloud.goods.domain.goods.entity.GoodsEntity;
import com.taotao.cloud.goods.domain.goods.repository.GoodsDomainRepository;
import com.taotao.cloud.goods.domain.goods.service.GoodsDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GoodsDomainServiceImpl implements GoodsDomainService {

	private GoodsDomainRepository deptDomainRepository;

	@Override
	public void create(GoodsEntity dept) {

	}

	//@Override
	//public void modify(GoodsEntity dept) {
	//
	//}

	@Override
	public void remove(Long[] ids) {

	}
}
