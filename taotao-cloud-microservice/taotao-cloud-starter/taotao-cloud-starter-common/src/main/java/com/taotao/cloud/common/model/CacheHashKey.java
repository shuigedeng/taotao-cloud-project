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
package com.taotao.cloud.common.model;

import static com.taotao.cloud.common.base.StrPool.COLON;

import cn.hutool.core.util.StrUtil;
import java.time.Duration;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.NonNull;

/**
 * hash 缓存 key 封装
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/5/2 16:56
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CacheHashKey extends CacheKey {

	/**
	 * redis hash field
	 */
	@NonNull
	private Object field;

	public CacheHashKey(@NonNull String key, final @NonNull Object field) {
		super(key);
		this.field = field;
	}

	public CacheHashKey(@NonNull String key, final @NonNull Object field, Duration expire) {
		super(key, expire);
		this.field = field;
	}

	public CacheKey tran() {
		return new CacheKey(StrUtil.join(COLON, getKey(), getField()), getExpire());
	}
}
