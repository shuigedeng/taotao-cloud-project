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
package com.taotao.cloud.elasticsearch.builder;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.utils.JsonUtil;
import com.taotao.cloud.core.model.PageModel;
import com.taotao.cloud.core.utils.BeanUtil;
import lombok.Data;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ES查询Builder
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/5/3 06:49
 */
@Data
public class SearchBuilder {

	/**
	 * 高亮前缀
	 */
	private static final String HIGHLIGHTER_PRE_TAGS = "<mark>";

	/**
	 * 高亮后缀
	 */
	private static final String HIGHLIGHTER_POST_TAGS = "</mark>";

	private SearchRequest searchRequest;
	private SearchSourceBuilder searchBuilder;
	private RestHighLevelClient client;

	private SearchBuilder(SearchRequest searchRequest, SearchSourceBuilder searchBuilder,
		RestHighLevelClient client) {
		this.searchRequest = searchRequest;
		this.searchBuilder = searchBuilder;
		this.client = client;
	}

	/**
	 * 生成SearchBuilder实例
	 *
	 * @param elasticsearchTemplate elasticsearchTemplate
	 * @param indexName             indexName
	 * @return com.taotao.cloud.elasticsearch.builder.SearchBuilder
	 * @author dengtao
	 * @since 2021/2/26 08:50
	 */
	public static SearchBuilder builder(ElasticsearchRestTemplate elasticsearchTemplate,
		String indexName) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		SearchRequest searchRequest = new SearchRequest(indexName);
		searchRequest.source(searchSourceBuilder);
		RestHighLevelClient client = BeanUtil.getBean(RestHighLevelClient.class, true);
		return new SearchBuilder(searchRequest, searchSourceBuilder, client);
	}

	/**
	 * 生成SearchBuilder实例
	 *
	 * @param elasticsearchTemplate elasticsearchTemplate
	 * @return com.taotao.cloud.elasticsearch.builder.SearchBuilder
	 * @author dengtao
	 * @since 2021/2/26 08:50
	 */
	public static SearchBuilder builder(ElasticsearchRestTemplate elasticsearchTemplate) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.source(searchSourceBuilder);
		RestHighLevelClient client = BeanUtil.getBean(RestHighLevelClient.class, true);
		return new SearchBuilder(searchRequest, searchSourceBuilder, client);
	}

	/**
	 * 设置索引名
	 *
	 * @param indices 索引名数组
	 * @return com.taotao.cloud.elasticsearch.builder.SearchBuilder
	 * @author dengtao
	 * @since 2021/2/26 08:50
	 */
	public SearchBuilder setIndices(String... indices) {
		if (ArrayUtil.isNotEmpty(indices)) {
			searchRequest.indices(indices);
		}
		return this;
	}

	/**
	 * 生成queryStringQuery查询
	 *
	 * @param queryStr 查询关键字
	 * @return com.taotao.cloud.elasticsearch.builder.SearchBuilder
	 * @author dengtao
	 * @since 2021/2/26 08:50
	 */
	public SearchBuilder setStringQuery(String queryStr) {
		QueryBuilder queryBuilder;
		if (StrUtil.isNotEmpty(queryStr)) {
			queryBuilder = QueryBuilders.queryStringQuery(queryStr);
		} else {
			queryBuilder = QueryBuilders.matchAllQuery();
		}
		searchBuilder.query(queryBuilder);
		return this;
	}

	/**
	 * 设置分页
	 *
	 * @param page  当前页数
	 * @param limit 每页显示数
	 * @return com.taotao.cloud.elasticsearch.builder.SearchBuilder
	 * @author dengtao
	 * @since 2021/2/26 08:50
	 */
	public SearchBuilder setPage(Integer page, Integer limit) {
		if (page != null && limit != null) {
			searchBuilder.from((page - 1) * limit)
				.size(limit);
		}
		return this;
	}

	/**
	 * 增加排序
	 *
	 * @param field 排序字段
	 * @param order 顺序方向
	 * @return com.taotao.cloud.elasticsearch.builder.SearchBuilder
	 * @author dengtao
	 * @since 2021/2/26 08:51
	 */
	public SearchBuilder addSort(String field, SortOrder order) {
		if (StrUtil.isNotEmpty(field) && order != null) {
			searchBuilder.sort(field, order);
		}
		return this;
	}

	/**
	 * 设置高亮
	 *
	 * @param preTags  高亮处理前缀
	 * @param postTags 高亮处理后缀
	 * @return com.taotao.cloud.elasticsearch.builder.SearchBuilder
	 * @author dengtao
	 * @since 2021/2/26 08:51
	 */
	public SearchBuilder setHighlight(String field, String preTags, String postTags) {
		if (StrUtil.isNotEmpty(field) && StrUtil.isNotEmpty(preTags) && StrUtil
			.isNotEmpty(postTags)) {
			HighlightBuilder highlightBuilder = new HighlightBuilder();
			highlightBuilder.field(field)
				.preTags(preTags)
				.postTags(postTags);
			searchBuilder.highlighter(highlightBuilder);
		}
		return this;
	}

	/**
	 * 设置是否需要高亮处理
	 *
	 * @param isHighlighter 是否需要高亮处理
	 * @return com.taotao.cloud.elasticsearch.builder.SearchBuilder
	 * @author dengtao
	 * @since 2021/2/26 08:51
	 */
	public SearchBuilder setIsHighlight(Boolean isHighlighter) {
		if (BooleanUtil.isTrue(isHighlighter)) {
			this.setHighlight("*"
				, HIGHLIGHTER_PRE_TAGS, HIGHLIGHTER_POST_TAGS);
		}
		return this;
	}

	/**
	 * 设置查询路由
	 *
	 * @param routing 路由数组
	 * @return com.taotao.cloud.elasticsearch.builder.SearchBuilder
	 * @author dengtao
	 * @since 2021/2/26 08:52
	 */
	public SearchBuilder setRouting(String... routing) {
		if (ArrayUtil.isNotEmpty(routing)) {
			searchRequest.routing(routing);
		}
		return this;
	}

	/**
	 * 返回结果
	 *
	 * @return org.elasticsearch.action.search.SearchResponse
	 * @author dengtao
	 * @since 2021/2/26 08:54
	 */
	public SearchResponse get() throws IOException {
		return client.search(searchRequest, RequestOptions.DEFAULT);
	}

	/**
	 * 返回列表结果
	 *
	 * @return java.util.List<java.lang.String>
	 * @author dengtao
	 * @since 2021/2/26 08:54
	 */
	public List<String> getList() throws IOException {
		return getList(this.get().getHits());
	}

	/**
	 * 返回分页结果
	 *
	 * @return com.taotao.cloud.core.model.PageResult<java.lang.String>
	 * @author dengtao
	 * @since 2021/2/26 08:54
	 */
	public PageModel<String> getPage() throws IOException {
		return this.getPage(null, null);
	}

	/**
	 * 返回分页结果
	 *
	 * @param page  当前页数
	 * @param limit 每页显示
	 * @return com.taotao.cloud.core.model.PageResult<java.lang.String>
	 * @author dengtao
	 * @since 2021/2/26 08:54
	 */
	public PageModel<String> getPage(Integer page, Integer limit) throws IOException {
		this.setPage(page, limit);
		SearchResponse response = this.get();
		SearchHits searchHits = response.getHits();
		TotalHits totalCnt = searchHits.getTotalHits();
		List<String> list = getList(searchHits);
		return PageModel.succeed(totalCnt.value, 1, 10, list);
	}

	/**
	 * 返回JSON列表数据
	 *
	 * @param searchHits searchHits
	 * @return java.util.List<java.lang.String>
	 * @author dengtao
	 * @since 2021/2/26 08:55
	 */
	private List<String> getList(SearchHits searchHits) {
		List<String> list = new ArrayList<>();
		if (searchHits != null) {
			searchHits.forEach(item -> {
				Map map = JsonUtil.toMap(item.getSourceAsString());
				map.put("id", item.getId());

				Map<String, HighlightField> highlightFields = item.getHighlightFields();
				if (highlightFields != null) {
					populateHighLightedFields(map, highlightFields);
				}
				String str = JsonUtil.toJSONString(map);
				list.add(str);
			});
		}
		return list;
	}

	/**
	 * 组装高亮字符
	 *
	 * @param result          目标对象
	 * @param highlightFields 高亮配置
	 * @author dengtao
	 * @since 2021/2/26 08:55
	 */
	private <T> void populateHighLightedFields(T result,
		Map<String, HighlightField> highlightFields) {
		for (HighlightField field : highlightFields.values()) {
			String name = field.getName();
			if (!name.endsWith(".keyword")) {

			}
		}
	}

	/**
	 * 拼凑数组为字符串
	 *
	 * @param texts texts
	 * @return java.lang.String
	 * @author dengtao
	 * @since 2021/2/26 08:55
	 */
	private String concat(Text[] texts) {
		StringBuilder sb = new StringBuilder();
		for (Text text : texts) {
			sb.append(text.toString());
		}
		return sb.toString();
	}
}
