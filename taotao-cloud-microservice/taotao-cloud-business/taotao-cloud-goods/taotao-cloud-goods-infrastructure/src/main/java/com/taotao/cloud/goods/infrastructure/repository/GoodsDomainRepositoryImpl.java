package com.taotao.cloud.goods.infrastructure.repository;

import com.taotao.boot.data.datasource.tx.TransactionalUtil;
import com.taotao.boot.data.mybatis.utils.MybatisUtil;
import com.taotao.cloud.goods.domain.goods.entity.GoodsEntity;
import com.taotao.cloud.goods.domain.goods.repository.GoodsDomainRepository;
import com.taotao.cloud.goods.infrastructure.persistent.mapper.IGoodsMapper;
import com.taotao.cloud.goods.infrastructure.persistent.mapper.IGoodsSkuMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GoodsDomainRepositoryImpl implements GoodsDomainRepository {


	private final TransactionalUtil transactionalUtil;
	private final MybatisUtil mybatisUtil;
	private final IGoodsMapper goodsMapper;
	private final IGoodsSkuMapper goodsSkuMapper;


	@Override
	public void create(GoodsEntity dept) {

	}

	@Override
	public void modify(GoodsEntity dept) {

	}

	@Override
	public void remove(Long[] ids) {

	}
}
