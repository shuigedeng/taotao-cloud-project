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

package com.taotao.cloud.sys.biz.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.cloud.sys.biz.model.vo.dept.DeptTreeVO;
import com.taotao.cloud.sys.biz.mapper.IDeptMapper;
import com.taotao.cloud.sys.biz.model.convert.DeptConvert;
import com.taotao.cloud.sys.biz.model.entity.system.Dept;
import com.taotao.cloud.sys.biz.repository.cls.DeptRepository;
import com.taotao.cloud.sys.biz.repository.inf.IDeptRepository;
import com.taotao.cloud.sys.biz.service.business.IDeptService;
import com.taotao.boot.webagg.service.impl.BaseSuperServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * DeptServiceImpl
 *
 * @author shuigedeng
 * @since 2020-10-16 15:54:05
 * @since 1.0
 */
@Service
public class DeptServiceImpl extends BaseSuperServiceImpl< Dept, Long,IDeptMapper, DeptRepository, IDeptRepository>
	implements IDeptService {

	@Override
	public List<DeptTreeVO> tree() {
		LambdaQueryWrapper<Dept> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.orderByDesc(Dept::getSortNum);
		List<Dept> list = list(queryWrapper);

		return DeptConvert.INSTANCE.convertTree(list)
			.stream()
			.filter(Objects::nonNull)
			.peek(e -> {
				e.setKey(e.getId());
				e.setValue(e.getId());
				e.setTitle(e.getName());
			})
			.toList();
	}
}
