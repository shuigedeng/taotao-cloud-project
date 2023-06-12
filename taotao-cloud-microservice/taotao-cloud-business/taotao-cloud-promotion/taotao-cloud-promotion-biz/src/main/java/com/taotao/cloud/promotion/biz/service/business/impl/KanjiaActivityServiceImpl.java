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

package com.taotao.cloud.promotion.biz.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.number.CurrencyUtils;
import com.taotao.cloud.goods.api.feign.IFeignGoodsSkuApi;
import com.taotao.cloud.member.api.feign.IFeignMemberApi;
import com.taotao.cloud.promotion.api.enums.KanJiaStatusEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import com.taotao.cloud.promotion.api.model.dto.KanjiaActivityDTO;
import com.taotao.cloud.promotion.api.model.page.KanjiaActivityPageQuery;
import com.taotao.cloud.promotion.api.model.query.KanjiaActivitySearchQuery;
import com.taotao.cloud.promotion.api.model.vo.KanjiaActivityVO;
import com.taotao.cloud.promotion.biz.mapper.KanJiaActivityMapper;
import com.taotao.cloud.promotion.biz.model.entity.KanjiaActivity;
import com.taotao.cloud.promotion.biz.model.entity.KanjiaActivityGoods;
import com.taotao.cloud.promotion.biz.model.entity.KanjiaActivityLog;
import com.taotao.cloud.promotion.biz.service.business.IKanjiaActivityGoodsService;
import com.taotao.cloud.promotion.biz.service.business.IKanjiaActivityLogService;
import com.taotao.cloud.promotion.biz.service.business.IKanjiaActivityService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import com.taotao.cloud.promotion.biz.util.PageQueryUtils;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 砍价活动参与记录业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:46:18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class KanjiaActivityServiceImpl extends ServiceImpl<KanJiaActivityMapper, KanjiaActivity>
        implements IKanjiaActivityService {

    @Autowired
    private IKanjiaActivityGoodsService kanjiaActivityGoodsService;

    @Autowired
    private IKanjiaActivityLogService kanjiaActivityLogService;

    @Autowired
    private IFeignMemberApi memberApi;

    @Autowired
    private IFeignGoodsSkuApi goodsSkuApi;

    @Override
    public KanjiaActivity getKanjiaActivity(KanjiaActivitySearchQuery searchQuery) {
		QueryWrapper<KanjiaActivity> wrapper = PageQueryUtils.kanjiaActivitySearchQuerywrapper(searchQuery);
		return this.getOne(wrapper);
    }

    @Override
    public KanjiaActivityVO getKanjiaActivityVO(KanjiaActivitySearchQuery kanJiaActivitySearchParams) {

    }

    @Override
    public KanjiaActivityLog add(String id) {
        AuthUser authUser = Objects.requireNonNull(SecurityUtils.getCurrentUser());
        // 根据skuId查询当前sku是否参与活动并且是在活动进行中
        KanjiaActivityGoods kanJiaActivityGoods = kanjiaActivityGoodsService.getById(id);
        // 只有砍价商品存在且已经开始的活动才可以发起砍价
        if (kanJiaActivityGoods == null
                || !kanJiaActivityGoods.getPromotionStatus().equals(PromotionsStatusEnum.START.name())) {
            throw new BusinessException(ResultEnum.PROMOTION_STATUS_END);
        }
        KanjiaActivityLog kanjiaActivityLog = new KanjiaActivityLog();
        // 获取会员信息
        Member member = memberApi.getById(authUser.getId());
        // 校验此活动是否已经发起过
        QueryWrapper<KanjiaActivity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("kanjia_activity_goods_id", kanJiaActivityGoods.getId());
        queryWrapper.eq("member_id", member.getId());
        if (this.count(queryWrapper) > 0) {
            throw new BusinessException(ResultEnum.KANJIA_ACTIVITY_MEMBER_ERROR);
        }
        KanjiaActivity kanJiaActivity = new KanjiaActivity();
        // 获取商品信息
        GoodsSku goodsSku = goodsSkuApi.getGoodsSkuByIdFromCache(kanJiaActivityGoods.getSkuId());
        if (goodsSku != null) {
            kanJiaActivity.setSkuId(kanJiaActivityGoods.getSkuId());
            kanJiaActivity.setGoodsName(goodsSku.getGoodsName());
            kanJiaActivity.setKanjiaActivityGoodsId(kanJiaActivityGoods.getId());
            kanJiaActivity.setThumbnail(goodsSku.getThumbnail());
            kanJiaActivity.setMemberId(SecurityUtils.getCurrentUser().getId());
            kanJiaActivity.setMemberName(member.getUsername());
            kanJiaActivity.setStatus(KanJiaStatusEnum.START.name());
            // 剩余砍价金额 开始 是商品金额
            kanJiaActivity.setSurplusPrice(goodsSku.getPrice());
            // 砍价最低购买金额
            kanJiaActivity.setPurchasePrice(kanJiaActivityGoods.getPurchasePrice());
            // 保存我的砍价活动
            boolean result = this.save(kanJiaActivity);

            // 因为发起砍价就是自己给自己砍一刀，所以要添加砍价记录信息
            if (result) {
                kanjiaActivityLog = this.helpKanJia(kanJiaActivity.getId());
            }
        }
        return kanjiaActivityLog;
    }

    @Override
    public KanjiaActivityLog helpKanJia(String kanjiaActivityId) {
        AuthUser authUser = Objects.requireNonNull(SecurityUtils.getCurrentUser());
        // 获取会员信息
        Member member = memberApi.getById(authUser.getId());
        // 根据砍价发起活动id查询砍价活动信息
        KanjiaActivity kanjiaActivity = this.getById(kanjiaActivityId);
        // 判断活动非空或非正在进行中的活动
        if (kanjiaActivity == null || !kanjiaActivity.getStatus().equals(PromotionsStatusEnum.START.name())) {
            throw new BusinessException(ResultEnum.PROMOTION_STATUS_END);
        } else if (member == null) {
            throw new BusinessException(ResultEnum.USER_NOT_EXIST);
        }
        // 根据skuId查询当前sku是否参与活动并且是在活动进行中
        KanjiaActivityGoods kanJiaActivityGoods =
                kanjiaActivityGoodsService.getById(kanjiaActivity.getKanjiaActivityGoodsId());
        if (kanJiaActivityGoods == null) {
            throw new BusinessException(ResultEnum.PROMOTION_STATUS_END);
        }
        // 判断是否已参与
        LambdaQueryWrapper<KanjiaActivityLog> lambdaQueryWrapper = new LambdaQueryWrapper<KanjiaActivityLog>()
                .eq(KanjiaActivityLog::getKanjiaActivityId, kanjiaActivityId)
                .eq(KanjiaActivityLog::getKanjiaMemberId, member.getId());
        if (kanjiaActivityLogService.count(lambdaQueryWrapper) > 0) {
            throw new BusinessException(ResultEnum.PROMOTION_LOG_EXIST);
        }

        // 添加砍价记录
        KanjiaActivityDTO kanjiaActivityDTO = new KanjiaActivityDTO();
        kanjiaActivityDTO.setKanjiaActivityGoodsId(kanjiaActivity.getKanjiaActivityGoodsId());
        kanjiaActivityDTO.setKanjiaActivityId(kanjiaActivityId);
        // 获取砍价金额
        BigDecimal price = this.getKanjiaPrice(kanJiaActivityGoods, kanjiaActivity.getSurplusPrice());
        kanjiaActivityDTO.setKanjiaPrice(price);
        // 计算剩余金额
        kanjiaActivityDTO.setSurplusPrice(CurrencyUtils.sub(kanjiaActivity.getSurplusPrice(), price));
        kanjiaActivityDTO.setKanjiaMemberId(member.getId());
        kanjiaActivityDTO.setKanjiaMemberName(member.getUsername());
        kanjiaActivityDTO.setKanjiaMemberFace(member.getFace());
        KanjiaActivityLog kanjiaActivityLog = kanjiaActivityLogService.addKanJiaActivityLog(kanjiaActivityDTO);

        // 如果可砍金额为0的话说明活动成功了
        if (BigDecimal.BigDecimalToLongBits(kanjiaActivityDTO.getSurplusPrice())
                == BigDecimal.BigDecimalToLongBits(0D)) {
            kanjiaActivity.setStatus(KanJiaStatusEnum.SUCCESS.name());
        }
        kanjiaActivity.setSurplusPrice(kanjiaActivityLog.getSurplusPrice());
        this.updateById(kanjiaActivity);
        return kanjiaActivityLog;
    }

    /**
     * 随机获取砍一刀价格
     *
     * @param kanjiaActivityGoods 砍价商品信息
     * @param surplusPrice 剩余可砍金额
     * @return 砍一刀价格
     */
    private BigDecimal getKanjiaPrice(KanjiaActivityGoods kanjiaActivityGoods, BigDecimal surplusPrice) {

        // 如果剩余砍价金额小于最低砍价金额则返回0
        if (kanjiaActivityGoods.getLowestPrice() > surplusPrice) {
            return surplusPrice;
        }

        // 如果金额相等则直接返回
        if (kanjiaActivityGoods.getLowestPrice().equals(kanjiaActivityGoods.getHighestPrice())) {
            return kanjiaActivityGoods.getLowestPrice();
        }
        // 获取随机砍价金额
        BigDecimal bigDecimal = RandomUtil.randomBigDecimal(
                Convert.toBigDecimal(kanjiaActivityGoods.getLowestPrice()),
                Convert.toBigDecimal(kanjiaActivityGoods.getHighestPrice()));
        return bigDecimal.setScale(2, RoundingMode.UP).BigDecimalValue();
    }

    @Override
    public IPage<KanjiaActivity> getForPage(KanjiaActivityPageQuery kanjiaActivityPageQuery, PageVO page) {
        QueryWrapper<KanjiaActivity> queryWrapper = kanjiaActivityPageQuery.wrapper();
        return this.page(PageUtil.initPage(page), queryWrapper);
    }
}
