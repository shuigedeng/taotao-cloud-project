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

package com.taotao.cloud.goods.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.taotao.cloud.cache.redis.repository.RedisRepository;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.goods.application.command.goods.dto.GoodsAddCmd;
import com.taotao.cloud.goods.application.service.ICategoryService;
import com.taotao.cloud.goods.application.service.IGoodsService;
import com.taotao.cloud.goods.application.service.IGoodsSkuService;
import com.taotao.cloud.goods.infrastructure.persistent.mapper.IGoodsMapper;
import com.taotao.cloud.goods.infrastructure.persistent.po.GoodsPO;
import com.taotao.cloud.goods.infrastructure.persistent.repository.cls.GoodsRepository;
import com.taotao.cloud.goods.infrastructure.persistent.repository.inf.IGoodsRepository;
import com.taotao.cloud.security.springsecurity.model.SecurityUser;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.mq.stream.framework.rocketmq.RocketmqSendCallbackBuilder;
import com.taotao.cloud.mq.stream.framework.rocketmq.tags.GoodsTagsEnum;
import com.taotao.cloud.mq.stream.properties.RocketmqCustomProperties;
import com.taotao.cloud.web.base.service.impl.BaseSuperServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.dromara.hutool.core.math.NumberUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.json.JSONUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 商品业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:02:41
 */
@Service
@AllArgsConstructor
public class GoodsServiceImpl extends BaseSuperServiceImpl<GoodsPO, Long, IGoodsMapper, GoodsRepository, IGoodsRepository>
        implements IGoodsService {
    private final GoodsManager goodsManager;

    /** 分类 */
    private final ICategoryService categoryService;
    /** 商品相册 */
    private final IGoodsGalleryService goodsGalleryService;
    /** 商品规格 */
    private final IGoodsSkuService goodsSkuService;

    /** 设置 */
    private final IFeignSettingApi settingApi;
    /** 店铺详情 */
    private final IFeignStoreApi storeApi;
    /** 运费模板 */
    private final IFeignFreightTemplateApi freightTemplateApi;
    /** 会员评价 */
    private final IFeignMemberEvaluationApi memberEvaluationApi;
    /** rocketMq */
    private final RocketMQTemplate rocketMQTemplate;
    /** rocketMq配置 */
    private final RocketmqCustomProperties rocketmqCustomProperties;

    private final RedisRepository redisRepository;

    @Override
    public List<GoodsPO> getByBrandIds(List<Long> brandIds) {
        LambdaQueryWrapper<GoodsPO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(GoodsPO::getBrandId, brandIds);
        return list(lambdaQueryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean underStoreGoods(Long storeId) {
        // 获取商品ID列表
        List<Long> list = this.baseMapper.getGoodsIdByStoreId(storeId);
        // 下架店铺下的商品
        updateGoodsMarketAble(list, GoodsStatusEnum.DOWN, "店铺关闭");
        return true;
    }

    /**
     * 更新商品参数
     *
     * @param goodsId 商品id
     * @param params 商品参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateGoodsParams(Long goodsId, String params) {
        LambdaUpdateWrapper<GoodsPO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(GoodsPO::getId, goodsId);
        updateWrapper.set(GoodsPO::getParams, params);
        return this.update(updateWrapper);
    }

    @Override
    public final Long getGoodsCountByCategory(Long categoryId) {
        QueryWrapper<GoodsPO> queryWrapper = Wrappers.query();
        queryWrapper.like("category_path", categoryId);
        queryWrapper.eq("delete_flag", false);
        return this.count(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addGoods(GoodsAddCmd goodsAddCmd) {
        GoodsPO goods = new GoodsPO(goodsAddCmd);
        // 检查商品
        this.checkGoods(goods);
        // 向goods加入图片
        this.setGoodsGalleryParam(goodsAddCmd.getGoodsGalleryList().get(0), goods);
        // 添加商品参数
        if (goodsAddCmd.getGoodsParamsAddCmdList() != null
                && !goodsAddCmd.getGoodsParamsAddCmdList().isEmpty()) {
            // 给商品参数填充值
            goods.setParams(JSONUtil.toJsonStr(goodsAddCmd.getGoodsParamsAddCmdList()));
        }
        // 添加商品
        this.save(goods);
        // 添加商品sku信息
        this.goodsSkuService.add(goodsAddCmd.getSkuList(), goods);
        // 添加相册
        if (goodsAddCmd.getGoodsGalleryList() != null
                && !goodsAddCmd.getGoodsGalleryList().isEmpty()) {
            this.goodsGalleryService.add(goodsAddCmd.getGoodsGalleryList(), goods.getId());
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editGoods(GoodsAddCmd goodsAddCmd, Long goodsId) {
        GoodsPO goods = new GoodsPO(goodsAddCmd);
        goods.setId(goodsId);

        // 检查商品信息
        this.checkGoods(goods);
        // 向goods加入图片
        this.setGoodsGalleryParam(goodsAddCmd.getGoodsGalleryList().get(0), goods);
        // 添加商品参数
        if (goodsAddCmd.getGoodsParamsAddCmdList() != null
                && !goodsAddCmd.getGoodsParamsAddCmdList().isEmpty()) {
            goods.setParams(JSONUtil.toJsonStr(goodsAddCmd.getGoodsParamsAddCmdList()));
        }
        // 修改商品
        this.updateById(goods);
        // 修改商品sku信息
        this.goodsSkuService.update(goodsAddCmd.getSkuList(), goods, goodsAddCmd.getRegeneratorSkuFlag());
        // 添加相册
        if (goodsAddCmd.getGoodsGalleryList() != null
                && !goodsAddCmd.getGoodsGalleryList().isEmpty()) {
            this.goodsGalleryService.add(goodsAddCmd.getGoodsGalleryList(), goods.getId());
        }
        if (GoodsAuthEnum.TOBEAUDITED.name().equals(goods.getIsAuth())) {
            this.deleteEsGoods(Collections.singletonList(goodsId));
        }
        redisRepository.del(CachePrefix.GOODS.getPrefix() + goodsId);
        return true;
    }

    @Override
    public GoodsSkuParamsCO getGoodsCO(Long goodsId) {
        // 缓存获取，如果没有则读取缓存
        GoodsSkuParamsCO goodsSkuParamsCO =
                (GoodsSkuParamsCO) redisRepository.get(CachePrefix.GOODS.getPrefix() + goodsId);
        if (goodsSkuParamsCO != null) {
            return goodsSkuParamsCO;
        }

        // 查询商品信息
        GoodsPO goods = this.getById(goodsId);
        if (goods == null) {
            LogUtils.error("商品ID为" + goodsId + "的商品不存在");
            throw new BusinessException(ResultEnum.GOODS_NOT_EXIST);
        }

        // 赋值
        goodsSkuParamsCO = GoodsConvert.INSTANCE.convert(goods);
        // 商品id
        goodsSkuParamsCO.setId(goods.getId());
        // 商品相册
        List<GoodsGallery> galleryList = goodsGalleryService.goodsGalleryList(goodsId);
        goodsSkuParamsCO.setGoodsGalleryList(galleryList.stream()
                .filter(Objects::nonNull)
                .map(GoodsGallery::getOriginal)
                .toList());

        // 商品sku赋值
        List<GoodsSkuSpecGalleryCO> goodsListByGoodsId = goodsSkuService.getGoodsListByGoodsId(goodsId);
        if (goodsListByGoodsId != null && !goodsListByGoodsId.isEmpty()) {
            goodsSkuParamsCO.setSkuList(goodsListByGoodsId);
        }

        // 商品分类名称赋值
        String categoryPath = goods.getCategoryPath();
        String[] strArray = categoryPath.split(",");
        List<Category> categories = categoryService.listByIds(Arrays.asList(strArray));
        goodsSkuParamsCO.setCategoryName(categories.stream()
                .filter(Objects::nonNull)
                .map(Category::getName)
                .toList());

        // 参数非空则填写参数
        if (StrUtil.isNotEmpty(goods.getParams())) {
            goodsSkuParamsCO.setGoodsParamsDTOList(JSONUtil.toList(goods.getParams(), GoodsParamsDTO.class));
        }

        redisRepository.set(CachePrefix.GOODS.getPrefix() + goodsId, goodsSkuParamsCO);
        return goodsSkuParamsCO;
    }

    @Override
    public IPage<GoodsPO> goodsQueryPage(GoodsPageQuery goodsPageQuery) {
        return this.page(goodsPageQuery.buildMpPage(), goodsManager.goodsQueryWrapper(goodsPageQuery));
    }

    @Override
    public List<GoodsPO> queryListByParams(GoodsPageQuery goodsPageQuery) {
        return this.list(goodsManager.goodsQueryWrapper(goodsPageQuery));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditGoods(List<Long> goodsIds, GoodsAuthEnum goodsAuthEnum) {
        boolean result = false;
        for (Long goodsId : goodsIds) {
            GoodsPO goods = this.checkExist(goodsId);
            goods.setIsAuth(goodsAuthEnum.name());
            result = this.updateById(goods);
            goodsSkuService.updateGoodsSkuStatus(goods);
            // 删除之前的缓存
            redisRepository.del(CachePrefix.GOODS.getPrefix() + goodsId);
            // 商品审核消息
            String destination = rocketmqCustomProperties.getGoodsTopic() + ":" + GoodsTagsEnum.GOODS_AUDIT.name();
            // 发送mq消息
            rocketMQTemplate.asyncSend(
                    destination, JSONUtil.toJsonStr(goods), RocketmqSendCallbackBuilder.commonCallback());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateGoodsMarketAble(List<Long> goodsIds, GoodsStatusEnum goodsStatusEnum, String underReason) {
        boolean result;

        // 如果商品为空，直接返回
        if (goodsIds == null || goodsIds.isEmpty()) {
            return true;
        }

        LambdaUpdateWrapper<GoodsPO> updateWrapper = this.getUpdateWrapperByStoreAuthority();
        updateWrapper.set(GoodsPO::getMarketEnable, goodsStatusEnum.name());
        updateWrapper.set(GoodsPO::getUnderMessage, underReason);
        updateWrapper.in(GoodsPO::getId, goodsIds);
        result = this.update(updateWrapper);

        // 修改规格商品
        LambdaQueryWrapper<GoodsPO> queryWrapper = this.getQueryWrapperByStoreAuthority();
        queryWrapper.in(GoodsPO::getId, goodsIds);
        List<GoodsPO> goodsList = this.list(queryWrapper);
        for (GoodsPO goods : goodsList) {
            goodsSkuService.updateGoodsSkuStatus(goods);
        }

        if (GoodsStatusEnum.DOWN.equals(goodsStatusEnum)) {
            this.deleteEsGoods(goodsIds);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean managerUpdateGoodsMarketAble(
            List<Long> goodsIds, GoodsStatusEnum goodsStatusEnum, String underReason) {
        boolean result;

        // 如果商品为空，直接返回
        if (goodsIds == null || goodsIds.isEmpty()) {
            return true;
        }

        // 检测管理员权限
        this.checkManagerAuthority();

        LambdaUpdateWrapper<GoodsPO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(GoodsPO::getMarketEnable, goodsStatusEnum.name());
        updateWrapper.set(GoodsPO::getUnderMessage, underReason);
        updateWrapper.in(GoodsPO::getId, goodsIds);
        result = this.update(updateWrapper);

        // 修改规格商品
        LambdaQueryWrapper<GoodsPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(GoodsPO::getId, goodsIds);
        List<GoodsPO> goodsList = this.list(queryWrapper);
        for (GoodsPO goods : goodsList) {
            goodsSkuService.updateGoodsSkuStatus(goods);
        }
        if (GoodsStatusEnum.DOWN.equals(goodsStatusEnum)) {
            this.deleteEsGoods(goodsIds);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteGoods(List<Long> goodsIds) {
        LambdaUpdateWrapper<GoodsPO> updateWrapper = this.getUpdateWrapperByStoreAuthority();
        updateWrapper.set(GoodsPO::getMarketEnable, GoodsStatusEnum.DOWN.name());
        updateWrapper.set(GoodsPO::getDelFlag, true);
        updateWrapper.in(GoodsPO::getId, goodsIds);
        this.update(updateWrapper);

        // 修改规格商品
        LambdaQueryWrapper<GoodsPO> queryWrapper = this.getQueryWrapperByStoreAuthority();
        queryWrapper.in(GoodsPO::getId, goodsIds);
        List<GoodsPO> goodsList = this.list(queryWrapper);
        for (GoodsPO goods : goodsList) {
            // 修改SKU状态
            goodsSkuService.updateGoodsSkuStatus(goods);
        }

        this.deleteEsGoods(goodsIds);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean freight(List<Long> goodsIds, Long templateId) {
        SecurityUser authUser = this.checkStoreAuthority();

        FreightTemplateCO freightTemplate = freightTemplateApi.getById(templateId);
        if (freightTemplate == null) {
            throw new BusinessException(ResultEnum.FREIGHT_TEMPLATE_NOT_EXIST);
        }
        if (authUser != null && !freightTemplate.getStoreId().equals(authUser.getStoreId())) {
            throw new BusinessException(ResultEnum.USER_AUTHORITY_ERROR);
        }
        LambdaUpdateWrapper<GoodsPO> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper.set(GoodsPO::getTemplateId, templateId);
        lambdaUpdateWrapper.in(GoodsPO::getId, goodsIds);

        return this.update(lambdaUpdateWrapper);
    }

    @Override
    public boolean updateStock(Long goodsId, Integer quantity) {
        LambdaUpdateWrapper<GoodsPO> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper.set(GoodsPO::getQuantity, quantity);
        lambdaUpdateWrapper.eq(GoodsPO::getId, goodsId);
        this.update(lambdaUpdateWrapper);
        return true;
    }

    @Override
    public boolean updateGoodsCommentNum(Long goodsId) {
        // 获取商品信息
        GoodsPO goods = this.getById(goodsId);
        // 修改商品评价数量
        goods.setCommentNum(goods.getCommentNum() + 1);

        // 好评数量
        Long highPraiseNum = memberEvaluationApi.count(goodsId, EvaluationGradeEnum.GOOD.name());

        // 好评率
        BigDecimal grade = NumberUtil.mul(
                NumberUtil.div(BigDecimal.valueOf(highPraiseNum), BigDecimal.valueOf(goods.getCommentNum()), 2), 100);

        // 修改商品好评率
        goods.setGrade(grade);
        return this.updateById(goods);
    }

    @Override
    public boolean updateGoodsBuyCount(Long goodsId, int buyCount) {
        this.update(new LambdaUpdateWrapper<GoodsPO>().eq(GoodsPO::getId, goodsId).set(GoodsPO::getBuyCount, buyCount));
        return true;
    }

    // @Override
    // @Transactional(rollbackFor = Exception.class)
    // public boolean updateStoreDetail(Store store) {
    //	UpdateWrapper updateWrapper = new UpdateWrapper<>()
    //		.eq("store_id", store.getId())
    //		.set("store_name", store.getStoreName())
    //		.set("self_operated", store.getSelfOperated());
    //	this.update(updateWrapper);
    //	goodsSkuService.update(updateWrapper);
    //	return true;
    // }

    @Override
    public Long countStoreGoodsNum(Long storeId) {
        return this.count(new LambdaQueryWrapper<GoodsPO>()
                .eq(GoodsPO::getStoreId, storeId)
                // .eq(Goods::getAuthFlag, GoodsAuthEnum.PASS.name())
                .eq(GoodsPO::getMarketEnable, GoodsStatusEnum.UPPER.name()));
    }


}
