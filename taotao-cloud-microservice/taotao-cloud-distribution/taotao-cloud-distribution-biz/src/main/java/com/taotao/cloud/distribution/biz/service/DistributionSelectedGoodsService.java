package com.taotao.cloud.distribution.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.distribution.biz.model.entity.DistributionSelectedGoods;

/**
 * 分销选择商品业务层
 */
public interface DistributionSelectedGoodsService extends IService<DistributionSelectedGoods> {

    /**
     * 分销员添加分销商品
     * @param distributionGoodsId 分销商品ID
     * @return 是否添加成功
     */
    boolean add(String distributionGoodsId);

    /**
     * 分销员删除分销商品
     * @param distributionGoodsId 分销商品ID
     * @return 是否删除成功
     */
    boolean delete(String distributionGoodsId);

    /**
     * 分销员删除分销商品（管理员操作）
     * @param distributionGoodsId 分销商品ID
     * @return 是否删除成功
     */
    boolean deleteByDistributionGoodsId(String distributionGoodsId);
}
