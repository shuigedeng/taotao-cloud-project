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

package com.taotao.cloud.goods.application.convert;

import com.taotao.cloud.goods.biz.model.vo.CategoryTreeVO;
import com.taotao.cloud.goods.biz.model.vo.CategoryVO;
import com.taotao.cloud.goods.biz.model.entity.Category;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * ICategoryMapStruct
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:58:05
 */
@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryConvert {

    /** 实例 */
    CategoryConvert INSTANCE = Mappers.getMapper(CategoryConvert.class);

    /**
     * 类别,类别签证官
     *
     * @param category 类别
     * @return {@link CategoryTreeVO }
     * @since 2022-04-27 16:58:05
     */
    CategoryTreeVO convert(Category category);

    /**
     * 类别基础vos思想史范畴
     *
     * @param categorys 思想史范畴
     * @return {@link List }<{@link CategoryVO }>
     * @since 2022-04-27 16:58:05
     */
    List<CategoryVO> convert(List<Category> categorys);
}
