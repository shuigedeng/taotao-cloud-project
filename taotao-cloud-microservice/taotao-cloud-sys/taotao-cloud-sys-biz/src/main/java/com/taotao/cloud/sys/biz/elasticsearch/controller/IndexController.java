package com.taotao.cloud.sys.biz.elasticsearch.controller;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import boot.spring.elastic.service.IndexService;
import boot.spring.pagemodel.MSG;
import boot.spring.po.Country;
import boot.spring.po.Sougoulog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "索引接口")
@Controller
public class IndexController {
	
	@Autowired
	RestHighLevelClient client;
	
	@Autowired
	IndexService indexService;
	
	@RequestMapping(value="/index", method = RequestMethod.GET)
	String index() {
		return "index";
	}
	
	@ApiOperation("索引一个日志文档")
	@RequestMapping(value="/indexSougoulog", method = RequestMethod.POST)
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
	@RequestMapping(value="/indexJsonSougoulog", method = RequestMethod.POST)
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
	@RequestMapping(value="/createIndexMapping",method = RequestMethod.GET)
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
	@RequestMapping(value="/indexDocs",method = RequestMethod.GET)
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
	@RequestMapping(value="/createShopMapping",method = RequestMethod.GET)
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
	@RequestMapping(value="/importShops",method = RequestMethod.GET)
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
	@RequestMapping(value="/indexDoc/{indexname}/{id}",method = RequestMethod.POST)
	@ResponseBody
	MSG indexDoc(@PathVariable String indexname, @PathVariable String id, @RequestBody Map<String, Object> jsonMap){
		indexService.indexDoc(indexname, id, jsonMap);
		return new MSG("index success");
	}
	
	@ApiOperation("向索引修改一个文档")
	@RequestMapping(value="/indexDoc/{indexname}/{id}",method = RequestMethod.PUT)
	@ResponseBody
	MSG updateIndexDoc(@PathVariable String indexname, @PathVariable String id, @RequestBody Map<String, Object> jsonMap){
		indexService.updateDoc(indexname, id, jsonMap);
		return new MSG("index success");
	}	
	
	@ApiOperation("删除一个索引中的文档")
	@RequestMapping(value="/indexDocs/{indexname}/{id}",method = RequestMethod.DELETE)
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
	@RequestMapping(value="/createCityMapping",method = RequestMethod.GET)
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
	@RequestMapping(value="/importCitys",method = RequestMethod.GET)
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
	@RequestMapping(value="/createJoinMapping",method = RequestMethod.GET)
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
	@RequestMapping(value="/importJoinCitys",method = RequestMethod.GET)
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
		
}
