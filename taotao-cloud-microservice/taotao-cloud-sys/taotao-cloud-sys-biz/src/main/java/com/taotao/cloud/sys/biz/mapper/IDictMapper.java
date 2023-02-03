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
package com.taotao.cloud.sys.biz.mapper;

import com.taotao.cloud.sys.biz.model.bo.DictDeptBO;
import com.taotao.cloud.sys.biz.model.entity.dict.Dict;
import com.taotao.cloud.sys.biz.model.params.DictDeptParams;
import com.taotao.cloud.web.base.mapper.BaseSuperMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * CompanyMapper
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/10/13 22:50
 */

public interface IDictMapper extends BaseSuperMapper<Dict, Long> {

	@Select("""
		select dict.id as id,
		dict.description as description,
		dict.itemValue as itemValue,
		dict.itemText as itemText,
		dept.id as deptId,
		dept.parentId as parentId
		from tt_dict dict left join tt_dept dept on dict.id = dept.id
		where dict.id in #{params.ids}
		""")
	List<DictDeptBO> testMybatisQueryStructure(@Param("params") DictDeptParams params);
}
