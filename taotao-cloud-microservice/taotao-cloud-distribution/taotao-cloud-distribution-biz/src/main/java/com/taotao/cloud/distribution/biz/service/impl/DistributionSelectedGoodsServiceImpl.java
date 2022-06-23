package com.taotao.cloud.distribution.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.distribution.biz.model.entity.DistributionSelectedGoods;
import com.taotao.cloud.distribution.biz.mapper.DistributionSelectedGoodsMapper;
import com.taotao.cloud.distribution.biz.service.DistributionSelectedGoodsService;
import com.taotao.cloud.distribution.biz.service.DistributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 分销选择商品接口实现
 */
@Service
public class DistributionSelectedGoodsServiceImpl extends ServiceImpl<DistributionSelectedGoodsMapper, DistributionSelectedGoods> implements
	DistributionSelectedGoodsService {

    /**
     * 分销员
     */
    @Autowired
    private DistributionService distributionService;

    @Override
    public boolean add(String distributionGoodsId) {
        //检查分销功能开关
        distributionService.checkDistributionSetting();

        String distributionId = distributionService.getDistribution().getId();
        DistributionSelectedGoods distributionSelectedGoods = new DistributionSelectedGoods(distributionId, distributionGoodsId);
        return this.save(distributionSelectedGoods);
    }

    @Override
    public boolean delete(String distributionGoodsId) {
        //检查分销功能开关
        distributionService.checkDistributionSetting();

        String distributionId = distributionService.getDistribution().getId();
        return this.remove(new LambdaQueryWrapper<DistributionSelectedGoods>()
                .eq(DistributionSelectedGoods::getDistributionGoodsId, distributionGoodsId)
                .eq(DistributionSelectedGoods::getDistributionId, distributionId));
    }

    @Override
    public boolean deleteByDistributionGoodsId(String distributionGoodsId) {
        return this.remove(new LambdaQueryWrapper<DistributionSelectedGoods>()
                .eq(DistributionSelectedGoods::getDistributionGoodsId, distributionGoodsId));
    }
}
