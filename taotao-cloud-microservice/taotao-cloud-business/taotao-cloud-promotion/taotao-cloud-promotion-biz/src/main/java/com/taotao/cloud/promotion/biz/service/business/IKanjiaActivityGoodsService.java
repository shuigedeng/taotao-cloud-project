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

package com.taotao.cloud.promotion.biz.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.cloud.promotion.api.model.dto.KanjiaActivityGoodsDTO;
import com.taotao.cloud.promotion.api.model.dto.KanjiaActivityGoodsOperationDTO;
import com.taotao.cloud.promotion.api.model.vo.KanjiaActivityGoodsListVO;
import com.taotao.cloud.promotion.api.model.page.KanjiaActivityGoodsPageQuery;
import com.taotao.cloud.promotion.biz.model.bo.KanjiaActivityGoodsBO;
import com.taotao.cloud.promotion.biz.model.entity.KanjiaActivityGoods;
import java.util.List;

/**
 * 砍价业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:43:36
 */
public interface IKanjiaActivityGoodsService extends IService<KanjiaActivityGoods> {

    /**
     * 添加砍价活动商品
     *
     * @param kanJiaActivityGoodsDTOS 砍价商品
     * @return {@link Boolean }
     * @since 2022-04-27 16:43:36
     */
    Boolean add(KanjiaActivityGoodsOperationDTO kanJiaActivityGoodsDTOS);

    /**
     * 查询砍价活动商品分页信息
     *
     * @param kanJiaActivityGoodsPageQuery 砍价活动商品
     * @param pageVO 分页信息
     * @return {@link IPage }<{@link KanjiaActivityGoods }>
     * @since 2022-04-27 16:43:36
     */
    IPage<KanjiaActivityGoods> getForPage(KanjiaActivityGoodsPageQuery kanJiaActivityGoodsPageQuery, PageQuery pageQuery);

    /**
     * 查询砍价活动商品分页信息
     *
     * @param kanJiaActivityGoodsPageQuery 砍价活动商品
     * @param pageVO 分页信息
     * @return {@link IPage }<{@link KanjiaActivityGoodsListVO }>
     * @since 2022-04-27 16:43:36
     */
    IPage<KanjiaActivityGoodsBO> kanjiaGoodsPage(
		KanjiaActivityGoodsPageQuery kanJiaActivityGoodsPageQuery, PageQuery pageQuery);

    /**
     * 查询砍价活动商品
     *
     * @param goodsId 砍价活动商品id
     * @return {@link KanjiaActivityGoodsDTO }
     * @since 2022-04-27 16:43:36
     */
    KanjiaActivityGoodsDTO getKanjiaGoodsDetail(String goodsId);

    /**
     * 根据SkuId获取正在进行中的砍价商品
     *
     * @param skuId 商品规格Id
     * @return {@link KanjiaActivityGoods }
     * @since 2022-04-27 16:43:36
     */
    KanjiaActivityGoods getKanjiaGoodsBySkuId(String skuId);

    /**
     * 修改看见商品信息
     *
     * @param kanjiaActivityGoodsDTO 砍价商品信息
     * @return boolean
     * @since 2022-04-27 16:43:36
     */
    boolean updateKanjiaActivityGoods(KanjiaActivityGoodsDTO kanjiaActivityGoodsDTO);

    /**
     * 删除砍价商品
     *
     * @param ids 砍价商品ids
     * @return boolean
     * @since 2022-04-27 16:43:36
     */
    boolean deleteKanJiaGoods(List<String> ids);
}
