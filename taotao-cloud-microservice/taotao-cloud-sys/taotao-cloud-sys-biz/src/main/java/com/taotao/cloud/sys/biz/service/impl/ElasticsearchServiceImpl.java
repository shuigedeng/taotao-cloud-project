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
package com.taotao.cloud.sys.biz.service.impl;

import com.taotao.cloud.sys.biz.service.IElasticsearchService;
import org.springframework.stereotype.Service;

/**
 * ElasticsearchServiceImpl
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022/03/02 17:23
 */
@Service
public class ElasticsearchServiceImpl implements IElasticsearchService {

	//@Override
	//public JSONObject clusterHealth( String connName) throws IOException {
	//	String address = loadAddress(connName);
	//	return clusterApis.clusterHealth(address);
	//}
	//@Override
	//public JSONObject clusterState( String connName) throws IOException {
	//	String address = loadAddress(connName);
	//	return clusterApis.clusterState(address);
	//}
	//@Override
	//public JSONObject clusterNodes( String connName) throws IOException {
	//	String address = loadAddress(connName);
	//	return clusterApis.clusterNodes(address);
	//}
	//@Override
	//public JSONObject nodeStats( String connName) throws IOException {
	//	String address = loadAddress(connName);
	//	return clusterApis.nodeStats(address);
	//}
	//@Override
	//public JSONObject status( String connName) throws IOException {
	//	String address = loadAddress(connName);
	//	return clusterApis.status(address);
	//}
	//@Override
	//public JSONObject search(String connName, String indexName,  JSONObject dsl) throws IOException {
	//	String address = loadAddress(connName);
	//	return clusterApis.indexDataSearch(address, indexName, dsl.toJSONString());
	//}
	//@Override
	//public JSONObject search(String connName, JSONObject dsl) throws IOException {
	//	String address = loadAddress(connName);
	//	return clusterApis.dslSearch(address, dsl.toJSONString());
	//}
	//
	//@Override
	//public ResultData termsAggs(QueryCommand query) throws Exception {
	//	ResultData data = aggsService.termsAggs(query);
	//	return data;
	//}
	//@Override
	//public ResultData rangeAggs(RangeQuery content) throws Exception {
	//	ResultData data = aggsService.rangeAggs(content);
	//	return data;
	//}
	//@Override
	//public ResultData histogramAggs( QueryCommand query) throws Exception {
	//	ResultData data = aggsService.histogramAggs(query);
	//	return data;
	//}
	//@Override
	//public ResultData datehistogramAggs( QueryCommand query) throws Exception {
	//	ResultData data = aggsService.datehistogramAggs(query);
	//	return data;
	//}
	//@Override
	//public ResultData nestedTermsAggs() throws Exception {
	//	ResultData data = aggsService.nestedTermsAggs();
	//	return data;
	//}
	//@Override
	//public MSG indexDoc( Sougoulog log) {
	//	IndexRequest indexRequest = new IndexRequest("sougoulog").id(String.valueOf(log.getId()));
	//	indexRequest.source(JSON.toJSONString(log), XContentType.JSON);
	//	try {
	//		client.index(indexRequest, RequestOptions.DEFAULT);
	//	} catch (ElasticsearchException e) {
	//		if (e.status() == RestStatus.CONFLICT) {
	//			System.out.println("写入索引产生冲突" + e.getDetailedMessage());
	//		}
	//	} catch (IOException e) {
	//		e.printStackTrace();
	//	}
	//	return new MSG("index success");
	//}
	//@Override
	//public MSG indexJsonDoc( List<Sougoulog> log) {
	//	indexService.indexJsonDocs("sougoulog", log);
	//	return new MSG("index success");
	//}
	//@Override
	//public MSG createMapping() throws Exception {
	//	// 创建sougoulog索引映射
	//	boolean exsit = indexService.existIndex("sougoulog");
	//	if (exsit == false) {
	//		XContentBuilder builder = XContentFactory.jsonBuilder();
	//		builder.startObject();
	//		{
	//			builder.startObject("settings");
	//			{
	//				builder.startObject("analysis");
	//				{
	//					builder.startObject("filter");
	//					{
	//						builder.startObject("my_filter");
	//						{
	//							builder.field("type", "stop");
	//							builder.field("stopwords", "");
	//						}
	//						builder.endObject();
	//					}
	//					builder.endObject();
	//					builder.startObject("tokenizer");
	//					{
	//						builder.startObject("my_tokenizer");
	//						{
	//							builder.field("type", "standard");
	//							builder.field("max_token_length", "1");
	//						}
	//						builder.endObject();
	//					}
	//					builder.endObject();
	//					builder.startObject("analyzer");
	//					{
	//						builder.startObject("my_analyzer");
	//						{
	//							builder.field("filter", "my_filter");
	//							builder.field("char_filter", "");
	//							builder.field("type", "custom");
	//							builder.field("tokenizer", "my_tokenizer");
	//						}
	//						builder.endObject();
	//					}
	//					builder.endObject();
	//				}
	//				builder.endObject();
	//			}
	//			builder.endObject();
	//
	//			builder.startObject("mappings");
	//			{
	//				builder.startObject("properties");
	//				{
	//					builder.startObject("id");
	//					{
	//						builder.field("type", "integer");
	//					}
	//					builder.endObject();
	//					builder.startObject("clicknum");
	//					{
	//						builder.field("type", "integer");
	//					}
	//					builder.endObject();
	//					builder.startObject("keywords");
	//					{
	//						builder.field("type", "text");
	//						builder.field("analyzer", "my_analyzer");
	//						builder.startObject("fields");
	//						{
	//							builder.startObject("keyword");
	//							{
	//								builder.field("type", "keyword");
	//								builder.field("ignore_above", "256");
	//							}
	//							builder.endObject();
	//						}
	//						builder.endObject();
	//					}
	//					builder.endObject();
	//					builder.startObject("rank");
	//					{
	//						builder.field("type", "integer");
	//					}
	//					builder.endObject();
	//					builder.startObject("url");
	//					{
	//						builder.field("type", "text");
	//						builder.field("analyzer", "my_analyzer");
	//					}
	//					builder.endObject();
	//					builder.startObject("userid");
	//					{
	//						builder.field("type", "text");
	//						builder.field("analyzer", "my_analyzer");
	//						builder.startObject("fields");
	//						{
	//							builder.startObject("keyword");
	//							{
	//								builder.field("type", "keyword");
	//								builder.field("ignore_above", "256");
	//							}
	//							builder.endObject();
	//						}
	//						builder.endObject();
	//					}
	//					builder.endObject();
	//					builder.startObject("visittime");
	//					{
	//						builder.field("type", "date");
	//						builder.field("format", "HH:mm:ss");
	//					}
	//					builder.endObject();
	//				}
	//				builder.endObject();
	//			}
	//			builder.endObject();
	//		}
	//		builder.endObject();
	//		System.out.println(builder.prettyPrint());
	//		indexService.createMapping("sougoulog", builder);
	//	}
	//	return new MSG("index success");
	//}
	//@Override
	//public MSG indexDocs() throws Exception {
	//	BufferedReader br = new BufferedReader(
	//		new FileReader(ResourceUtils.getFile("classpath:SougouQ.log")));
	//	String s;
	//	int i = 1;
	//	List<Sougoulog> jsonDocs = new ArrayList<>();
	//	while ((s = br.readLine()) != null) {
	//		String[] words = s.split(" |\t");
	//		System.out.println(words[0] + " " + words[1] + words[2] + words[5]);
	//		Sougoulog log = new Sougoulog();
	//		log.setId(i);
	//		log.setVisittime(words[0]);
	//		log.setUserid(words[1]);
	//		log.setKeywords(words[2]);
	//		log.setRank(Integer.parseInt(words[3]));
	//		log.setClicknum(Integer.parseInt(words[4]));
	//		log.setUrl(words[5]);
	//		jsonDocs.add(log);
	//		i++;
	//	}
	//	int start = 0;
	//	while (start < jsonDocs.size()) {
	//		int end = 0;
	//		if (start + 1000 <= jsonDocs.size()) {
	//			end = start + 1000;
	//		} else {
	//			end = jsonDocs.size();
	//		}
	//		List<Sougoulog> sublist = jsonDocs.subList(start, end);
	//		indexService.indexJsonDocs("sougoulog", sublist);
	//		start += 1000;
	//	}
	//	br.close();
	//	return new MSG("index success");
	//}
	//@Override
	//public MSG createShopMapping() throws Exception {
	//	// 创建shop索引映射
	//	boolean exsit = indexService.existIndex("shop");
	//	if (exsit == false) {
	//		XContentBuilder builder = XContentFactory.jsonBuilder();
	//		builder.startObject();
	//		{
	//			builder.startObject("mappings");
	//			{
	//				builder.startObject("properties");
	//				{
	//					builder.startObject("key");
	//					{
	//						builder.field("type", "text");
	//					}
	//					builder.endObject();
	//					builder.startObject("name");
	//					{
	//						builder.field("type", "keyword");
	//					}
	//					builder.endObject();
	//					builder.startObject("location");
	//					{
	//						builder.field("type", "geo_point");
	//					}
	//					builder.endObject();
	//				}
	//				builder.endObject();
	//			}
	//			builder.endObject();
	//		}
	//		builder.endObject();
	//		System.out.println(builder.prettyPrint());
	//		indexService.createMapping("shop", builder);
	//	}
	//	return new MSG("index success");
	//}
	//@Override
	//public MSG importShops() throws Exception {
	//	BufferedReader br = new BufferedReader(
	//		new FileReader(ResourceUtils.getFile("classpath:shop.txt")));
	//	String s;
	//	int i = 1;
	//	List<Map<String, Object>> docs = new ArrayList<>();
	//	while ((s = br.readLine()) != null) {
	//		String[] words = s.split(" |\t");
	//		System.out.println(words[0] + " " + words[1] + words[2] + words[3]);
	//		HashMap<String, Object> doc = new HashMap<String, Object>();
	//		doc.put("key", words[0]);
	//		doc.put("name", words[1]);
	//		doc.put("location",
	//			new GeoPoint(Double.parseDouble(words[2]), Double.parseDouble(words[3])));
	//		docs.add(doc);
	//		i++;
	//	}
	//	int start = 0;
	//	while (start < docs.size()) {
	//		int end = 0;
	//		if (start + 1000 <= docs.size()) {
	//			end = start + 1000;
	//		} else {
	//			end = docs.size();
	//		}
	//		List<Map<String, Object>> sublist = docs.subList(start, end);
	//		indexService.indexDocs("shop", sublist);
	//		start += 1000;
	//	}
	//	br.close();
	//	return new MSG("index success");
	//}
	//@Override
	//public MSG indexDoc( String indexname, String id, Map<String, Object> jsonMap) {
	//	indexService.indexDoc(indexname, id, jsonMap);
	//	return new MSG("index success");
	//}
	//@Override
	//public MSG updateIndexDoc(String indexname, String id, Map<String, Object> jsonMap) {
	//	indexService.updateDoc(indexname, id, jsonMap);
	//	return new MSG("index success");
	//}
	//@Override
	//public MSG indexDocs(String indexname,String id) {
	//	int result = indexService.deleteDoc(indexname, id);
	//	if (result < 0) {
	//		return new MSG("index delete failed");
	//	} else {
	//		return new MSG("index delete success");
	//	}
	//}
	//@Override
	//public MSG createCityMapping() throws Exception {
	//	// 创建shop索引映射
	//	boolean exsit = indexService.existIndex("city");
	//	if (exsit == false) {
	//		XContentBuilder builder = XContentFactory.jsonBuilder();
	//		builder.startObject();
	//		{
	//			builder.startObject("mappings");
	//			{
	//				builder.startObject("properties");
	//				{
	//					builder.startObject("key");
	//					{
	//						builder.field("type", "text");
	//					}
	//					builder.endObject();
	//					builder.startObject("cityname");
	//					{
	//						builder.field("type", "text");
	//					}
	//					builder.endObject();
	//					builder.startObject("lastupdate");
	//					{
	//						builder.field("type", "date");
	//						builder.field("format", "yyyy-MM-dd HH:mm:ss");
	//					}
	//					builder.endObject();
	//					builder.startObject("country");
	//					{
	//						builder.field("type", "nested");
	//						builder.startObject("properties");
	//						{
	//							builder.startObject("countryid");
	//							{
	//								builder.field("type", "text");
	//							}
	//							builder.endObject();
	//							builder.startObject("countryname");
	//							{
	//								builder.field("type", "text");
	//								builder.startObject("fields");
	//								{
	//									builder.startObject("keyword");
	//									{
	//										builder.field("type", "keyword");
	//									}
	//									builder.endObject();
	//								}
	//								builder.endObject();
	//							}
	//							builder.endObject();
	//							builder.startObject("lastupdate");
	//							{
	//								builder.field("type", "date");
	//								builder.field("format", "yyyy-MM-dd HH:mm:ss");
	//							}
	//							builder.endObject();
	//						}
	//						builder.endObject();
	//					}
	//
	//					builder.endObject();
	//				}
	//				builder.endObject();
	//			}
	//			builder.endObject();
	//		}
	//		builder.endObject();
	//		indexService.createMapping("city", builder);
	//	}
	//	return new MSG("index success");
	//}
	//
	//
	//@Override
	//public MSG importCitys() throws Exception {
	//	BufferedReader countryreader = new BufferedReader(
	//		new FileReader(ResourceUtils.getFile("classpath:country.txt")));
	//	String line;
	//	int k = 1;
	//	List<Country> countrys = new ArrayList<>();
	//	while ((line = countryreader.readLine()) != null) {
	//		String[] words = line.split(";");
	//		Country country = new Country();
	//		country.setCountry_id(words[0]);
	//		country.setCountry(words[1]);
	//		country.setLast_update(words[2]);
	//		countrys.add(country);
	//		k++;
	//	}
	//	countryreader.close();
	//	BufferedReader br = new BufferedReader(
	//		new FileReader(ResourceUtils.getFile("classpath:city.txt")));
	//	String s;
	//	int i = 1;
	//	List<Map<String, Object>> docs = new ArrayList<>();
	//	while ((s = br.readLine()) != null) {
	//		String[] words = s.split(";");
	//		HashMap<String, Object> doc = new HashMap<String, Object>();
	//		doc.put("key", words[0]);
	//		doc.put("cityname", words[1]);
	//		doc.put("lastupdate", words[3]);
	//		int temp = Integer.parseInt(words[2]) - 1;
	//		String countryname = countrys.get(temp).getCountry();
	//		String date = countrys.get(temp).getLast_update();
	//		HashMap<String, Object> countrymap = new HashMap<String, Object>();
	//		countrymap.put("countryid", words[2]);
	//		countrymap.put("countryname", countryname);
	//		countrymap.put("lastupdate", date);
	//		doc.put("country", countrymap);
	//		docs.add(doc);
	//		i++;
	//	}
	//	indexService.indexDocs("city", docs);
	//	br.close();
	//	return new MSG("index success");
	//}
	//
	//@Override
	//public MSG createJoinMapping() throws Exception {
	//	// 创建shop索引映射
	//	boolean exsit = indexService.existIndex("cityjoincountry");
	//	if (exsit == false) {
	//		XContentBuilder builder = XContentFactory.jsonBuilder();
	//		builder.startObject();
	//		{
	//			builder.startObject("mappings");
	//			{
	//				builder.startObject("properties");
	//				{
	//					builder.startObject("id");
	//					{
	//						builder.field("type", "integer");
	//					}
	//					builder.endObject();
	//					builder.startObject("key");
	//					{
	//						builder.field("type", "text");
	//					}
	//					builder.endObject();
	//					builder.startObject("cityname");
	//					{
	//						builder.field("type", "keyword");
	//					}
	//					builder.endObject();
	//					builder.startObject("country");
	//					{
	//						builder.field("type", "keyword");
	//					}
	//					builder.endObject();
	//					builder.startObject("lastupdate");
	//					{
	//						builder.field("type", "date");
	//						builder.field("format", "yyyy-MM-dd HH:mm:ss");
	//					}
	//					builder.endObject();
	//					builder.startObject("joinkey");
	//					{
	//						builder.field("type", "join");
	//						builder.startObject("relations");
	//						{
	//							builder.field("country", "city");
	//						}
	//						builder.endObject();
	//					}
	//					builder.endObject();
	//				}
	//				builder.endObject();
	//			}
	//			builder.endObject();
	//		}
	//		builder.endObject();
	//		indexService.createMapping("cityjoincountry", builder);
	//	}
	//	return new MSG("index success");
	//}
	//
	//@Override
	//public MSG importJoinCitys() throws Exception {
	//	BufferedReader countryreader = new BufferedReader(
	//		new FileReader(ResourceUtils.getFile("classpath:country.txt")));
	//	String line;
	//	int k = 1;
	//	List<Map<String, Object>> docs = new ArrayList<>();
	//	while ((line = countryreader.readLine()) != null) {
	//		String[] words = line.split(";");
	//		HashMap<String, Object> doc = new HashMap<String, Object>();
	//		doc.put("key", words[0]);
	//		doc.put("id", Integer.parseInt(words[0]));
	//		doc.put("country", words[1]);
	//		doc.put("lastupdate", words[2]);
	//		HashMap<String, Object> join = new HashMap<String, Object>();
	//		join.put("name", "country");
	//		// 设置关联键
	//		doc.put("joinkey", join);
	//		docs.add(doc);
	//		k++;
	//	}
	//	countryreader.close();
	//	indexService.indexDocs("cityjoincountry", docs);
	//
	//	BufferedReader br = new BufferedReader(
	//		new FileReader(ResourceUtils.getFile("classpath:city.txt")));
	//	String s;
	//	int i = 1;
	//	List<Map<String, Object>> citys = new ArrayList<>();
	//	while ((s = br.readLine()) != null) {
	//		String[] words = s.split(";");
	//		HashMap<String, Object> city = new HashMap<String, Object>();
	//		// key必须为纯数字，否则has child和has parent查询都失败！
	//		city.put("key", "1000" + words[0]);
	//		city.put("id", Integer.parseInt(words[0]));
	//		city.put("cityname", words[1]);
	//		city.put("lastupdate", words[3]);
	//		HashMap<String, Object> joinmap = new HashMap<String, Object>();
	//		joinmap.put("name", "city");
	//		joinmap.put("parent", words[2]);
	//		city.put("joinkey", joinmap);
	//		citys.add(city);
	//		i++;
	//	}
	//	br.close();
	//	indexService.indexDocsWithRouting("cityjoincountry", citys);
	//	return new MSG("index success");
	//}
	//
	//@Override
	//public ResultData sougouLog(@PathVariable String id) throws Exception {
	//	SearchResponse rsp = searchService.termSearch("sougoulog", "id", id);
	//	SearchHit[] searchHits = rsp.getHits().getHits();
	//	List<Object> data = new ArrayList<>();
	//	for (SearchHit hit : searchHits) {
	//		Map<String, Object> map = hit.getSourceAsMap();
	//		data.add(map);
	//	}
	//	ResultData rd = new ResultData();
	//	rd.setData(data);
	//	return rd;
	//}
	//
	//@Override
	//public ResultData sougouLogNumber() throws Exception {
	//	SearchResponse rsp = searchService.matchAllSearch("sougoulog");
	//	Long total = rsp.getHits().getTotalHits().value;
	//	ResultData rd = new ResultData();
	//	rd.setData(total);
	//	return rd;
	//}
	//
	//@Override
	//public DataGrid<Object> listSougouLog(int current, int rowCount,String searchPhrase, String startdate, String enddate) {
	//	DataGrid<Object> grid = new DataGrid<Object>();
	//	List<Object> data = new ArrayList<>();
	//	ElasticSearchRequest request = new ElasticSearchRequest();
	//	QueryCommand query = new QueryCommand();
	//	query.setIndexname("sougoulog");
	//	if (StringUtils.isBlank(searchPhrase)) {
	//		query.setKeyWords("*");
	//	} else {
	//		query.setKeyWords(searchPhrase);
	//	}
	//	query.setRows(rowCount);
	//	query.setStart((current - 1) * rowCount);
	//	query.setSort("id");
	//	request.setQuery(query);
	//	if (StringUtils.isNotBlank(startdate) || StringUtils.isNotBlank(enddate)) {
	//		FilterCommand filter = new FilterCommand();
	//		filter.setField("visittime");
	//		filter.setStartdate(startdate);
	//		filter.setEnddate(enddate);
	//		request.setFilter(filter);
	//	}
	//	SearchResponse searchResponse = searchService.query_string(request);
	//	SearchHits hits = searchResponse.getHits();
	//	SearchHit[] searchHits = hits.getHits();
	//	for (SearchHit hit : searchHits) {
	//		Map<String, Object> highlights = new HashMap<String, Object>();
	//		Map<String, Object> map = hit.getSourceAsMap();
	//		// 获取高亮结果
	//		Map<String, HighlightField> highlightFields = hit.getHighlightFields();
	//		for (Map.Entry<String, HighlightField> entry : highlightFields.entrySet()) {
	//			String mapKey = entry.getKey();
	//			HighlightField mapValue = entry.getValue();
	//			Text[] fragments = mapValue.fragments();
	//			String fragmentString = fragments[0].string();
	//			highlights.put(mapKey, fragmentString);
	//		}
	//		map.put("highlight", highlights);
	//		data.add(map);
	//	}
	//	grid.setCurrent(current);
	//	grid.setRowCount(rowCount);
	//	grid.setRows(data);
	//	grid.setTotal(hits.getTotalHits().value);
	//	return grid;
	//}
	//
	//@Override
	//public ResultData queryString(ElasticSearchRequest request) {
	//	// 搜索结果
	//	List<Object> data = new ArrayList<Object>();
	//	SearchResponse searchResponse = searchService.query_string(request);
	//	SearchHits hits = searchResponse.getHits();
	//	SearchHit[] searchHits = hits.getHits();
	//	for (SearchHit hit : searchHits) {
	//		Map<String, Object> highlights = new HashMap<String, Object>();
	//		Map<String, Object> map = hit.getSourceAsMap();
	//		// 获取高亮结果
	//		Map<String, HighlightField> highlightFields = hit.getHighlightFields();
	//		for (Map.Entry<String, HighlightField> entry : highlightFields.entrySet()) {
	//			String mapKey = entry.getKey();
	//			HighlightField mapValue = entry.getValue();
	//			Text[] fragments = mapValue.fragments();
	//			String fragmentString = fragments[0].string();
	//			highlights.put(mapKey, fragmentString);
	//		}
	//		map.put("highlight", highlights);
	//		data.add(map);
	//	}
	//	ResultData resultData = new ResultData();
	//	resultData.setQtime(new Date());
	//	resultData.setData(data);
	//	resultData.setNumberFound(hits.getTotalHits().value);
	//	resultData.setStart(request.getQuery().getStart());
	//	return resultData;
	//}
	//
	//@Override
	//public ResultData scrollQueryString(ElasticSearchRequest request) {
	//	// 搜索结果
	//	List<Object> data = new ArrayList<Object>();
	//	SearchResponse searchResponse = searchService.scrollquerystring(request);
	//	SearchHits hits = searchResponse.getHits();
	//	SearchHit[] searchHits = hits.getHits();
	//	String scrollid = searchResponse.getScrollId();
	//	for (SearchHit hit : searchHits) {
	//		Map<String, Object> highlights = new HashMap<String, Object>();
	//		Map<String, Object> map = hit.getSourceAsMap();
	//		// 获取高亮结果
	//		Map<String, HighlightField> highlightFields = hit.getHighlightFields();
	//		for (Map.Entry<String, HighlightField> entry : highlightFields.entrySet()) {
	//			String mapKey = entry.getKey();
	//			HighlightField mapValue = entry.getValue();
	//			Text[] fragments = mapValue.fragments();
	//			String fragmentString = fragments[0].string();
	//			highlights.put(mapKey, fragmentString);
	//		}
	//		map.put("highlight", highlights);
	//		data.add(map);
	//	}
	//	ResultData resultData = new ResultData();
	//	resultData.setQtime(new Date());
	//	resultData.setData(data);
	//	resultData.setNumberFound(hits.getTotalHits().value);
	//	resultData.setStart(request.getQuery().getStart());
	//	resultData.setScrollid(scrollid);
	//	return resultData;
	//}
	//
	//@Override
	//public DataTable<Object> geoSearch(GeoDistance geo) {
	//	// 搜索结果
	//	List<Object> data = new ArrayList<Object>();
	//	SearchResponse searchResponse = searchService.geoDistanceSearch("shop", geo,
	//		geo.getPagenum(), geo.getPagesize());
	//	SearchHits hits = searchResponse.getHits();
	//	SearchHit[] searchHits = hits.getHits();
	//	for (SearchHit hit : searchHits) {
	//		Map<String, Object> map = hit.getSourceAsMap();
	//		data.add(map);
	//	}
	//	DataTable<Object> grid = new DataTable<Object>();
	//	grid.setDraw(UUID.randomUUID().toString());
	//	grid.setRecordsFiltered(hits.getTotalHits().value);
	//	grid.setRecordsTotal(hits.getTotalHits().value);
	//	grid.setData(data);
	//	grid.setLength(geo.getPagesize());
	//	return grid;
	//}
	//
	//@Override
	//public DataGrid<Object> listCitys(int current, int rowCount, String searchPhrase) {
	//	DataGrid<Object> grid = new DataGrid<Object>();
	//	List<Object> data = new ArrayList<>();
	//	ElasticSearchRequest request = new ElasticSearchRequest();
	//	QueryCommand query = new QueryCommand();
	//	query.setIndexname("city");
	//	if (StringUtils.isBlank(searchPhrase)) {
	//		query.setKeyWords("*");
	//	} else {
	//		query.setKeyWords(searchPhrase);
	//	}
	//	query.setRows(rowCount);
	//	query.setStart((current - 1) * rowCount);
	//	request.setQuery(query);
	//	SearchResponse searchResponse;
	//	if (StringUtils.isBlank(searchPhrase)) {
	//		searchResponse = searchService.query_string(request);
	//	} else {
	//		searchResponse = searchService.matchNestedObjectSearch("country", "city",
	//			"country.countryname", searchPhrase, current, rowCount);
	//	}
	//	SearchHits hits = searchResponse.getHits();
	//	SearchHit[] searchHits = hits.getHits();
	//	for (SearchHit hit : searchHits) {
	//		Map<String, Object> highlights = new HashMap<String, Object>();
	//		Map<String, Object> map = hit.getSourceAsMap();
	//		// 获取高亮结果
	//		Map<String, HighlightField> highlightFields = hit.getHighlightFields();
	//		for (Map.Entry<String, HighlightField> entry : highlightFields.entrySet()) {
	//			String mapKey = entry.getKey();
	//			HighlightField mapValue = entry.getValue();
	//			Text[] fragments = mapValue.fragments();
	//			String fragmentString = fragments[0].string();
	//			highlights.put(mapKey, fragmentString);
	//		}
	//		map.put("highlight", highlights);
	//		data.add(map);
	//	}
	//	grid.setCurrent(current);
	//	grid.setRowCount(rowCount);
	//	grid.setRows(data);
	//	grid.setTotal(hits.getTotalHits().value);
	//	return grid;
	//}
	//
	//@Override
	//public DataTable<Object> hasChild(JoinParams param) {
	//	// 搜索结果
	//	List<Object> data = new ArrayList<Object>();
	//	SearchResponse searchResponse = searchService.hasChildSearch("city", "cityjoincountry",
	//		"cityname", param.getName(), param.getPagenum(), param.getPagesize());
	//	SearchHits hits = searchResponse.getHits();
	//	SearchHit[] searchHits = hits.getHits();
	//	for (SearchHit hit : searchHits) {
	//		Map<String, Object> map = hit.getSourceAsMap();
	//		data.add(map);
	//	}
	//	DataTable<Object> grid = new DataTable<Object>();
	//	grid.setDraw(UUID.randomUUID().toString());
	//	grid.setRecordsFiltered(hits.getTotalHits().value);
	//	grid.setLength(param.getPagesize());
	//	grid.setRecordsTotal(hits.getTotalHits().value);
	//	grid.setData(data);
	//	return grid;
	//}
	//
	//@Override
	//public DataTable<Object> hasParent(JoinParams param) {
	//	// 搜索结果
	//	List<Object> data = new ArrayList<Object>();
	//	SearchResponse searchResponse = searchService.hasParentSearch("country", "cityjoincountry",
	//		"country", param.getName(), param.getPagenum(), param.getPagesize());
	//	SearchHits hits = searchResponse.getHits();
	//	SearchHit[] searchHits = hits.getHits();
	//	for (SearchHit hit : searchHits) {
	//		Map<String, Object> map = hit.getSourceAsMap();
	//		data.add(map);
	//	}
	//	DataTable<Object> grid = new DataTable<Object>();
	//	grid.setDraw(UUID.randomUUID().toString());
	//	grid.setRecordsFiltered(hits.getTotalHits().value);
	//	grid.setRecordsTotal(hits.getTotalHits().value);
	//	grid.setLength(param.getPagesize());
	//	grid.setData(data);
	//	return grid;
	//}
	//
	//@Override
	//public void exportExcel(HttpServletResponse response, ElasticSearchRequest query) {
	//	try {
	//		HSSFWorkbook workbook = new HSSFWorkbook();                        // 创建工作簿对象
	//		HSSFSheet sheet = workbook.createSheet("sheet1");
	//		// 搜索结果
	//		SearchResponse searchResponse = searchService.query_string(query);
	//		SearchHits hits = searchResponse.getHits();
	//		SearchHit[] searchHits = hits.getHits();
	//		if (searchHits.length > 0) {
	//			// 写列头
	//			SearchHit first = searchHits[0];
	//			HSSFRow frow = sheet.createRow(0);
	//			Map<String, Object> fmap = first.getSourceAsMap();
	//			int fcol = 0;
	//			for (String key : fmap.keySet()) {
	//				if (key.contains("@")) {
	//					continue;
	//				} else {
	//					HSSFCell cell = null;   //设置单元格的数据类型
	//					cell = frow.createCell(fcol, HSSFCell.CELL_TYPE_STRING);
	//					cell.setCellValue(key);
	//				}
	//				fcol++;
	//			}
	//
	//			for (int i = 0; i < searchHits.length; i++) {
	//				SearchHit hit = searchHits[i];
	//				HSSFRow row = sheet.createRow(i + 1);
	//
	//				Map<String, Object> map = hit.getSourceAsMap();
	//
	//				int col = 0;
	//				for (String key : map.keySet()) {
	//					if (key.contains("@")) {
	//						continue;
	//					}
	//					if (map.get(key) == null && !("id".equals(key))) {
	//						HSSFCell cell = null;   //设置单元格的数据类型
	//						cell = row.createCell(col, HSSFCell.CELL_TYPE_STRING);
	//						cell.setCellValue("");
	//					} else {
	//						HSSFCell cell = null;   //设置单元格的数据类型
	//						cell = row.createCell(col, HSSFCell.CELL_TYPE_STRING);
	//						String cellvalue = map.get(key).toString();
	//						cell.setCellValue(cellvalue);
	//					}
	//					col++;
	//				}
	//			}
	//		} else {
	//			HSSFRow frow = sheet.createRow(0);
	//		}
	//		ByteArrayOutputStream os = new ByteArrayOutputStream();
	//		try {
	//			workbook.write(os);
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//
	//		byte[] content = os.toByteArray();
	//		InputStream is = new ByteArrayInputStream(content);
	//		response.setContentType("application/vnd.ms-excel");
	//		response.setHeader("Content-Disposition", "attachment;filename=AllUsers.xls");
	//		ServletOutputStream output = response.getOutputStream();
	//		IOUtils.copy(is, output);
	//	} catch (HttpMessageNotReadableException hex) {
	//		hex.printStackTrace();
	//	} catch (Exception e) {
	//		e.printStackTrace();
	//	}
	//}
}
