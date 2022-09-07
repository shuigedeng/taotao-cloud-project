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
package com.taotao.cloud.elasticsearch.service.impl;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.common.utils.context.ContextUtils;
import com.taotao.cloud.elasticsearch.model.IndexDto;
import com.taotao.cloud.elasticsearch.service.IIndexService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

/**
 * 索引服务实现
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/6/15 11:27
 */
public class IndexServiceImpl implements IIndexService {

	@Autowired(required = false)
	private ElasticsearchRestTemplate elasticsearchRestTemplate;

	private final ObjectMapper mapper = new ObjectMapper();

	private final RestHighLevelClient client = ContextUtils.getBean(RestHighLevelClient.class, true);

	@Override
	public boolean create(IndexDto indexDto) throws IOException {
		CreateIndexRequest request = new CreateIndexRequest(indexDto.getIndexName());
		request.settings(Settings.builder()
			.put("index.number_of_shards", indexDto.getNumberOfShards())
			.put("index.number_of_replicas", indexDto.getNumberOfReplicas())
		);
		if (StrUtil.isNotEmpty(indexDto.getType()) && StrUtil
			.isNotEmpty(indexDto.getMappingsSource())) {
			//mappings
			request.mapping(indexDto.getType(), indexDto.getMappingsSource(), XContentType.JSON);
		}

		CreateIndexResponse response = client
			.indices()
			.create(request, RequestOptions.DEFAULT);
		return response.isAcknowledged();
	}

	@Override
	public boolean delete(String indexName) throws IOException {
		DeleteIndexRequest request = new DeleteIndexRequest(indexName);
		AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
		return response.isAcknowledged();
	}

	@Override
	public PageResult<HashMap<String, String>> list(String queryStr, String indices)
		throws IOException {
		Response response = client.getLowLevelClient()
			.performRequest(new Request(
				"GET",
				"/_cat/indices?h=health,status,index,docsCount,docsDeleted,storeSize&s=cds:desc&format=json&index="
					+ StrUtil.nullToEmpty(indices)
			));

		List<HashMap<String, String>> listOfIndicesFromEs = null;
		if (response != null) {
			String rawBody = EntityUtils.toString(response.getEntity());
			TypeReference<List<HashMap<String, String>>> typeRef = new TypeReference<List<HashMap<String, String>>>() {
			};
			listOfIndicesFromEs = mapper.readValue(rawBody, typeRef);
		}
		return PageResult.of(100, 1, 10,  30, listOfIndicesFromEs);
	}

	/**
	 * bytes 转换为 kb
	 */
	private Double getKB(Long bytes) {
		if (bytes == null) {
			return 0D;
		}
		return bytes / 1024D;
	}

	@Override
	public Map<String, Object> show(String indexName) throws IOException {
		// GetIndexRequest request = new GetIndexRequest();
		// request.indices(indexName);
		// GetIndexResponse getIndexResponse = client
		// 	.indices().get(request, RequestOptions.DEFAULT);
		// ImmutableOpenMap<String, MappingMetaData> mappOpenMap = getIndexResponse.getMappings().get(indexName);
		// List<AliasMetaData> indexAliases = getIndexResponse.getAliases().get(indexName);
		//
		// String settingsStr = getIndexResponse.getSettings().get(indexName).toString();
		// Object settingsObj = null;
		// if (StrUtil.isNotEmpty(settingsStr)) {
		// 	settingsObj = JsonUtil.toJSONString(settingsStr);
		// }
		// Map<String, Object> result = new HashMap<>(1);
		// Map<String, Object> indexMap = new HashMap<>(3);
		// Map<String, Object> mappMap = new HashMap<>(mappOpenMap.size());
		// List<String> aliasesList = new ArrayList<>(indexAliases.size());
		// indexMap.put("aliases", aliasesList);
		// indexMap.put("settings", settingsObj);
		// indexMap.put("mappings", mappMap);
		// result.put(indexName, indexMap);
		// //获取mappings数据
		// for (ObjectCursor<String> key : mappOpenMap.keys()) {
		// 	MappingMetaData data = mappOpenMap.get(key.value);
		// 	Map<String, Object> dataMap = data.getSourceAsMap();
		// 	mappMap.put(key.value, dataMap);
		// }
		// //获取aliases数据
		// for (AliasMetaData aliases : indexAliases) {
		// 	aliasesList.add(aliases.getAlias());
		// }
		Map<String, Object> result = new HashMap<>(1);
		return result;
	}
}
