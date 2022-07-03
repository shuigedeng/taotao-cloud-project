/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.jetcache.jackson;


import com.alicp.jetcache.support.CacheEncodeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.function.Function;

/**
 * JacksonKey Convertor
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-03 09:50:45
 */
public class JacksonKeyConvertor implements Function<Object, Object> {
	private final ObjectMapper mapper;

	public JacksonKeyConvertor(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public Object apply(Object originalKey) {
		if (originalKey == null) {
			return null;
		}
		if (originalKey instanceof String) {
			return originalKey;
		}
		try {
			return mapper.writeValueAsString(originalKey);
		} catch (JsonProcessingException e) {
			throw new CacheEncodeException("Key convertor error", e);
		}
	}

}

