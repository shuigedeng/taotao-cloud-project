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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.taotao.boot.common.utils.io.FileUtils;
import com.taotao.cloud.goods.biz.mapper.GoodsGalleryMapper;
import com.taotao.cloud.goods.biz.model.entity.GoodsGallery;
import com.taotao.cloud.goods.biz.repository.GoodsGalleryRepository;
import com.taotao.cloud.goods.biz.service.business.GoodsGalleryService;
import com.taotao.cloud.sys.api.enums.SettingCategoryEnum;
import com.taotao.cloud.sys.api.feign.SettingApi;
import com.taotao.cloud.sys.api.model.vo.setting.GoodsSettingVO;
import com.taotao.boot.webagg.service.impl.BaseSuperServiceImpl;
import java.util.List;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商品相册接口实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:02:38
 */
@AllArgsConstructor
@Service
public class GoodsGalleryServiceImpl
        extends BaseSuperServiceImpl<GoodsGallery, Long, GoodsGalleryMapper, GoodsGalleryRepository, GoodsGalleryRepository>
        implements GoodsGalleryService {

    /** 设置 */
    @Autowired
    private SettingApi settingApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(List<String> goodsGalleryList, Long goodsId) {
        // 删除原来商品相册信息
        this.baseMapper.delete(new UpdateWrapper<GoodsGallery>().eq("goods_id", goodsId));
        // 确定好图片选择器后进行处理
        int i = 0;
        for (String origin : goodsGalleryList) {
            // 获取带所有缩略的相册
            GoodsGallery galley = this.getGoodsGallery(origin);
            galley.setGoodsId(goodsId);
            // 默认第一个为默认图片
            galley.setIsDefault(i == 0 ? 1 : 0);
            i++;
            this.baseMapper.insert(galley);
        }
        return true;
    }

    @Override
    public GoodsGallery getGoodsGallery(String origin) {
        GoodsGallery goodsGallery = new GoodsGallery();
        // 获取商品系统配置决定是否审核
        GoodsSettingVO goodsSetting = settingApi.getGoodsSetting(SettingCategoryEnum.GOODS_SETTING.name());
        // 缩略图
        String thumbnail = FileUtils.getUrl(
                origin, goodsSetting.getAbbreviationPictureWidth(), goodsSetting.getAbbreviationPictureHeight());
        // 小图
        String small =
                FileUtils.getUrl(origin, goodsSetting.getSmallPictureWidth(), goodsSetting.getSmallPictureHeight());
        // 赋值
        goodsGallery.setSmall(small);
        goodsGallery.setThumbnail(thumbnail);
        goodsGallery.setOriginal(origin);
        return goodsGallery;
    }

    @Override
    public List<GoodsGallery> goodsGalleryList(Long goodsId) {
        // 根据商品id查询商品相册
        LambdaQueryWrapper<GoodsGallery> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(GoodsGallery::getGoodsId, goodsId);
        return this.list(queryWrapper);
    }
}
