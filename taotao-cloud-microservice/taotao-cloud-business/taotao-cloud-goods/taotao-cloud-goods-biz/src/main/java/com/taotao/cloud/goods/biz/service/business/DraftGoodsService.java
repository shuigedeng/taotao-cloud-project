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

package com.taotao.cloud.goods.biz.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.goods.biz.model.dto.DraftGoodsSkuParamsDTO;
import com.taotao.cloud.goods.biz.model.page.DraftGoodsPageQuery;
import com.taotao.cloud.goods.biz.model.vo.DraftGoodsSkuParamsVO;
import com.taotao.cloud.goods.biz.model.entity.DraftGoods;
import com.taotao.boot.webagg.service.BaseSuperService;

/**
 * 草稿商品业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:59:56
 */
public interface DraftGoodsService extends BaseSuperService<DraftGoods, Long> {

    /**
     * 添加草稿商品
     *
     * @param draftGoods 草稿商品
     * @return {@link boolean }
     * @since 2022-04-27 16:59:56
     */
    boolean addGoodsDraft(DraftGoodsSkuParamsDTO draftGoods);

    /**
     * 更新草稿商品
     *
     * @param draftGoods 草稿商品
     * @return {@link boolean }
     * @since 2022-04-27 16:59:56
     */
    boolean updateGoodsDraft(DraftGoodsSkuParamsDTO draftGoods);

    /**
     * 保存草稿商品
     *
     * @param draftGoodsVO 草稿商品
     * @return {@link boolean }
     * @since 2022-04-27 16:59:56
     */
    boolean saveGoodsDraft(DraftGoodsSkuParamsDTO draftGoodsVO);

    /**
     * 根据ID删除草稿商品
     *
     * @param id 草稿商品ID
     * @return {@link boolean }
     * @since 2022-04-27 16:59:56
     */
    boolean deleteGoodsDraft(Long id);

    /**
     * 获取草稿商品详情
     *
     * @param id 草稿商品ID
     * @return {@link DraftGoodsSkuParamsVO }
     * @since 2022-04-27 16:59:57
     */
    DraftGoodsSkuParamsVO getDraftGoods(Long id);

    /**
     * 分页获取草稿商品
     *
     * @param searchParams 查询参数
     * @return {@link IPage }<{@link DraftGoods }>
     * @since 2022-04-27 16:59:57
     */
    IPage<DraftGoods> draftGoodsQueryPage(DraftGoodsPageQuery searchParams);
}
