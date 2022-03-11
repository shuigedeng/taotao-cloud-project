package com.taotao.cloud.member.biz.service.impl;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.member.api.vo.GoodsCollectionVO;
import com.taotao.cloud.member.biz.entity.MemberGoodsCollection;
import com.taotao.cloud.member.biz.mapper.GoodsCollectionMapper;
import com.taotao.cloud.member.biz.service.GoodsCollectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 会员收藏业务层实现
 *
 * 
 * @since 2020/11/18 2:25 下午
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GoodsCollectionServiceImpl extends ServiceImpl<GoodsCollectionMapper, MemberGoodsCollection> implements
	GoodsCollectionService {


    @Override
    public IPage<GoodsCollectionVO> goodsCollection(PageVO pageVo) {
        QueryWrapper<GoodsCollectionVO> queryWrapper = new QueryWrapper();
        queryWrapper.eq("gc.member_id", UserContext.getCurrentUser().getId());
        queryWrapper.groupBy("gc.id");
        queryWrapper.orderByDesc("gc.create_time");
        return this.baseMapper.goodsCollectionVOList(PageUtil.initPage(pageVo), queryWrapper);
    }

    @Override
    public boolean isCollection(String skuId) {
        QueryWrapper<MemberGoodsCollection> queryWrapper = new QueryWrapper();
        queryWrapper.eq("member_id", UserContext.getCurrentUser().getId());
        queryWrapper.eq(skuId != null, "sku_id", skuId);
        return Optional.ofNullable(this.getOne(queryWrapper)).isPresent();
    }

    @Override
    public MemberGoodsCollection addGoodsCollection(String skuId) {
        MemberGoodsCollection memberGoodsCollection = this.getOne(new LambdaUpdateWrapper<MemberGoodsCollection>()
                .eq(MemberGoodsCollection::getMemberId, UserContext.getCurrentUser().getId())
                .eq(MemberGoodsCollection::getSkuId, skuId));
        if (memberGoodsCollection == null) {
            memberGoodsCollection = new MemberGoodsCollection(UserContext.getCurrentUser().getId(), skuId);

            this.save(memberGoodsCollection);
            return memberGoodsCollection;
        }
        throw new ServiceException(ResultCode.USER_COLLECTION_EXIST);
    }

    @Override
    public boolean deleteGoodsCollection(String skuId) {
        QueryWrapper<MemberGoodsCollection> queryWrapper = new QueryWrapper();
        queryWrapper.eq("member_id", UserContext.getCurrentUser().getId());
        queryWrapper.eq(skuId != null, "sku_id", skuId);
        return this.remove(queryWrapper);
    }

    @Override
    public boolean deleteGoodsCollection(List<String> goodsIds) {
        QueryWrapper<MemberGoodsCollection> queryWrapper = new QueryWrapper();
        queryWrapper.in("sku_id", goodsIds);
        return this.remove(queryWrapper);
    }

    @Override
    public boolean deleteSkuCollection(List<String> skuIds) {
        QueryWrapper<MemberGoodsCollection> queryWrapper = new QueryWrapper();
        queryWrapper.in("sku_id", skuIds);
        return this.remove(queryWrapper);
    }
}
