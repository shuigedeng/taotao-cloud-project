package com.taotao.cloud.goods.biz.service.business.impl;

import com.taotao.cloud.goods.biz.mapper.IStudioCommodityMapper;
import com.taotao.cloud.goods.biz.model.entity.StudioCommodity;
import com.taotao.cloud.goods.biz.repository.cls.StudioCommodityRepository;
import com.taotao.cloud.goods.biz.repository.inf.IStudioCommodityRepository;
import com.taotao.cloud.goods.biz.service.business.IStudioCommodityService;
import com.taotao.cloud.web.base.service.impl.BaseSuperServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 直播间-商品关联业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:03:01
 */
@Service
public class StudioCommodityServiceImpl extends
	BaseSuperServiceImpl<IStudioCommodityMapper, StudioCommodity, StudioCommodityRepository, IStudioCommodityRepository, Long> implements IStudioCommodityService {

}
