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

package com.taotao.cloud.goods.biz.service.business.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.goods.biz.model.dto.DraftGoodsSkuParamsDTO;
import com.taotao.cloud.goods.biz.model.dto.GoodsParamsDTO;
import com.taotao.cloud.goods.biz.model.page.DraftGoodsPageQuery;
import com.taotao.cloud.goods.biz.model.vo.DraftGoodsSkuParamsVO;
import com.taotao.cloud.goods.biz.mapper.IDraftGoodsMapper;
import com.taotao.cloud.goods.biz.model.convert.DraftGoodsConvert;
import com.taotao.cloud.goods.biz.model.entity.Category;
import com.taotao.cloud.goods.biz.model.entity.DraftGoods;
import com.taotao.cloud.goods.biz.model.entity.GoodsGallery;
import com.taotao.cloud.goods.biz.model.entity.GoodsSku;
import com.taotao.cloud.goods.biz.repository.DraftGoodsRepository;
import com.taotao.cloud.goods.biz.repository.IDraftGoodsRepository;
import com.taotao.cloud.goods.biz.service.business.ICategoryService;
import com.taotao.cloud.goods.biz.service.business.IDraftGoodsService;
import com.taotao.cloud.goods.biz.service.business.IGoodsGalleryService;
import com.taotao.cloud.goods.biz.service.business.IGoodsSkuService;
import com.taotao.cloud.goods.biz.util.QueryUtil;
import com.taotao.boot.webagg.service.impl.BaseSuperServiceImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.*;
import org.springframework.stereotype.Service;

/**
 * 草稿商品业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:02:24
 */
@AllArgsConstructor
@Service
public class DraftGoodsServiceImpl
        extends BaseSuperServiceImpl<DraftGoods, Long,IDraftGoodsMapper,  DraftGoodsRepository, IDraftGoodsRepository>
        implements IDraftGoodsService {

    /** 分类 */
    private final ICategoryService categoryService;
    /** 商品相册 */
    private final IGoodsGalleryService goodsGalleryService;
    /** 规格商品 */
    private final IGoodsSkuService goodsSkuService;

    @Override
    public boolean addGoodsDraft(DraftGoodsSkuParamsDTO draftGoods) {
        draftGoods.setGoodsGalleryListJson(JSONUtil.toJsonStr(draftGoods.getGoodsGalleryList()));
        draftGoods.setSkuListJson(JSONUtil.toJsonStr(draftGoods.getSkuList()));
        draftGoods.setGoodsParamsListJson(JSONUtil.toJsonStr(draftGoods.getGoodsParamsDTOList()));

        return this.save(DraftGoodsConvert.INSTANCE.convert(draftGoods));
    }

    @Override
    public boolean updateGoodsDraft(DraftGoodsSkuParamsDTO draftGoods) {
        draftGoods.setGoodsGalleryListJson(JSONUtil.toJsonStr(draftGoods.getGoodsGalleryList()));
        draftGoods.setSkuListJson(JSONUtil.toJsonStr(draftGoods.getSkuList()));
        draftGoods.setGoodsParamsListJson(JSONUtil.toJsonStr(draftGoods.getGoodsParamsDTOList()));

        DraftGoods draftGoods1 = DraftGoodsConvert.INSTANCE.convert(draftGoods);
        // todo 此处需要修改
        draftGoods1.setId(0L);

        return this.updateById(draftGoods1);
    }

    @Override
    public boolean saveGoodsDraft(DraftGoodsSkuParamsDTO draftGoods) {
        if (draftGoods.getGoodsGalleryList() != null
                && !draftGoods.getGoodsGalleryList().isEmpty()) {
            GoodsGallery goodsGallery = goodsGalleryService.getGoodsGallery(
                    draftGoods.getGoodsGalleryList().get(0));
            draftGoods.setOriginal(goodsGallery.getOriginal());
            draftGoods.setSmall(goodsGallery.getSmall());
            draftGoods.setThumbnail(goodsGallery.getThumbnail());
        }
        draftGoods.setGoodsGalleryListJson(JSONUtil.toJsonStr(draftGoods.getGoodsGalleryList()));
        draftGoods.setSkuListJson(JSONUtil.toJsonStr(this.getGoodsSkuList(draftGoods.getSkuList())));
        draftGoods.setGoodsParamsListJson(JSONUtil.toJsonStr(draftGoods.getGoodsParamsDTOList()));

        return this.saveOrUpdate(DraftGoodsConvert.INSTANCE.convert(draftGoods));
    }

    @Override
    public boolean deleteGoodsDraft(Long id) {
        return this.removeById(id);
    }

    @Override
    public DraftGoodsSkuParamsVO getDraftGoods(Long id) {
        DraftGoods draftGoods = this.getById(id);
        DraftGoodsSkuParamsVO draftGoodsSkuParamsVO = new DraftGoodsSkuParamsVO();
        BeanUtil.copyProperties(draftGoods, draftGoodsSkuParamsVO);

        // 商品分类名称赋值
        List<String> categoryName = new ArrayList<>();
        String[] strArray = draftGoods.getCategoryPath().split(",");
        List<Category> categories = categoryService.listByIds(Arrays.asList(strArray));
        for (Category category : categories) {
            categoryName.add(category.getName());
        }
        draftGoodsSkuParamsVO.setCategoryName(categoryName);
        draftGoodsSkuParamsVO.setGoodsParamsDTOList(
                JSONUtil.toList(JSONUtil.parseArray(draftGoods.getGoodsParamsListJson()), GoodsParamsDTO.class));
        draftGoodsSkuParamsVO.setGoodsGalleryList(
                JSONUtil.toList(JSONUtil.parseArray(draftGoods.getGoodsGalleryListJson()), String.class));
        JSONArray jsonArray = JSONUtil.parseArray(draftGoods.getSkuListJson());
        List<GoodsSku> list = JSONUtil.toList(jsonArray, GoodsSku.class);
        draftGoodsSkuParamsVO.setSkuList(goodsSkuService.getGoodsSkuVOList(list));
        return draftGoodsSkuParamsVO;
    }

    @Override
    public IPage<DraftGoods> draftGoodsQueryPage(DraftGoodsPageQuery searchParams) {
        return this.page(searchParams.buildMpPage(), QueryUtil.draftGoodsQueryWrapper(searchParams));
    }

    /**
     * 获取sku集合
     *
     * @param skuList sku列表
     * @return sku集合
     */
    private List<GoodsSku> getGoodsSkuList(List<Map<String, Object>> skuList) {
        List<GoodsSku> skus = new ArrayList<>();
        for (Map<String, Object> skuVO : skuList) {
            GoodsSku add = this.add(skuVO);
            skus.add(add);
        }
        return skus;
    }

    private GoodsSku add(Map<String, Object> map) {
        Map<String, Object> specMap = new HashMap<>(2);
        GoodsSku sku = new GoodsSku();
        for (Map.Entry<String, Object> m : map.entrySet()) {
            switch (m.getKey()) {
                case "sn" -> sku.setSn(m.getValue() != null ? m.getValue().toString() : "");
                case "cost" -> sku.setCost(Convert.toBigDecimal(m.getValue()));
                case "price" -> sku.setPrice(Convert.toBigDecimal(m.getValue()));
                case "quantity" -> sku.setQuantity(Convert.toInt(m.getValue()));
                case "weight" -> sku.setWeight(Convert.toBigDecimal(m.getValue()));
                default -> specMap.put(m.getKey(), m.getValue());
            }
        }
        sku.setSpecs(JSONUtil.toJsonStr(specMap));
        return sku;
    }
}
