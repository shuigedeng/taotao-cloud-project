package com.taotao.cloud.goods.biz.service.business.impl;

import com.taotao.cloud.goods.biz.mapper.IGoodsUnitMapper;
import com.taotao.cloud.goods.biz.model.entity.GoodsUnit;
import com.taotao.cloud.goods.biz.repository.cls.GoodsUnitRepository;
import com.taotao.cloud.goods.biz.repository.inf.IGoodsUnitRepository;
import com.taotao.cloud.goods.biz.service.business.IGoodsUnitService;
import com.taotao.cloud.web.base.service.impl.BaseSuperServiceImpl;
import org.springframework.stereotype.Service;


/**
 * 计量单位业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:02:47
 */
@Service
public class GoodsUnitServiceImpl extends
	BaseSuperServiceImpl<IGoodsUnitMapper, GoodsUnit, GoodsUnitRepository, IGoodsUnitRepository, Long> implements
	IGoodsUnitService {

}
