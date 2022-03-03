package com.taotao.cloud.sys.biz.service.impl;


import com.taotao.cloud.sys.biz.service.ISearchService;
import org.springframework.stereotype.Service;


/**
 * SearchServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-03 09:37:30
 */
@Service
public class SearchServiceImpl implements ISearchService {

	//@Autowired(required = false)
	//RestHighLevelClient client;
	//
	//@Override
	//public SearchResponse queryString(ElasticSearchRequest request) {
	//	SearchRequest searchRequest = new SearchRequest(request.getQuery().getIndexname());
	//	// 如果关键词为空，则返回所有
	//	String content = request.getQuery().getKeyWords();
	//	Integer rows = request.getQuery().getRows();
	//	if (rows == null || rows == 0) {
	//		rows = 10;
	//	}
	//	Integer start = request.getQuery().getStart();
	//	if (content == null || "".equals(content)) {
	//		// 查询所有
	//		content = "*";
	//	}
	//	SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	//	// 提取搜索内容
	//	BoolQueryBuilder builder;
	//	if ("*".equalsIgnoreCase(content)) {
	//		builder = QueryBuilders.boolQuery()
	//			.must(QueryBuilders.queryStringQuery(content).defaultOperator(Operator.AND));
	//	} else {
	//		builder = QueryBuilders.boolQuery().must(
	//			QueryBuilders.queryStringQuery(ToolUtils.handKeyword(content))
	//				.defaultOperator(Operator.AND));
	//	}
	//	// 提取过滤条件
	//	FilterCommand filter = request.getFilter();
	//	if (filter != null) {
	//		if (filter.getStartdate() != null && filter.getEnddate() != null) {
	//			builder.filter(
	//				QueryBuilders.rangeQuery(filter.getField()).from(filter.getStartdate())
	//					.to(filter.getEnddate()));
	//		}
	//	}
	//	// 排序
	//	if (StringUtils.isNoneBlank(request.getQuery().getSort())) {
	//		searchSourceBuilder.sort(request.getQuery().getSort(), SortOrder.ASC);
	//	}
	//	// 查询全部
	//	searchSourceBuilder.trackTotalHits(true);
	//	searchSourceBuilder.query(builder);
	//	// 处理高亮
	//	HighlightBuilder highlightBuilder = new HighlightBuilder();
	//	highlightBuilder.field("*");
	//	searchSourceBuilder.highlighter(highlightBuilder);
	//	searchSourceBuilder.from(start);
	//	searchSourceBuilder.size(rows);
	//
	//	searchRequest.source(searchSourceBuilder);
	//	SearchResponse searchResponse = null;
	//	try {
	//		searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
	//	} catch (IOException e) {
	//		// TODO Auto-generated catch block
	//		e.printStackTrace();
	//	}
	//	return searchResponse;
	//}
	//
	//
	//@Override
	//public SearchResponse termSearch(String index, String field, String term) {
	//	SearchRequest searchRequest = new SearchRequest(index);
	//	BoolQueryBuilder builder = QueryBuilders.boolQuery()
	//		.must(QueryBuilders.termQuery(field, term));
	//	SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	//	searchSourceBuilder.query(builder);
	//	searchRequest.source(searchSourceBuilder);
	//	SearchResponse searchResponse = null;
	//	try {
	//		searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
	//	} catch (IOException e) {
	//		// TODO Auto-generated catch block
	//		e.printStackTrace();
	//	}
	//
	//	return searchResponse;
	//}
	//
	//@Override
	//public SearchResponse matchAllSearch(String index) {
	//	SearchRequest searchRequest = new SearchRequest(index);
	//	BoolQueryBuilder builder = QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery());
	//	SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	//	searchSourceBuilder.query(builder);
	//	searchSourceBuilder.trackTotalHits(true);
	//	searchRequest.source(searchSourceBuilder);
	//	SearchResponse searchResponse = null;
	//	try {
	//		searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
	//	} catch (IOException e) {
	//		// TODO Auto-generated catch block
	//		e.printStackTrace();
	//	}
	//	return searchResponse;
	//}
	//
	//
	//@Override
	//public SearchResponse geoDistanceSearch(String index, GeoDistance geo, Integer pagenum,
	//	Integer pagesize) {
	//	SearchRequest searchRequest = new SearchRequest("shop");
	//	SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	//	BoolQueryBuilder builder;
	//	builder = QueryBuilders.boolQuery().must(QueryBuilders.geoDistanceQuery("location")
	//		.point(geo.getLatitude(), geo.getLongitude())
	//		.distance(geo.getDistance(), DistanceUnit.KILOMETERS));
	//	SearchResponse searchResponse = null;
	//	try {
	//		searchSourceBuilder.query(builder);
	//		searchSourceBuilder.trackTotalHits(true);
	//		searchRequest.source(searchSourceBuilder);
	//		int start = (pagenum - 1) * pagesize;
	//		searchSourceBuilder.from(start);
	//		searchSourceBuilder.size(pagesize);
	//		searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
	//	} catch (IOException e) {
	//		// TODO Auto-generated catch block
	//		e.printStackTrace();
	//	}
	//	return searchResponse;
	//}
	//
	//@Override
	//public SearchResponse matchNestedObjectSearch(String path, String index, String field,
	//	String value, Integer pagenum, Integer pagesize) {
	//	SearchRequest searchRequest = new SearchRequest(index);
	//	BoolQueryBuilder builder = QueryBuilders.boolQuery()
	//		.must(QueryBuilders.nestedQuery(path, QueryBuilders.matchQuery(field, value),
	//			ScoreMode.None));
	//	SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	//	searchSourceBuilder.trackTotalHits(true);
	//	searchSourceBuilder.query(builder);
	//	int start = (pagenum - 1) * pagesize;
	//	searchSourceBuilder.from(start);
	//	searchSourceBuilder.size(pagesize);
	//	// 处理高亮
	//	HighlightBuilder highlightBuilder = new HighlightBuilder();
	//	highlightBuilder.field("*");
	//	searchSourceBuilder.highlighter(highlightBuilder);
	//	searchRequest.source(searchSourceBuilder);
	//	SearchResponse searchResponse = null;
	//	try {
	//		searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
	//	} catch (IOException e) {
	//		// TODO Auto-generated catch block
	//		e.printStackTrace();
	//	}
	//	return searchResponse;
	//}
	//
	//
	//@Override
	//public SearchResponse scrollquerystring(ElasticSearchRequest request) {
	//	SearchRequest searchRequest = new SearchRequest(request.getQuery().getIndexname());
	//	// 如果关键词为空，则返回所有
	//	String content = request.getQuery().getKeyWords();
	//	Integer rows = request.getQuery().getRows();
	//	if (rows == null || rows == 0) {
	//		rows = 10;
	//	}
	//	Integer start = request.getQuery().getStart();
	//	if (content == null || "".equals(content)) {
	//		// 查询所有
	//		content = "*";
	//	}
	//	SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	//	// 提取搜索内容
	//	BoolQueryBuilder builder;
	//	if ("*".equalsIgnoreCase(content)) {
	//		builder = QueryBuilders.boolQuery()
	//			.must(QueryBuilders.queryStringQuery(content).defaultOperator(Operator.AND));
	//	} else {
	//		builder = QueryBuilders.boolQuery().must(
	//			QueryBuilders.queryStringQuery(ToolUtils.handKeyword(content))
	//				.defaultOperator(Operator.AND));
	//	}
	//	// 提取过滤条件
	//	FilterCommand filter = request.getFilter();
	//	if (filter != null) {
	//		if (filter.getStartdate() != null && filter.getEnddate() != null) {
	//			builder.must(QueryBuilders.constantScoreQuery(
	//				QueryBuilders.rangeQuery(filter.getField()).from(filter.getStartdate())
	//					.to(filter.getEnddate())));
	//		}
	//	}
	//	// 排序
	//	if (StringUtils.isNoneBlank(request.getQuery().getSort())) {
	//		searchSourceBuilder.sort(request.getQuery().getSort(), SortOrder.ASC);
	//	}
	//	searchSourceBuilder.query(builder);
	//	searchSourceBuilder.trackTotalHits(true);
	//	// 处理高亮
	//	HighlightBuilder highlightBuilder = new HighlightBuilder();
	//	highlightBuilder.field("*");
	//	searchSourceBuilder.highlighter(highlightBuilder);
	//
	//	searchSourceBuilder.size(rows);
	//
	//	SearchResponse searchResponse = null;
	//
	//	if (request.getQuery().getScrollid() == null) {
	//		searchSourceBuilder.from(0);
	//		searchRequest.scroll(TimeValue.timeValueMinutes(5L));
	//		searchRequest.source(searchSourceBuilder);
	//		try {
	//			searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
	//		} catch (IOException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//	} else {
	//		SearchScrollRequest scrollRequest = new SearchScrollRequest(
	//			request.getQuery().getScrollid());
	//		scrollRequest.scroll(TimeValue.timeValueMinutes(5L));
	//		try {
	//			searchResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
	//		} catch (IOException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//	}
	//	return searchResponse;
	//}
	//
	//@Override
	//public SearchResponse hasChildSearch(String childtype, String index, String field, String value,
	//	Integer pagenum, Integer pagesize) {
	//	SearchRequest searchRequest = new SearchRequest(index);
	//	HasChildQueryBuilder builder;
	//	if (value != null && !("".equals(value))) {
	//		builder = JoinQueryBuilders.hasChildQuery(childtype,
	//			QueryBuilders.termQuery(field, value), ScoreMode.None);
	//	} else {
	//		builder = JoinQueryBuilders.hasChildQuery(childtype, QueryBuilders.matchAllQuery(),
	//			ScoreMode.None);
	//	}
	//	SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	//	searchSourceBuilder.query(builder);
	//	searchSourceBuilder.trackTotalHits(true);
	//	int start = (pagenum - 1) * pagesize;
	//	searchSourceBuilder.from(start);
	//	searchSourceBuilder.size(pagesize);
	//	searchRequest.source(searchSourceBuilder);
	//	SearchResponse searchResponse = null;
	//	try {
	//		searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
	//	} catch (IOException e) {
	//		// TODO Auto-generated catch block
	//		e.printStackTrace();
	//	}
	//	return searchResponse;
	//}
	//
	//
	//@Override
	//public SearchResponse hasParentSearch(String parenttype, String index, String field,
	//	String value, Integer pagenum, Integer pagesize) {
	//	SearchRequest searchRequest = new SearchRequest(index);
	//	QueryBuilder builder;
	//	if (value != null && !("".equals(value))) {
	//		builder = JoinQueryBuilders.hasParentQuery(parenttype,
	//			QueryBuilders.termQuery(field, value), false);
	//	} else {
	//		builder = JoinQueryBuilders.hasParentQuery(parenttype, QueryBuilders.matchAllQuery(),
	//			false);
	//	}
	//	SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	//	searchSourceBuilder.query(builder);
	//	searchSourceBuilder.trackTotalHits(true);
	//	int start = (pagenum - 1) * pagesize;
	//	searchSourceBuilder.from(start);
	//	searchSourceBuilder.size(pagesize);
	//	searchRequest.source(searchSourceBuilder);
	//	SearchResponse searchResponse = null;
	//	try {
	//		searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
	//	} catch (IOException e) {
	//		// TODO Auto-generated catch block
	//		e.printStackTrace();
	//	}
	//	return searchResponse;
	//}
}
