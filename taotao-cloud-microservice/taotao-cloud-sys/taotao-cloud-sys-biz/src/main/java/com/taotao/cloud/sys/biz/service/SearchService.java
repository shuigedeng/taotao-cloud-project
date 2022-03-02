package com.taotao.cloud.sys.biz.service;

import com.taotao.cloud.sys.api.dto.elasticsearch.pagemodel.ElasticSearchRequest;
import com.taotao.cloud.sys.api.dto.elasticsearch.pagemodel.GeoDistance;
import org.elasticsearch.action.search.SearchResponse;


public interface SearchService {
	/**
	 * 多字段搜索-普通分页
	 * @param request
	 * @return
	 */
	SearchResponse query_string(ElasticSearchRequest request);
	/**
	 * 多字段搜索-滚动分页
	 * @param request
	 * @return
	 */
	SearchResponse scrollquerystring(ElasticSearchRequest request);
	
	/**
	 * 精准搜索
	 */
	SearchResponse termSearch(String index, String field,String term);
	
	/**
	 * 搜索全部
	 */
	SearchResponse matchAllSearch(String index);
	
	/**
	 * 经纬度搜索
	 */
	SearchResponse geoDistanceSearch(String index, GeoDistance geo, Integer pagenum, Integer pagesize);
	
	/**
	 * 搜索嵌套对象
	 */
	SearchResponse matchNestedObjectSearch(String path, String index, String field, String value, Integer pagenum, Integer pagesize);

	/**
	 * join查询：以子查父
	 */
	SearchResponse hasChildSearch(String childtype, String index, String field, String value, Integer pagenum, Integer pagesize);
	
	/**
	 * join查询：以父查子
	 */
	SearchResponse hasParentSearch(String parenttype, String index, String field, String value, Integer pagenum, Integer pagesize);

}
