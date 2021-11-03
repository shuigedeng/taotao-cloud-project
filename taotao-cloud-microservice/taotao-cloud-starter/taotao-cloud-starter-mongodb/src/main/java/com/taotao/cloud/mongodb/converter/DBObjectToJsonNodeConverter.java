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

package com.taotao.cloud.mongodb.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.taotao.cloud.common.utils.JsonUtil;
import org.bson.BasicBSONObject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.Nullable;

/**
 * mongo DBObject 转 jsonNode
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:01:42
 */
@ReadingConverter
public enum DBObjectToJsonNodeConverter implements Converter<BasicBSONObject, JsonNode> {
	/**
	 * 实例
	 */
	INSTANCE;

	@Nullable
	@Override
	public JsonNode convert(@Nullable BasicBSONObject source) {
		if (source == null) {
			return null;
		}
		return JsonUtil.MAPPER.valueToTree(source);
	}
}

