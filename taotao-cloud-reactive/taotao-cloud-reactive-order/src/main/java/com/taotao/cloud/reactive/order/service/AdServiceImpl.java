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
package com.taotao.cloud.reactive.order.service;

import com.taotao.cloud.reactive.order.bean.AdVo;
import com.taotao.cloud.reactive.order.repository.AdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * AdServiceImpl
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/03/03 16:28
 */
@Service
public class AdServiceImpl {

	@Autowired
	private AdRepository adRepository;

	public Flux<AdVo> findAll() {
		return adRepository.findAll()
			.map(adBean -> {
					AdVo vo = new AdVo();
					vo.setName(adBean.getName());
					return vo;
				}
			);
	}
}
