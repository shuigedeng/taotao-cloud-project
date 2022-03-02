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
package com.taotao.cloud.sys.biz.service;

import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.sys.api.dto.elasticsearch.pagemodel.DataGrid;
import com.taotao.cloud.sys.api.dto.elasticsearch.pagemodel.DataTable;
import com.taotao.cloud.sys.api.dto.elasticsearch.pagemodel.ElasticSearchRequest;
import com.taotao.cloud.sys.api.dto.elasticsearch.pagemodel.GeoDistance;
import com.taotao.cloud.sys.api.dto.elasticsearch.pagemodel.JoinParams;
import com.taotao.cloud.sys.api.dto.elasticsearch.pagemodel.MSG;
import com.taotao.cloud.sys.api.dto.elasticsearch.pagemodel.QueryCommand;
import com.taotao.cloud.sys.api.dto.elasticsearch.pagemodel.RangeQuery;
import com.taotao.cloud.sys.api.dto.elasticsearch.pagemodel.ResultData;
import com.taotao.cloud.sys.api.dto.elasticsearch.po.Sougoulog;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * IElasticsearchService
 *
 * @author shuigedeng
 * @version v1.0
 * @since 2022/03/02 17:22
 */
public interface IElasticsearchService {
	public ResultData queryString(ElasticSearchRequest request);
	public ResultData scrollQueryString(ElasticSearchRequest request);
	public DataTable<Object> geoSearch(GeoDistance geo);
	public DataGrid<Object> listCitys(int current, int rowCount, String searchPhrase);
	public DataTable<Object> hasChild(JoinParams param);
	public DataTable<Object> hasParent(JoinParams param);
	public void exportExcel(HttpServletResponse response, ElasticSearchRequest query);

	public DataGrid<Object> listSougouLog(int current, int rowCount,String searchPhrase, String startdate, String enddate);
	public ResultData sougouLogNumber() throws Exception;
	public ResultData sougouLog(@PathVariable String id) throws Exception;
	public MSG importJoinCitys() throws Exception;
	public MSG createJoinMapping() throws Exception;
	public MSG importCitys() throws Exception;

	public MSG createCityMapping() throws Exception;
	public MSG indexDocs(String indexname,String id);
	public MSG updateIndexDoc(String indexname, String id, Map<String, Object> jsonMap);
	public MSG indexDoc( String indexname, String id, Map<String, Object> jsonMap);
	public MSG importShops() throws Exception;
	public MSG createShopMapping() throws Exception;
	public MSG indexDocs() throws Exception;
	public MSG createMapping() throws Exception;
	public MSG indexJsonDoc( List<Sougoulog> log);
	public MSG indexDoc( Sougoulog log);
	public ResultData nestedTermsAggs();
	public ResultData datehistogramAggs( QueryCommand query)throws Exception;
	public ResultData rangeAggs(RangeQuery content) throws Exception;
	public ResultData termsAggs(QueryCommand query) throws Exception;
	public JSONObject search(String connName, JSONObject dsl) throws IOException;
	public JSONObject search(String connName, String indexName,  JSONObject dsl) throws IOException;
	public JSONObject status( String connName) throws IOException;
	public JSONObject nodeStats( String connName) throws IOException;
	public JSONObject clusterNodes( String connName) throws IOException;
	public JSONObject clusterState( String connName) throws IOException;
	public JSONObject clusterHealth( String connName) throws IOException;
	public ResultData histogramAggs( QueryCommand query) throws Exception;
}
