/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.sys.biz.mapstruct;

import com.taotao.cloud.sys.api.vo.dept.DeptTreeVO;
import com.taotao.cloud.sys.biz.entity.system.Dept;
import java.util.List;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/11/11 16:58
 */
@Mapper(builder = @Builder(disableBuilder = true),
	unmappedSourcePolicy = ReportingPolicy.IGNORE,
	unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeptMapStruct {

	DeptMapStruct INSTANCE = Mappers.getMapper(DeptMapStruct.class);

	/**
	 * deptListToVoList
	 *
	 * @param deptList deptList
	 * @return {@link List&lt;com.taotao.cloud.sys.api.vo.dept.DepartVO&gt; }
	 * @author shuigedeng
	 * @since 2021-12-22 21:20:33
	 */
	List<DeptTreeVO> deptListToVoList(List<Dept> deptList);


}
