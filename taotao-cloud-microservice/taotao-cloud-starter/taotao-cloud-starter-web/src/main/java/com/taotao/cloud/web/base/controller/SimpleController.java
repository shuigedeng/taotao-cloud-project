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
package com.taotao.cloud.web.base.controller;

import com.taotao.cloud.web.base.entity.SuperEntity;
import com.taotao.cloud.web.base.service.BaseSuperService;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 简单的实现了BaseController，为了获取注入 Service 和 实体类型
 * <p>
 * 基类该类后，没有任何方法。 可以让业务Controller继承 SuperSimpleController 后，按需实现 *Controller 接口
 *
 * @param <S>      Service
 * @param <Entity> 实体
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:15:37
 */
public abstract class SimpleController<S extends BaseSuperService<T, I>,
	T extends SuperEntity<I>,
	I extends Serializable>
	implements BaseController<T, I> {

	protected Class<T> entityClass = null;

	@Autowired
	protected S baseService;

	@Override
	public Class<T> getEntityClass() {
		if (entityClass == null) {
			this.entityClass = (Class<T>) ((ParameterizedType) this.getClass()
				.getGenericSuperclass()).getActualTypeArguments()[1];
		}
		return this.entityClass;
	}

	@Override
	public BaseSuperService<T, I> getBaseService() {
		return baseService;
	}
}
