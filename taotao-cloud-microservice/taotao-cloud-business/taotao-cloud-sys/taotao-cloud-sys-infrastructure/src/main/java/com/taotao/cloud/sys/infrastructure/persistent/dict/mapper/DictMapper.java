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

package com.taotao.cloud.sys.infrastructure.persistent.dict.mapper;

import com.taotao.cloud.sys.infrastructure.persistent.dict.dataobject.DictDeptDO;
import com.taotao.cloud.sys.infrastructure.persistent.dict.params.DictDeptParams;
import com.taotao.cloud.sys.infrastructure.persistent.dict.po.DictPO;
import com.taotao.boot.web.base.mapper.BaseSuperMapper;
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
public interface DictMapper extends BaseSuperMapper<DictPO, Long> {

	@Select(
		"""
			select dictPO.id as id,
			dictPO.description as description,
			dictPO.dict_name as itemValue,
			dictPO.dict_code as itemText
			from tt_dict dictPO
			""")
	List<DictDeptDO> testMybatisQueryStructure(@Param("params") DictDeptParams params);
}
