package com.taotao.cloud.member.biz.service.impl;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.member.biz.entity.MemberBrowse;
import com.taotao.cloud.member.biz.mapper.FootprintMapper;
import com.taotao.cloud.member.biz.service.IMemberBrowseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 会员浏览历史业务层实现
 *
 *
 * @since 2020/11/18 10:46 上午
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IMemberBrowseServiceImpl extends ServiceImpl<FootprintMapper, MemberBrowse> implements
	IMemberBrowseService {

    /**
     * es商品业务层
     */
    @Autowired
    private EsGoodsSearchService esGoodsSearchService;

    @Override
    public MemberBrowse saveFootprint(MemberBrowse memberBrowse) {
        LambdaQueryWrapper<MemberBrowse> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MemberBrowse::getMemberId, memberBrowse.getMemberId());
        queryWrapper.eq(MemberBrowse::getGoodsId, memberBrowse.getGoodsId());
        //如果已存在某商品记录，则更新其修改时间
        //如果不存在则添加记录
        List<MemberBrowse> oldPrints = list(queryWrapper);
        if (oldPrints != null && !oldPrints.isEmpty()) {
            MemberBrowse oldPrint = oldPrints.get(0);
            oldPrint.setSkuId(memberBrowse.getSkuId());
            this.updateById(oldPrint);
            return oldPrint;
        } else {
            memberBrowse.setCreateTime(new Date());
            this.save(memberBrowse);
            //删除超过100条后的记录
            this.baseMapper.deleteLastFootPrint(memberBrowse.getMemberId());
            return memberBrowse;
        }
    }

    @Override
    public boolean clean() {
        LambdaQueryWrapper<MemberBrowse> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(MemberBrowse::getMemberId, UserContext.getCurrentUser().getId());
        return this.remove(lambdaQueryWrapper);
    }

    @Override
    public boolean deleteByIds(List<String> ids) {
        LambdaQueryWrapper<MemberBrowse> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(MemberBrowse::getMemberId, UserContext.getCurrentUser().getId());
        lambdaQueryWrapper.in(MemberBrowse::getGoodsId, ids);
        this.remove(lambdaQueryWrapper);
        return true;
    }

    @Override
    public List<EsGoodsIndex> footPrintPage(PageVO pageVO) {

        LambdaQueryWrapper<MemberBrowse> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(MemberBrowse::getMemberId, UserContext.getCurrentUser().getId());
        lambdaQueryWrapper.eq(MemberBrowse::getDeleteFlag, false);
        lambdaQueryWrapper.orderByDesc(MemberBrowse::getUpdateTime);
        List<String> skuIdList = this.baseMapper.footprintSkuIdList(PageUtil.initPage(pageVO), lambdaQueryWrapper);
        if (!skuIdList.isEmpty()) {
            List<EsGoodsIndex> list = esGoodsSearchService.getEsGoodsBySkuIds(skuIdList);
            //去除为空的商品数据
            list.removeIf(Objects::isNull);
            return list;
        }
        return Collections.emptyList();
    }

    @Override
    public long getFootprintNum() {
        LambdaQueryWrapper<MemberBrowse> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(
	        MemberBrowse::getMemberId, Objects.requireNonNull(UserContext.getCurrentUser()).getId());
        lambdaQueryWrapper.eq(MemberBrowse::getDeleteFlag, false);
        return this.count(lambdaQueryWrapper);
    }
}
