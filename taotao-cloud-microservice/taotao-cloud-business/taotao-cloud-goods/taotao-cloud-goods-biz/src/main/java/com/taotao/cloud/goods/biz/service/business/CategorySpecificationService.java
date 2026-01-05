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

import com.taotao.cloud.goods.biz.model.entity.CategorySpecification;
import com.taotao.cloud.goods.biz.model.entity.Specification;
import com.taotao.boot.webagg.service.BaseSuperService;
import java.util.List;

/**
 * 商品分类规格业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:59:40
 */
public interface CategorySpecificationService extends BaseSuperService<CategorySpecification, Long> {

    /**
     * 根据分类id查询规格信息
     *
     * @param categoryId 分类id
     * @return {@link List }<{@link Specification }>
     * @since 2022-04-27 16:59:40
     */
    List<Specification> getCategorySpecList(Long categoryId);

    /**
     * 通过分类ID删除关联规格
     *
     * @param categoryId 分类ID
     * @return {@link boolean }
     * @since 2022-04-27 16:59:40
     */
    boolean deleteByCategoryId(Long categoryId);
}
