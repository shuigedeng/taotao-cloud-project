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

package com.taotao.cloud.sys.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.sys.biz.model.entity.SysLogLogin;
import com.taotao.cloud.sys.biz.service.SysLogLoginService;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.stereotype.Service;

/**
 * @program: logs
 * @description:
 * @author: Sinda
 * @create: 2022-03-19 20:42:34
 */
@Service
public class SysLogLoginServiceImpl implements SysLogLoginService {

	@Override
	public boolean saveBatch(Collection<SysLogLogin> entityList, int batchSize) {
		return false;
	}

	@Override
	public boolean saveOrUpdateBatch(Collection<SysLogLogin> entityList, int batchSize) {
		return false;
	}

	@Override
	public boolean updateBatchById(Collection<SysLogLogin> entityList, int batchSize) {
		return false;
	}

	@Override
	public boolean saveOrUpdate(SysLogLogin entity) {
		return false;
	}

	@Override
	public SysLogLogin getOne(Wrapper<SysLogLogin> queryWrapper, boolean throwEx) {
		return null;
	}

	@Override
	public Optional<SysLogLogin> getOneOpt(Wrapper<SysLogLogin> queryWrapper, boolean throwEx) {
		return Optional.empty();
	}

	@Override
	public Map<String, Object> getMap(Wrapper<SysLogLogin> queryWrapper) {
		return Map.of();
	}

	@Override
	public <V> V getObj(Wrapper<SysLogLogin> queryWrapper, Function<? super Object, V> mapper) {
		return null;
	}

	@Override
	public BaseMapper<SysLogLogin> getBaseMapper() {
		return null;
	}

	@Override
	public Class<SysLogLogin> getEntityClass() {
		return null;
	}
}
