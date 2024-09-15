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

package com.taotao.cloud.goods.infrastructure.persistent.mapper;

import com.taotao.cloud.goods.biz.model.vo.CategoryBrandVO;
import com.taotao.cloud.goods.infrastructure.persistent.po.CategoryBrandPO;
import com.taotao.boot.web.base.mapper.BaseSuperMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 商品分类品牌数据处理层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:55:23
 */
public interface ICategoryBrandMapper extends BaseSuperMapper<CategoryBrandPO, Long> {

	/**
	 * 根据分类id查分类绑定品牌
	 *
	 * @param categoryId 分类id
	 * @return {@link List }<{@link CategoryBrandVO }>
	 * @since 2022-04-27 16:55:23
	 */
	@Select("""
		SELECT b.id,b.name,b.logo
		FROM tt_brand b INNER join tt_category_brand cb on b.id = cb.brand_id and cb.category_id = #{categoryId}
		where b.delete_flag = 0
		""")
	List<CategoryBrandVO> getCategoryBrandList(@Param(value = "categoryId") Long categoryId);
}
