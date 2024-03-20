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

package com.taotao.cloud.goods.application.config.aop;

import com.taotao.cloud.common.utils.log.LogUtils;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 商品操作日志
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:56:40
 */
@Aspect
@Component
@AllArgsConstructor
public class GoodsOperationAspect {

	private final ApplicationEventPublisher publisher;

	@After("@annotation(com.taotao.cloud.goods.biz.aop.GoodsLogPoint)")
	public void doAfter(JoinPoint joinPoint) {
		LogUtils.info("");
	}
}
