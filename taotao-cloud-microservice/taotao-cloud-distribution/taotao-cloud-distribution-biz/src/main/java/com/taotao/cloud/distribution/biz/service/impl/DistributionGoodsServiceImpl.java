package com.taotao.cloud.distribution.biz.service.impl;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.distribution.api.dto.DistributionGoodsSearchParams;
import com.taotao.cloud.distribution.api.vo.DistributionGoodsVO;
import com.taotao.cloud.distribution.biz.entity.DistributionGoods;
import com.taotao.cloud.distribution.biz.mapper.DistributionGoodsMapper;
import com.taotao.cloud.distribution.biz.service.DistributionGoodsService;
import com.taotao.cloud.distribution.biz.service.DistributionService;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


/**
 * 分销商品接口实现
 */
@Service
public class DistributionGoodsServiceImpl extends ServiceImpl<DistributionGoodsMapper, DistributionGoods> implements
	DistributionGoodsService {

    /**
     * 分销员
     */
    @Autowired
    private DistributionService distributionService;
    /**
     * 规格商品
     */
    @Autowired
    private GoodsSkuService goodsSkuService;

    @Override
    public IPage<DistributionGoodsVO> goodsPage(DistributionGoodsSearchParams searchParams) {
        //获取商家的分销商品列表
        if (Objects.requireNonNull(UserContext.getCurrentUser()).getRole().equals(UserEnums.STORE)) {
            return this.baseMapper.getDistributionGoodsVO(PageUtil.initPage(searchParams), searchParams.storeQueryWrapper());
        } else if (UserContext.getCurrentUser().getRole().equals(UserEnums.MEMBER)) {
            //判断当前登录用户是否为分销员
            Distribution distribution = distributionService.getDistribution();
            if (distribution != null) {
                //判断查看已选择的分销商品列表
                if (searchParams.isChecked()) {
                    return this.baseMapper.selectGoods(PageUtil.initPage(searchParams), searchParams.distributionQueryWrapper(), distribution.getId());
                } else {
                    return this.baseMapper.notSelectGoods(PageUtil.initPage(searchParams), searchParams.distributionQueryWrapper(), distribution.getId());
                }
            }
            throw new BusinessException(ResultEnum.DISTRIBUTION_NOT_EXIST);
        }
        //如果是平台则直接进行查询
        return this.baseMapper.getDistributionGoodsVO(PageUtil.initPage(searchParams), searchParams.distributionQueryWrapper());
    }

    /**
     * 根据条件查询分销商品信息列表
     *
     * @param distributionGoodsSearchParams 商品条件
     * @return 分销商品信息列表
     */
    @Override
    public List<DistributionGoods> getDistributionGoodsList(DistributionGoodsSearchParams distributionGoodsSearchParams) {
        return this.list(distributionGoodsSearchParams.queryWrapper());
    }

    /**
     * 根据条件查询分销商品信息
     *
     * @param distributionGoodsSearchParams 条件
     * @return 分销商品信息
     */
    @Override
    public DistributionGoods getDistributionGoods(DistributionGoodsSearchParams distributionGoodsSearchParams) {
        return this.getOne(distributionGoodsSearchParams.queryWrapper(), false);
    }

    /**
     * 根据条件删除分销商品
     *
     * @param distributionGoodsSearchParams 条件
     */
    @Override
    public boolean deleteDistributionGoods(DistributionGoodsSearchParams distributionGoodsSearchParams) {
        return this.remove(distributionGoodsSearchParams.queryWrapper());
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

        //检查分销功能开关
        distributionService.checkDistributionSetting();

        //判断是否存在分销商品，如果存在不能添加
        QueryWrapper queryWrapper = Wrappers.query().eq("sku_id", skuId);

        if (this.getOne(queryWrapper) != null) {
            throw new BusinessException(ResultEnum.DISTRIBUTION_GOODS_BigDecimal);
        }
        GoodsSku goodsSku = goodsSkuService.getGoodsSkuByIdFromCache(skuId);
        if (!goodsSku.getStoreId().equals(storeId)) {
            throw new BusinessException(ResultEnum.USER_AUTHORITY_ERROR);
        }
        DistributionGoods distributionGoods = new DistributionGoods(goodsSku, commission);
        this.save(distributionGoods);
        return distributionGoods;
    }

}
