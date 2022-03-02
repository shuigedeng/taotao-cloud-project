package com.taotao.cloud.sys.biz.controller.tools;

import com.alibaba.fastjson.JSON;
import com.taotao.cloud.sys.biz.service.AggsService;
import com.taotao.cloud.sys.biz.service.IndexService;
import com.taotao.cloud.sys.biz.service.SearchService;
import com.taotao.cloud.sys.api.dto.elasticsearch.pagemodel.DataGrid;
import com.taotao.cloud.sys.api.dto.elasticsearch.pagemodel.DataTable;
import com.taotao.cloud.sys.api.dto.elasticsearch.pagemodel.ElasticSearchRequest;
import com.taotao.cloud.sys.api.dto.elasticsearch.pagemodel.FilterCommand;
import com.taotao.cloud.sys.api.dto.elasticsearch.pagemodel.GeoDistance;
import com.taotao.cloud.sys.api.dto.elasticsearch.pagemodel.JoinParams;
import com.taotao.cloud.sys.api.dto.elasticsearch.pagemodel.MSG;
import com.taotao.cloud.sys.api.dto.elasticsearch.pagemodel.QueryCommand;
import com.taotao.cloud.sys.api.dto.elasticsearch.pagemodel.RangeQuery;
import com.taotao.cloud.sys.api.dto.elasticsearch.pagemodel.ResultData;
import com.taotao.cloud.sys.api.dto.elasticsearch.po.Country;
import com.taotao.cloud.sys.api.dto.elasticsearch.po.Sougoulog;
import com.taotao.cloud.sys.biz.forest.ClusterApis;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.util.IOUtils;
import org.apache.pulsar.shade.io.swagger.annotations.ApiOperation;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;

/**
 * ElasticsearchController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-02 16:31:03
 */
@Validated
@RestController
@Tag(name = "工具管理端-elasticsearch管理API", description = "工具管理端-elasticsearch管理API")
@RequestMapping("/sys/tools/elasticsearch")
public class ElasticsearchController {

    @Autowired
    private ConnectService connectService;

    @Autowired
    private ClusterApis clusterApis;

//    /**
//     * 数据量太大了, 使用分开的 api
//     * @param connName
//     * @return
//     * @throws IOException
//     */
//    @Deprecated
//    @GetMapping("/cluster/state")
//    public ClusterModel clusterModel(@NotNull String connName) throws IOException {
//        String address = loadAddress(connName);
//
//        JSONObject clusterHealth = clusterApis.clusterHealth(address);
//        JSONObject clusterNodes = clusterApis.clusterNodes(address);
//        JSONObject clusterState = clusterApis.clusterState(address);
//        JSONObject status = clusterApis.status(address);
//        JSONObject nodeStats = clusterApis.nodeStats(address);
//
//        ClusterModel clusterModel = new ClusterModel(clusterState, status, nodeStats, clusterNodes, clusterHealth);
//        return clusterModel;
//    }

    @GetMapping("/cluster/health")
    public JSONObject clusterHealth(@NotNull String connName) throws IOException {
        String address = loadAddress(connName);
        return clusterApis.clusterHealth(address);
    }

    @GetMapping("/cluster/state")
    public JSONObject clusterState(@NotNull String connName) throws IOException {
        String address = loadAddress(connName);
        return clusterApis.clusterState(address);
    }

    @GetMapping("/cluster/nodes")
    public JSONObject clusterNodes(@NotNull String connName) throws IOException {
        String address = loadAddress(connName);
        return clusterApis.clusterNodes(address);
    }

    @GetMapping("/node/stats")
    public JSONObject nodeStats(@NotNull String connName) throws IOException {
        String address = loadAddress(connName);
        return clusterApis.nodeStats(address);
    }

    @GetMapping("/status")
    public JSONObject status(@NotNull String connName) throws IOException {
        String address = loadAddress(connName);
        return clusterApis.status(address);
    }

    @PostMapping("/search/{connName}/{indexName}")
    public JSONObject search(@PathVariable("connName") String connName,@PathVariable("indexName") String indexName, @RequestBody JSONObject dsl) throws IOException {
        String address = loadAddress(connName);
        return clusterApis.indexDataSearch(address,indexName,dsl.toJSONString());
    }

    @PostMapping("/search/{connName}")
    public JSONObject search(@PathVariable("connName") String connName, @RequestBody JSONObject dsl) throws IOException {
        String address = loadAddress(connName);
        return clusterApis.dslSearch(address,dsl.toJSONString());
    }

    /**
     * 获取当前连接的 es 地址
     * @param connName
     * @return
     * @throws IOException
     */
    private String loadAddress(String connName) throws IOException {
        SimpleConnectParam simpleConnectParam = (SimpleConnectParam) connectService.readConnParams("elasticsearch",connName);
        return simpleConnectParam.getConnectParam().httpConnectString();
    }


	@Autowired
	AggsService aggsService;

	/**
	 * terms聚集接口
	 * @param content
	 * @return
	 * @throws Exception
	 */
	@ApiOperation("词条聚集")
	@RequestMapping(value = "/aggs/termsAggs", method = RequestMethod.POST)
	@ResponseBody
	public ResultData termsAggs(@RequestBody QueryCommand query) throws Exception{
		ResultData data = aggsService.termsAggs(query);
		return data;
	}

	/**
	 * 范围聚集接口
	 * @param content
	 * @return
	 * @throws Exception
	 */
	@ApiOperation("范围聚集")
	@RequestMapping(value = "/aggs/rangeAggs", method = RequestMethod.POST)
	@ResponseBody
	public ResultData rangeAggs(@RequestBody RangeQuery content) throws Exception{
		ResultData data = aggsService.rangeAggs(content);
		return data;
	}

	/**
	 * histogram聚集接口
	 * @param content
	 * @return
	 * @throws Exception
	 */
	@ApiOperation("直方图聚集")
	@RequestMapping(value = "/aggs/histogramAggs", method = RequestMethod.POST)
	@ResponseBody
	public ResultData histogramAggs(@RequestBody QueryCommand query) throws Exception{
		ResultData data = aggsService.histogramAggs(query);
		return data;
	}

	/**
	 * datehistogram聚集接口
	 * @param content
	 * @return
	 * @throws Exception
	 */
	@ApiOperation("日期直方图聚集")
	@RequestMapping(value = "/aggs/datehistogramAggs", method = RequestMethod.POST)
	@ResponseBody
	public ResultData datehistogramAggs(@RequestBody QueryCommand query) throws Exception{
		ResultData data = aggsService.datehistogramAggs(query);
		return data;
	}

	@ApiOperation("按国家嵌套对象词条聚集")
	@RequestMapping(value = "/aggs/nestedTermsAggs", method = RequestMethod.GET)
	@ResponseBody
	public ResultData nestedTermsAggs() throws Exception{
		ResultData data = aggsService.nestedTermsAggs();
		return data;
	}

	@RequestMapping(value = "/aggs/analysis", method = RequestMethod.GET)
	public String analysis() {
		return "analysis";
	}


	@Autowired
	RestHighLevelClient client;

	@Autowired
	IndexService indexService;

	@ApiOperation("索引一个日志文档")
	@RequestMapping(value="/index/indexSougoulog", method = RequestMethod.POST)
	@ResponseBody
	MSG indexDoc(@RequestBody Sougoulog log){
		IndexRequest indexRequest = new IndexRequest("sougoulog").id(String.valueOf(log.getId()));
		indexRequest.source(JSON.toJSONString(log), XContentType.JSON);
		try {
			client.index(indexRequest, RequestOptions.DEFAULT);
		} catch(ElasticsearchException e ) {
			if (e.status() == RestStatus.CONFLICT) {
				System.out.println("写入索引产生冲突"+e.getDetailedMessage());
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return new MSG("index success");
	}

	@ApiOperation("索引一组json日志文档")
	@RequestMapping(value="/index/indexJsonSougoulog", method = RequestMethod.POST)
	@ResponseBody
	MSG indexJsonDoc(@RequestBody List<Sougoulog> log){
		indexService.indexJsonDocs("sougoulog", log);
		return new MSG("index success");
	}

	/**
	 * 创建索引并设置字段类型
	 * @param indexname
	 * @param indextype
	 * @param jsonMap
	 * @return
	 * @throws Exception
	 * rest api:
	PUT sougoulog
	{
	"settings": {
	"analysis": {
	"filter": {
	"my_filter": {
	"type": "stop",
	"stopwords": ""
	}
	},
	"tokenizer": {
	"my_tokenizer": {
	"type": "standard",
	"max_token_length": "1"
	}
	},
	"analyzer": {
	"my_analyzer": {
	"type": "custom",
	"char_filter": "",
	"tokenizer": "my_tokenizer",
	"filter": "my_filter"
	}
	}
	}
	},
	"mappings": {
	"_doc": {
	"properties": {
	"id": {
	"type": "integer"
	},
	"clicknum": {
	"type": "integer"
	},
	"keywords": {
	"type": "text",
	"analyzer": "my_analyzer",
	"fields": {
	"keyword": {
	"type": "keyword",
	"ignore_above": 256
	}
	}
	},
	"rank": {
	"type": "integer"
	},
	"url": {
	"type": "text",
	"analyzer": "my_analyzer"
	},
	"userid": {
	"type": "text",
	"analyzer": "my_analyzer",
	"fields": {
	"keyword": {
	"type": "keyword",
	"ignore_above": 256
	}
	}
	},
	"visittime": {
	"type": "date",
	"format": "HH:mm:ss"
	}

	}
	}
	}
	}
	 */
	@ApiOperation("创建各索引并设置字段类型:sougoulog")
	@RequestMapping(value="/index/createIndexMapping",method = RequestMethod.GET)
	@ResponseBody
	MSG createMapping() throws Exception{
		// 创建sougoulog索引映射
		boolean exsit = indexService.existIndex("sougoulog");
		if ( exsit == false ) {
			XContentBuilder builder = XContentFactory.jsonBuilder();
			builder.startObject();
			{
				builder.startObject("settings");
				{
					builder.startObject("analysis");
					{
						builder.startObject("filter");
						{
							builder.startObject("my_filter");
							{
								builder.field("type", "stop");
								builder.field("stopwords", "");
							}
							builder.endObject();
						}
						builder.endObject();
						builder.startObject("tokenizer");
						{
							builder.startObject("my_tokenizer");
							{
								builder.field("type", "standard");
								builder.field("max_token_length", "1");
							}
							builder.endObject();
						}
						builder.endObject();
						builder.startObject("analyzer");
						{
							builder.startObject("my_analyzer");
							{
								builder.field("filter", "my_filter");
								builder.field("char_filter", "");
								builder.field("type", "custom");
								builder.field("tokenizer", "my_tokenizer");
							}
							builder.endObject();
						}
						builder.endObject();
					}
					builder.endObject();
				}
				builder.endObject();

				builder.startObject("mappings");
				{
					builder.startObject("properties");
					{
						builder.startObject("id");
						{
							builder.field("type", "integer");
						}
						builder.endObject();
						builder.startObject("clicknum");
						{
							builder.field("type", "integer");
						}
						builder.endObject();
						builder.startObject("keywords");
						{
							builder.field("type", "text");
							builder.field("analyzer", "my_analyzer");
							builder.startObject("fields");
							{
								builder.startObject("keyword");{
								builder.field("type", "keyword");
								builder.field("ignore_above", "256");
							}
								builder.endObject();
							}
							builder.endObject();
						}
						builder.endObject();
						builder.startObject("rank");
						{
							builder.field("type", "integer");
						}
						builder.endObject();
						builder.startObject("url");
						{
							builder.field("type", "text");
							builder.field("analyzer", "my_analyzer");
						}
						builder.endObject();
						builder.startObject("userid");
						{
							builder.field("type", "text");
							builder.field("analyzer", "my_analyzer");
							builder.startObject("fields");
							{
								builder.startObject("keyword");{
								builder.field("type", "keyword");
								builder.field("ignore_above", "256");
							}
								builder.endObject();
							}
							builder.endObject();
						}
						builder.endObject();
						builder.startObject("visittime");
						{
							builder.field("type", "date");
							builder.field("format", "HH:mm:ss");
						}
						builder.endObject();
					}
					builder.endObject();
				}
				builder.endObject();
			}
			builder.endObject();
			System.out.println(builder.prettyPrint());
			indexService.createMapping("sougoulog",builder);
		}
		return new MSG("index success");
	}

	@ApiOperation("向索引sougoulog导入数据")
	@RequestMapping(value="/index/indexDocs",method = RequestMethod.GET)
	@ResponseBody
	MSG indexDocs() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(ResourceUtils.getFile("classpath:SougouQ.log")));
		String s;
		int i = 1;
		List<Sougoulog> jsonDocs = new ArrayList<>();
		while ((s = br.readLine()) != null) {
			String[] words = s.split(" |\t");
			System.out.println(words[0]+" "+words[1]+words[2]+words[5]);
			Sougoulog log = new Sougoulog();
			log.setId(i);
			log.setVisittime(words[0]);
			log.setUserid(words[1]);
			log.setKeywords(words[2]);
			log.setRank(Integer.parseInt(words[3]));
			log.setClicknum(Integer.parseInt(words[4]));
			log.setUrl(words[5]);
			jsonDocs.add(log);
			i++;
		}
		int start = 0;
		while (start < jsonDocs.size()) {
			int end = 0;
			if (start + 1000 <= jsonDocs.size()) {
				end = start + 1000;
			} else {
				end = jsonDocs.size();
			}
			List<Sougoulog> sublist = jsonDocs.subList(start, end);
			indexService.indexJsonDocs("sougoulog", sublist);
			start += 1000;
		}
		br.close();
		return new MSG("index success");
	}

	@ApiOperation("创建shop索引")
	@RequestMapping(value="/index/createShopMapping",method = RequestMethod.GET)
	@ResponseBody
	MSG createShopMapping() throws Exception{
		// 创建shop索引映射
		boolean exsit = indexService.existIndex("shop");
		if ( exsit == false ) {
			XContentBuilder builder = XContentFactory.jsonBuilder();
			builder.startObject();
			{
				builder.startObject("mappings");
				{
					builder.startObject("properties");
					{
						builder.startObject("key");
						{
							builder.field("type", "text");
						}
						builder.endObject();
						builder.startObject("name");
						{
							builder.field("type", "keyword");
						}
						builder.endObject();
						builder.startObject("location");
						{
							builder.field("type", "geo_point");
						}
						builder.endObject();
					}
					builder.endObject();
				}
				builder.endObject();
			}
			builder.endObject();
			System.out.println(builder.prettyPrint());
			indexService.createMapping("shop",builder);
		}
		return new MSG("index success");
	}

	@ApiOperation("导入shop经纬度数据")
	@RequestMapping(value="/index/importShops",method = RequestMethod.GET)
	@ResponseBody
	MSG importShops() throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(ResourceUtils.getFile("classpath:shop.txt")));
		String s;
		int i = 1;
		List<Map<String, Object>> docs = new ArrayList<>();
		while ((s = br.readLine()) != null) {
			String[] words = s.split(" |\t");
			System.out.println(words[0]+" "+words[1]+words[2]+words[3]);
			HashMap<String, Object> doc = new HashMap<String, Object>();
			doc.put("key", words[0]);
			doc.put("name", words[1]);
			doc.put("location", new GeoPoint(Double.parseDouble(words[2]), Double.parseDouble(words[3])));
			docs.add(doc);
			i++;
		}
		int start = 0;
		while (start < docs.size()) {
			int end = 0;
			if (start + 1000 <= docs.size()) {
				end = start + 1000;
			} else {
				end = docs.size();
			}
			List<Map<String, Object>> sublist = docs.subList(start, end);
			indexService.indexDocs("shop", sublist);
			start += 1000;
		}
		br.close();
		return new MSG("index success");
	}

	@ApiOperation("向索引添加一个文档")
	@RequestMapping(value="/index/indexDoc/{indexname}/{id}",method = RequestMethod.POST)
	@ResponseBody
	MSG indexDoc(@PathVariable String indexname, @PathVariable String id, @RequestBody Map<String, Object> jsonMap){
		indexService.indexDoc(indexname, id, jsonMap);
		return new MSG("index success");
	}

	@ApiOperation("向索引修改一个文档")
	@RequestMapping(value="/index/indexDoc/{indexname}/{id}",method = RequestMethod.PUT)
	@ResponseBody
	MSG updateIndexDoc(@PathVariable String indexname, @PathVariable String id, @RequestBody Map<String, Object> jsonMap){
		indexService.updateDoc(indexname, id, jsonMap);
		return new MSG("index success");
	}

	@ApiOperation("删除一个索引中的文档")
	@RequestMapping(value="/index/indexDocs/{indexname}/{id}",method = RequestMethod.DELETE)
	@ResponseBody
	MSG indexDocs(@PathVariable String indexname, @PathVariable String id){
		int result = indexService.deleteDoc(indexname, id);
		if ( result < 0 ) {
			return new MSG("index delete failed");
		} else {
			return new MSG("index delete success");
		}
	}

	@ApiOperation("创建城市索引")
	@RequestMapping(value="/index/createCityMapping",method = RequestMethod.GET)
	@ResponseBody
	MSG createCityMapping() throws Exception{
		// 创建shop索引映射
		boolean exsit = indexService.existIndex("city");
		if ( exsit == false ) {
			XContentBuilder builder = XContentFactory.jsonBuilder();
			builder.startObject();
			{
				builder.startObject("mappings");
				{
					builder.startObject("properties");
					{
						builder.startObject("key");
						{
							builder.field("type", "text");
						}
						builder.endObject();
						builder.startObject("cityname");
						{
							builder.field("type", "text");
						}
						builder.endObject();
						builder.startObject("lastupdate");
						{
							builder.field("type", "date");
							builder.field("format", "yyyy-MM-dd HH:mm:ss");
						}
						builder.endObject();
						builder.startObject("country");
						{
							builder.field("type", "nested");
							builder.startObject("properties");
							{
								builder.startObject("countryid");
								{
									builder.field("type", "text");
								}
								builder.endObject();
								builder.startObject("countryname");
								{
									builder.field("type", "text");
									builder.startObject("fields");
									{
										builder.startObject("keyword");
										{
											builder.field("type", "keyword");
										}
										builder.endObject();
									}
									builder.endObject();
								}
								builder.endObject();
								builder.startObject("lastupdate");
								{
									builder.field("type", "date");
									builder.field("format", "yyyy-MM-dd HH:mm:ss");
								}
								builder.endObject();
							}
							builder.endObject();
						}

						builder.endObject();
					}
					builder.endObject();
				}
				builder.endObject();
			}
			builder.endObject();
			indexService.createMapping("city",builder);
		}
		return new MSG("index success");
	}

	@ApiOperation("导入城市和国家数据-嵌套对象")
	@RequestMapping(value="/index/importCitys",method = RequestMethod.GET)
	@ResponseBody
	MSG importCitys() throws Exception{
		BufferedReader countryreader = new BufferedReader(new FileReader(ResourceUtils.getFile("classpath:country.txt")));
		String line;
		int k = 1;
		List<Country> countrys = new ArrayList<>();
		while ((line = countryreader.readLine()) != null) {
			String[] words = line.split(";");
			Country country = new Country();
			country.setCountry_id(words[0]);
			country.setCountry(words[1]);
			country.setLast_update(words[2]);
			countrys.add(country);
			k++;
		}
		countryreader.close();
		BufferedReader br = new BufferedReader(new FileReader(ResourceUtils.getFile("classpath:city.txt")));
		String s;
		int i = 1;
		List<Map<String, Object>> docs = new ArrayList<>();
		while ((s = br.readLine()) != null) {
			String[] words = s.split(";");
			HashMap<String, Object> doc = new HashMap<String, Object>();
			doc.put("key", words[0]);
			doc.put("cityname", words[1]);
			doc.put("lastupdate", words[3]);
			int temp = Integer.parseInt(words[2])-1;
			String countryname = countrys.get(temp).getCountry();
			String date = countrys.get(temp).getLast_update();
			HashMap<String, Object> countrymap = new HashMap<String, Object>();
			countrymap.put("countryid", words[2]);
			countrymap.put("countryname", countryname);
			countrymap.put("lastupdate", date);
			doc.put("country", countrymap);
			docs.add(doc);
			i++;
		}
		indexService.indexDocs("city", docs);
		br.close();
		return new MSG("index success");
	}

	/**
	 * 使用join字段构建索引一对多关联
	 */
	@ApiOperation("创建一对多关联索引")
	@RequestMapping(value="/index/createJoinMapping",method = RequestMethod.GET)
	@ResponseBody
	MSG createJoinMapping() throws Exception {
		// 创建shop索引映射
		boolean exsit = indexService.existIndex("cityjoincountry");
		if ( exsit == false ) {
			XContentBuilder builder = XContentFactory.jsonBuilder();
			builder.startObject();
			{
				builder.startObject("mappings");
				{
					builder.startObject("properties");
					{
						builder.startObject("id");
						{
							builder.field("type", "integer");
						}
						builder.endObject();
						builder.startObject("key");
						{
							builder.field("type", "text");
						}
						builder.endObject();
						builder.startObject("cityname");
						{
							builder.field("type", "keyword");
						}
						builder.endObject();
						builder.startObject("country");
						{
							builder.field("type", "keyword");
						}
						builder.endObject();
						builder.startObject("lastupdate");
						{
							builder.field("type", "date");
							builder.field("format", "yyyy-MM-dd HH:mm:ss");
						}
						builder.endObject();
						builder.startObject("joinkey");
						{
							builder.field("type", "join");
							builder.startObject("relations");
							{
								builder.field("country", "city");
							}
							builder.endObject();
						}
						builder.endObject();
					}
					builder.endObject();
				}
				builder.endObject();
			}
			builder.endObject();
			indexService.createMapping("cityjoincountry",builder);
		}
		return new MSG("index success");
	}

	@ApiOperation("导入城市和国家数据-join")
	@RequestMapping(value="/index/importJoinCitys",method = RequestMethod.GET)
	@ResponseBody
	MSG importJoinCitys() throws Exception{
		BufferedReader countryreader = new BufferedReader(new FileReader(ResourceUtils.getFile("classpath:country.txt")));
		String line;
		int k = 1;
		List<Map<String, Object>> docs = new ArrayList<>();
		while ((line = countryreader.readLine()) != null) {
			String[] words = line.split(";");
			HashMap<String, Object> doc = new HashMap<String, Object>();
			doc.put("key", words[0]);
			doc.put("id", Integer.parseInt(words[0]));
			doc.put("country", words[1]);
			doc.put("lastupdate", words[2]);
			HashMap<String, Object> join = new HashMap<String, Object>();
			join.put("name", "country");
			// 设置关联键
			doc.put("joinkey", join);
			docs.add(doc);
			k++;
		}
		countryreader.close();
		indexService.indexDocs("cityjoincountry", docs);

		BufferedReader br = new BufferedReader(new FileReader(ResourceUtils.getFile("classpath:city.txt")));
		String s;
		int i = 1;
		List<Map<String, Object>> citys = new ArrayList<>();
		while ((s = br.readLine()) != null) {
			String[] words = s.split(";");
			HashMap<String, Object> city = new HashMap<String, Object>();
			// key必须为纯数字，否则has child和has parent查询都失败！
			city.put("key", "1000" + words[0]);
			city.put("id", Integer.parseInt(words[0]));
			city.put("cityname", words[1]);
			city.put("lastupdate", words[3]);
			HashMap<String, Object> joinmap = new HashMap<String, Object>();
			joinmap.put("name", "city");
			joinmap.put("parent",words[2]);
			city.put("joinkey", joinmap);
			citys.add(city);
			i++;
		}
		br.close();
		indexService.indexDocsWithRouting("cityjoincountry", citys);
		return new MSG("index success");
	}



	@Autowired
	SearchService searchService;

	@ApiOperation("获取一个日志数据")
	@RequestMapping(value = "/search/sougoulog/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResultData sougoulog(@PathVariable String id) throws Exception{
		SearchResponse rsp = searchService.termSearch("sougoulog", "id", id);
		SearchHit[] searchHits = rsp.getHits().getHits();
		List<Object> data = new ArrayList<>();
		for (SearchHit hit : searchHits) {
			Map<String, Object> map = hit.getSourceAsMap();
			data.add(map);
		}
		ResultData rd = new ResultData();
		rd.setData(data);
		return rd;
	}

	@ApiOperation("获取日志数据的总数")
	@RequestMapping(value = "/sougoulognumber", method = RequestMethod.GET)
	@ResponseBody
	public ResultData sougoulognumber() throws Exception {
		SearchResponse rsp = searchService.matchAllSearch("sougoulog");
		Long total = rsp.getHits().getTotalHits().value;
		ResultData rd = new ResultData();
		rd.setData(total);
		return rd;
	}

	@ApiOperation("分页查询搜狗日志")
	@RequestMapping(value = "/search/sougoulog", method = RequestMethod.POST)
	@ResponseBody
	public DataGrid<Object> listsougoulog(@RequestParam(value="current") int current, @RequestParam(value="rowCount") int rowCount
		,@RequestParam(value="searchPhrase") String searchPhrase,@RequestParam(value="startdate",required=false) String startdate
		,@RequestParam(value="enddate",required=false) String enddate) {
		DataGrid<Object> grid = new DataGrid<Object>();
		List<Object> data = new ArrayList<>();
		ElasticSearchRequest request = new ElasticSearchRequest();
		QueryCommand query = new QueryCommand();
		query.setIndexname("sougoulog");
		if (StringUtils.isBlank(searchPhrase)) {
			query.setKeyWords("*");
		} else {
			query.setKeyWords(searchPhrase);
		}
		query.setRows(rowCount);
		query.setStart((current-1)*rowCount);
		query.setSort("id");
		request.setQuery(query);
		if (StringUtils.isNotBlank(startdate) || StringUtils.isNotBlank(enddate)) {
			FilterCommand filter = new FilterCommand();
			filter.setField("visittime");
			filter.setStartdate(startdate);
			filter.setEnddate(enddate);
			request.setFilter(filter);
		}
		SearchResponse searchResponse = searchService.query_string(request);
		SearchHits hits = searchResponse.getHits();
		SearchHit[] searchHits = hits.getHits();
		for (SearchHit hit : searchHits) {
			Map<String, Object> highlights = new HashMap<String, Object>();
			Map<String, Object> map = hit.getSourceAsMap();
			// 获取高亮结果
			Map<String, HighlightField> highlightFields = hit.getHighlightFields();
			for (Map.Entry<String, HighlightField> entry : highlightFields.entrySet()) {
				String mapKey = entry.getKey();
				HighlightField mapValue = entry.getValue();
				Text[] fragments = mapValue.fragments();
				String fragmentString = fragments[0].string();
				highlights.put(mapKey, fragmentString);
			}
			map.put("highlight", highlights);
			data.add(map);
		}
		grid.setCurrent(current);
		grid.setRowCount(rowCount);
		grid.setRows(data);
		grid.setTotal(hits.getTotalHits().value);
		return grid;
	}


	@ApiOperation("query_string全字段查找-普通分页版")
	@RequestMapping(value = "/search/query_string", method = RequestMethod.POST)
	@ResponseBody
	public ResultData query_string(@RequestBody ElasticSearchRequest request) {
		// 搜索结果
		List<Object> data = new ArrayList<Object>();
		SearchResponse searchResponse = searchService.query_string(request);
		SearchHits hits = searchResponse.getHits();
		SearchHit[] searchHits = hits.getHits();
		for (SearchHit hit : searchHits) {
			Map<String, Object> highlights = new HashMap<String, Object>();
			Map<String, Object> map = hit.getSourceAsMap();
			// 获取高亮结果
			Map<String, HighlightField> highlightFields = hit.getHighlightFields();
			for (Map.Entry<String, HighlightField> entry : highlightFields.entrySet()) {
				String mapKey = entry.getKey();
				HighlightField mapValue = entry.getValue();
				Text[] fragments = mapValue.fragments();
				String fragmentString = fragments[0].string();
				highlights.put(mapKey, fragmentString);
			}
			map.put("highlight", highlights);
			data.add(map);
		}
		ResultData resultData = new ResultData();
		resultData.setQtime(new Date());
		resultData.setData(data);
		resultData.setNumberFound(hits.getTotalHits().value);
		resultData.setStart(request.getQuery().getStart());
		return resultData;
	}

	@ApiOperation("query_string全字段查找-滚动分页版")
	@RequestMapping(value = "/search/query_string/scroll", method = RequestMethod.POST)
	@ResponseBody
	public ResultData scrollquery_string(@RequestBody ElasticSearchRequest request) {
		// 搜索结果
		List<Object> data = new ArrayList<Object>();
		SearchResponse searchResponse = searchService.scrollquerystring(request);
		SearchHits hits = searchResponse.getHits();
		SearchHit[] searchHits = hits.getHits();
		String scrollid = searchResponse.getScrollId();
		for (SearchHit hit : searchHits) {
			Map<String, Object> highlights = new HashMap<String, Object>();
			Map<String, Object> map = hit.getSourceAsMap();
			// 获取高亮结果
			Map<String, HighlightField> highlightFields = hit.getHighlightFields();
			for (Map.Entry<String, HighlightField> entry : highlightFields.entrySet()) {
				String mapKey = entry.getKey();
				HighlightField mapValue = entry.getValue();
				Text[] fragments = mapValue.fragments();
				String fragmentString = fragments[0].string();
				highlights.put(mapKey, fragmentString);
			}
			map.put("highlight", highlights);
			data.add(map);
		}
		ResultData resultData = new ResultData();
		resultData.setQtime(new Date());
		resultData.setData(data);
		resultData.setNumberFound(hits.getTotalHits().value);
		resultData.setStart(request.getQuery().getStart());
		resultData.setScrollid(scrollid);
		return resultData;
	}

	@ApiOperation("经纬度搜索")
	@RequestMapping(value = "/search/geosearch", method = RequestMethod.POST)
	@ResponseBody
	public DataTable<Object> geosearch(@RequestBody GeoDistance geo) {
		// 搜索结果
		List<Object> data = new ArrayList<Object>();
		SearchResponse searchResponse = searchService.geoDistanceSearch("shop", geo, geo.getPagenum(), geo.getPagesize());
		SearchHits hits = searchResponse.getHits();
		SearchHit[] searchHits = hits.getHits();
		for (SearchHit hit : searchHits) {
			Map<String, Object> map = hit.getSourceAsMap();
			data.add(map);
		}
		DataTable<Object> grid = new DataTable<Object>();
		grid.setDraw(UUID.randomUUID().toString());
		grid.setRecordsFiltered(hits.getTotalHits().value);
		grid.setRecordsTotal(hits.getTotalHits().value);
		grid.setData(data);
		grid.setLength(geo.getPagesize());
		return grid;
	}

	@ApiOperation("分页查询城市索引-嵌套父文档")
	@RequestMapping(value = "/search/city", method = RequestMethod.POST)
	@ResponseBody
	public DataGrid<Object> listCitys(@RequestParam(value="current") int current, @RequestParam(value="rowCount") int rowCount
		,@RequestParam(value="searchPhrase") String searchPhrase) {
		DataGrid<Object> grid = new DataGrid<Object>();
		List<Object> data = new ArrayList<>();
		ElasticSearchRequest request = new ElasticSearchRequest();
		QueryCommand query = new QueryCommand();
		query.setIndexname("city");
		if (StringUtils.isBlank(searchPhrase)) {
			query.setKeyWords("*");
		} else {
			query.setKeyWords(searchPhrase);
		}
		query.setRows(rowCount);
		query.setStart((current-1)*rowCount);
		request.setQuery(query);
		SearchResponse searchResponse;
		if (StringUtils.isBlank(searchPhrase)) {
			searchResponse = searchService.query_string(request);
		} else {
			searchResponse = searchService.matchNestedObjectSearch("country", "city", "country.countryname", searchPhrase, current, rowCount);
		}
		SearchHits hits = searchResponse.getHits();
		SearchHit[] searchHits = hits.getHits();
		for (SearchHit hit : searchHits) {
			Map<String, Object> highlights = new HashMap<String, Object>();
			Map<String, Object> map = hit.getSourceAsMap();
			// 获取高亮结果
			Map<String, HighlightField> highlightFields = hit.getHighlightFields();
			for (Map.Entry<String, HighlightField> entry : highlightFields.entrySet()) {
				String mapKey = entry.getKey();
				HighlightField mapValue = entry.getValue();
				Text[] fragments = mapValue.fragments();
				String fragmentString = fragments[0].string();
				highlights.put(mapKey, fragmentString);
			}
			map.put("highlight", highlights);
			data.add(map);
		}
		grid.setCurrent(current);
		grid.setRowCount(rowCount);
		grid.setRows(data);
		grid.setTotal(hits.getTotalHits().value);
		return grid;
	}

	@ApiOperation("join搜索-用城市搜国家")
	@RequestMapping(value = "/search/haschild", method = RequestMethod.POST)
	@ResponseBody
	public DataTable<Object> haschild(@RequestBody JoinParams param) {
		// 搜索结果
		List<Object> data = new ArrayList<Object>();
		SearchResponse searchResponse = searchService.hasChildSearch("city", "cityjoincountry", "cityname", param.getName(), param.getPagenum(), param.getPagesize());
		SearchHits hits = searchResponse.getHits();
		SearchHit[] searchHits = hits.getHits();
		for (SearchHit hit : searchHits) {
			Map<String, Object> map = hit.getSourceAsMap();
			data.add(map);
		}
		DataTable<Object> grid = new DataTable<Object>();
		grid.setDraw(UUID.randomUUID().toString());
		grid.setRecordsFiltered(hits.getTotalHits().value);
		grid.setLength(param.getPagesize());
		grid.setRecordsTotal(hits.getTotalHits().value);
		grid.setData(data);
		return grid;
	}

	@ApiOperation("join搜索-用国家搜城市")
	@RequestMapping(value = "/search/hasparent", method = RequestMethod.POST)
	@ResponseBody
	public DataTable<Object> hasparent(@RequestBody JoinParams param) {
		// 搜索结果
		List<Object> data = new ArrayList<Object>();
		SearchResponse searchResponse = searchService.hasParentSearch("country", "cityjoincountry", "country", param.getName(), param.getPagenum(), param.getPagesize());
		SearchHits hits = searchResponse.getHits();
		SearchHit[] searchHits = hits.getHits();
		for (SearchHit hit : searchHits) {
			Map<String, Object> map = hit.getSourceAsMap();
			data.add(map);
		}
		DataTable<Object> grid = new DataTable<Object>();
		grid.setDraw(UUID.randomUUID().toString());
		grid.setRecordsFiltered(hits.getTotalHits().value);
		grid.setRecordsTotal(hits.getTotalHits().value);
		grid.setLength(param.getPagesize());
		grid.setData(data);
		return grid;
	}

	@ApiOperation("导出搜索结果为Excel")
	@RequestMapping(value="/search/exportExcel",method = RequestMethod.POST)
	@ResponseBody
	public void exportExcel(HttpServletResponse response, @RequestBody ElasticSearchRequest query) {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();						// 创建工作簿对象
			HSSFSheet sheet = workbook.createSheet("sheet1");
			// 搜索结果
			SearchResponse searchResponse = searchService.query_string(query);
			SearchHits hits = searchResponse.getHits();
			SearchHit[] searchHits = hits.getHits();
			if (searchHits.length>0){
				// 写列头
				SearchHit first = searchHits[0];
				HSSFRow frow = sheet.createRow(0);
				Map<String, Object> fmap = first.getSourceAsMap();
				int fcol = 0;
				for(String key : fmap.keySet())
				{
					if (key.contains("@"))
					{
						continue;
					} else {
						HSSFCell cell = null;   //设置单元格的数据类型
						cell = frow.createCell(fcol, HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(key);
					}
					fcol++;
				}

				for (int i = 0; i< searchHits.length; i++) {
					SearchHit hit = searchHits[i];
					HSSFRow row = sheet.createRow(i+1);

					Map<String, Object> map = hit.getSourceAsMap();

					int col = 0;
					for(String key : map.keySet())
					{
						if (key.contains("@"))
							continue;
						if (map.get(key) == null && !("id".equals(key))) {
							HSSFCell  cell = null;   //设置单元格的数据类型
							cell = row.createCell(col, HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue("");
						}  else {
							HSSFCell  cell = null;   //设置单元格的数据类型
							cell = row.createCell(col, HSSFCell.CELL_TYPE_STRING);
							String cellvalue = map.get(key).toString();
							cell.setCellValue(cellvalue);
						}
						col++;
					}
				}
			} else {
				HSSFRow frow = sheet.createRow(0);
			}
			ByteArrayOutputStream os=new ByteArrayOutputStream();
			try {
				workbook.write(os);
			} catch (IOException e) {
				e.printStackTrace();
			}

			byte[] content=os.toByteArray();
			InputStream is=new ByteArrayInputStream(content);
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=AllUsers.xls");
			ServletOutputStream output = response.getOutputStream();
			IOUtils.copy(is, output);
		} catch (HttpMessageNotReadableException hex) {
			hex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
