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

package com.taotao.cloud.goods.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.goods.application.command.draft.dto.DraftGoodsPageQry;
import com.taotao.cloud.goods.application.command.draft.dto.DraftGoodsSkuParamsAddCmd;
import com.taotao.cloud.goods.application.command.draft.dto.DraftGoodsSkuParamsSaveCmd;
import com.taotao.cloud.goods.application.command.draft.dto.DraftGoodsSkuParamsUpdateCmd;
import com.taotao.cloud.goods.application.command.draft.dto.clientobject.DraftGoodsSkuParamsCO;
import com.taotao.cloud.goods.infrastructure.persistent.po.DraftGoodsPO;
import com.taotao.boot.web.base.service.BaseSuperService;

/**
 * 草稿商品业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:59:56
 */
public interface IDraftGoodsService extends BaseSuperService<DraftGoodsPO, Long> {

    /**
     * 添加草稿商品
     *
     * @param draftGoods 草稿商品
     * @return {@link boolean }
     * @since 2022-04-27 16:59:56
     */
    boolean addGoodsDraft(DraftGoodsSkuParamsAddCmd draftGoods);

    /**
     * 更新草稿商品
     *
     * @param draftGoods 草稿商品
     * @return {@link boolean }
     * @since 2022-04-27 16:59:56
     */
    boolean updateGoodsDraft(DraftGoodsSkuParamsUpdateCmd draftGoods);

    /**
     * 保存草稿商品
     *
     * @param draftGoodsVO 草稿商品
     * @return {@link boolean }
     * @since 2022-04-27 16:59:56
     */
    boolean saveGoodsDraft(DraftGoodsSkuParamsSaveCmd draftGoodsVO);

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
     * @return {@link DraftGoodsSkuParamsCO }
     * @since 2022-04-27 16:59:57
     */
    DraftGoodsSkuParamsCO getDraftGoods(Long id);

    /**
     * 分页获取草稿商品
     *
     * @param searchParams 查询参数
     * @return {@link IPage }<{@link DraftGoodsPO }>
     * @since 2022-04-27 16:59:57
     */
    IPage<DraftGoodsPO> draftGoodsQueryPage(DraftGoodsPageQry searchParams);
}
