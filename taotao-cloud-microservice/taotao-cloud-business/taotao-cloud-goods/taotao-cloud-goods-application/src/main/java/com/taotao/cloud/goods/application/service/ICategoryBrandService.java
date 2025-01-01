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

import com.taotao.cloud.goods.application.command.category.dto.clientobject.CategoryBrandCO;
import com.taotao.cloud.goods.infrastructure.persistent.po.CategoryBrandPO;
import com.taotao.boot.webagg.service.BaseSuperService;
import java.util.List;

/**
 * 商品分类品牌业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:59:19
 */
public interface ICategoryBrandService extends BaseSuperService<CategoryBrandPO, Long> {

    /**
     * 根据分类id查询品牌信息
     *
     * @param categoryId 分类id
     * @return {@link List }<{@link CategoryBrandCO }>
     * @since 2022-04-27 16:59:19
     */
    List<CategoryBrandCO> getCategoryBrandList(Long categoryId);

    /**
     * 通过分类ID删除关联品牌
     *
     * @param categoryId 品牌ID
     * @return {@link boolean }
     * @since 2022-04-27 16:59:19
     */
    boolean deleteByCategoryId(Long categoryId);

    /**
     * 根据品牌ID获取分类品牌关联信息
     *
     * @param brandId 品牌ID
     * @return {@link List }<{@link CategoryBrandPO }>
     * @since 2022-04-27 16:59:19
     */
    List<CategoryBrandPO> getCategoryBrandListByBrandId(List<Long> brandId);

    /**
     * 保存分类品牌关系
     *
     * @param categoryId 分类id
     * @param brandIds 品牌ids
     * @return {@link boolean }
     * @since 2022-04-27 16:59:19
     */
    boolean saveCategoryBrandList(Long categoryId, List<Long> brandIds);
}
