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
package com.taotao.cloud.operation.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.operation.api.feign.IFeignArticleService;
import com.taotao.cloud.operation.api.vo.ArticleVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * FeignArticleServiceFallback
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignArticleServiceFallback implements FallbackFactory<IFeignArticleService> {
	@Override
	public IFeignArticleService create(Throwable throwable) {
		return new IFeignArticleService() {
			@Override
			public Result<ArticleVO> getMemberSecurityUser(Long id) {
				LogUtil.error("调用getMemberSecurityUser异常：{}", throwable, id);
				return Result.fail(null, 500);
			}
		};
	}
}
