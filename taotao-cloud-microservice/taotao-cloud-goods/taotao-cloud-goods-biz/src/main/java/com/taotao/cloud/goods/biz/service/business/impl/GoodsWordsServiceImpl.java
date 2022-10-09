package com.taotao.cloud.goods.biz.service.business.impl;

import com.taotao.cloud.goods.biz.mapper.IGoodsWordsMapper;
import com.taotao.cloud.goods.biz.model.entity.GoodsWords;
import com.taotao.cloud.goods.biz.repository.cls.GoodsWordsRepository;
import com.taotao.cloud.goods.biz.repository.inf.IGoodsWordsRepository;
import com.taotao.cloud.goods.biz.service.business.IGoodsWordsService;
import com.taotao.cloud.web.base.service.impl.BaseSuperServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 商品关键字业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:02:50
 */
@Service
public class GoodsWordsServiceImpl extends
	BaseSuperServiceImpl<IGoodsWordsMapper, GoodsWords, GoodsWordsRepository, IGoodsWordsRepository, Long> implements
	IGoodsWordsService {

}
