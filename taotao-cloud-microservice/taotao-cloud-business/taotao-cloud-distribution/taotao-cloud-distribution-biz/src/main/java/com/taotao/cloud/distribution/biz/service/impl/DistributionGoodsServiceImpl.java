/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.distribution.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.cloud.distribution.api.model.query.DistributionGoodsPageQuery;
import com.taotao.cloud.distribution.api.model.vo.DistributionGoodsVO;
import com.taotao.cloud.distribution.biz.mapper.DistributionGoodsMapper;
import com.taotao.cloud.distribution.biz.model.entity.Distribution;
import com.taotao.cloud.distribution.biz.model.entity.DistributionGoods;
import com.taotao.cloud.distribution.biz.service.DistributionGoodsService;
import com.taotao.cloud.distribution.biz.service.DistributionService;
import com.taotao.cloud.goods.api.feign.GoodsSkuApi;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 分销商品接口实现 */
@Service
public class DistributionGoodsServiceImpl extends ServiceImpl<DistributionGoodsMapper, DistributionGoods>
        implements DistributionGoodsService {

    /** 分销员 */
    @Autowired
    private DistributionService distributionService;
    /** 规格商品 */
    @Autowired
    private GoodsSkuApi goodsSkuApi;

    @Override
    public IPage<DistributionGoodsVO> goodsPage(DistributionGoodsPageQuery searchParams) {
        // 获取商家的分销商品列表
        if (Objects.requireNonNull(UserContext.getCurrentUser()).getRole().equals(UserEnums.STORE)) {
            return this.baseMapper.getDistributionGoodsVO(
                    PageUtil.initPage(searchParams), searchParams.storeQueryWrapper());
        } else if (UserContext.getCurrentUser().getRole().equals(UserEnums.MEMBER)) {
            // 判断当前登录用户是否为分销员
            Distribution distribution = distributionService.getDistribution();
            if (distribution != null) {
                // 判断查看已选择的分销商品列表
                if (searchParams.isChecked()) {
                    return this.baseMapper.selectGoods(
                            PageUtil.initPage(searchParams),
                            searchParams.distributionQueryWrapper(),
                            distribution.getId());
                } else {
                    return this.baseMapper.notSelectGoods(
                            PageUtil.initPage(searchParams),
                            searchParams.distributionQueryWrapper(),
                            distribution.getId());
                }
            }
            throw new BusinessException(ResultEnum.DISTRIBUTION_NOT_EXIST);
        }
        // 如果是平台则直接进行查询
        return this.baseMapper.getDistributionGoodsVO(
                PageUtil.initPage(searchParams), searchParams.distributionQueryWrapper());
    }

    /**
     * 根据条件查询分销商品信息列表
     *
     * @param distributionGoodsPageQuery 商品条件
     * @return 分销商品信息列表
     */
    @Override
    public List<DistributionGoods> getDistributionGoodsList(DistributionGoodsPageQuery distributionGoodsPageQuery) {
        return this.list(distributionGoodsPageQuery.queryWrapper());
    }

    /**
     * 根据条件查询分销商品信息
     *
     * @param distributionGoodsPageQuery 条件
     * @return 分销商品信息
     */
    @Override
    public DistributionGoods getDistributionGoods(DistributionGoodsPageQuery distributionGoodsPageQuery) {
        return this.getOne(distributionGoodsPageQuery.queryWrapper(), false);
    }

    /**
     * 根据条件删除分销商品
     *
     * @param distributionGoodsPageQuery 条件
     */
    @Override
    public boolean deleteDistributionGoods(DistributionGoodsPageQuery distributionGoodsPageQuery) {
        return this.remove(distributionGoodsPageQuery.queryWrapper());
    }

    @Override
    public DistributionGoods distributionGoodsVO(String id) {

        return this.getById(id);
    }

    @Override
    public DistributionGoods distributionGoodsVOBySkuId(String skuId) {
        return this.getOne(new LambdaUpdateWrapper<DistributionGoods>().eq(DistributionGoods::getSkuId, skuId));
    }

    @Override
    public List<DistributionGoods> distributionGoods(List<String> skuIds) {
        return this.list(new LambdaUpdateWrapper<DistributionGoods>().in(DistributionGoods::getSkuId, skuIds));
    }

    @Override
    public DistributionGoods checked(String skuId, BigDecimal commission, String storeId) {

        // 检查分销功能开关
        distributionService.checkDistributionSetting();

        // 判断是否存在分销商品，如果存在不能添加
        QueryWrapper queryWrapper = Wrappers.query().eq("sku_id", skuId);

        if (this.getOne(queryWrapper) != null) {
            throw new BusinessException(ResultEnum.DISTRIBUTION_GOODS_BigDecimal);
        }
        GoodsSku goodsSku = goodsSkuApi.getGoodsSkuByIdFromCache(skuId);
        if (!goodsSku.getStoreId().equals(storeId)) {
            throw new BusinessException(ResultEnum.USER_AUTHORITY_ERROR);
        }
        DistributionGoods distributionGoods = new DistributionGoods(goodsSku, commission);
        this.save(distributionGoods);
        return distributionGoods;
    }
}
