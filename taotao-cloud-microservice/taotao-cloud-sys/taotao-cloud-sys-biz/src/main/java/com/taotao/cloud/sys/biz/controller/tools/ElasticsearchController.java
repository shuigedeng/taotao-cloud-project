package com.taotao.cloud.sys.biz.controller.tools;

import com.taotao.cloud.sys.biz.service.IElasticsearchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ElasticsearchController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-02 16:31:03
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "工具管理端-elasticsearch管理API", description = "工具管理端-elasticsearch管理API")
@RequestMapping("/sys/tools/elasticsearch")
public class ElasticsearchController {

	private final IElasticsearchService elasticsearchService;

//	@Autowired
//	private ClusterApis clusterApis;
//
////    /**
////     * 数据量太大了, 使用分开的 api
////     * @param connName
////     * @return
////     * @throws IOException
////     */
////    @Deprecated
////    @GetMapping("/cluster/state")
////    public ClusterModel clusterModel(@NotNull String connName) throws IOException {
////        String address = loadAddress(connName);
////
////        JSONObject clusterHealth = clusterApis.clusterHealth(address);
////        JSONObject clusterNodes = clusterApis.clusterNodes(address);
////        JSONObject clusterState = clusterApis.clusterState(address);
////        JSONObject status = clusterApis.status(address);
////        JSONObject nodeStats = clusterApis.nodeStats(address);
////
////        ClusterModel clusterModel = new ClusterModel(clusterState, status, nodeStats, clusterNodes, clusterHealth);
////        return clusterModel;
////    }
//
//	@GetMapping("/cluster/health")
//	public JSONObject clusterHealth(@NotNull String address) throws IOException {
//		return elasticsearchService.clusterHealth(address);
//	}
//
//	@GetMapping("/cluster/state")
//	public JSONObject clusterState(@NotNull String address) throws IOException {
//		return elasticsearchService.clusterState(address);
//	}
//
//	@GetMapping("/cluster/nodes")
//	public JSONObject clusterNodes(@NotNull String address) throws IOException {
//		return elasticsearchService.clusterNodes(address);
//	}
//
//	@GetMapping("/node/stats")
//	public JSONObject nodeStats(@NotNull String address) throws IOException {
//		return elasticsearchService.nodeStats(address);
//	}
//
//	@GetMapping("/status")
//	public JSONObject status(@NotNull String address) throws IOException {
//		return elasticsearchService.status(address);
//	}
//
//	@PostMapping("/search/{connName}/{indexName}")
//	public JSONObject search(@PathVariable("connName") String address,
//		@PathVariable("indexName") String indexName, @RequestBody JSONObject dsl)
//		throws IOException {
//		return elasticsearchService.search(address, indexName, dsl);
//	}
//
//	@PostMapping("/search/{connName}")
//	public JSONObject search(@PathVariable("connName") String address, @RequestBody JSONObject dsl)
//		throws IOException {
//		return elasticsearchService.search(address, dsl);
//	}
//
//	/**
//	 * terms聚集接口
//	 *
//	 * @param content
//	 * @return
//	 * @throws Exception
//	 */
//	@ApiOperation("词条聚集")
//	@RequestMapping(value = "/aggs/termsAggs", method = RequestMethod.POST)
//	@ResponseBody
//	public ResultData termsAggs(@RequestBody QueryCommand query) throws Exception {
//		ResultData data = elasticsearchService.termsAggs(query);
//		return data;
//	}
//
//	/**
//	 * 范围聚集接口
//	 *
//	 * @param content
//	 * @return
//	 * @throws Exception
//	 */
//	@ApiOperation("范围聚集")
//	@RequestMapping(value = "/aggs/rangeAggs", method = RequestMethod.POST)
//	@ResponseBody
//	public ResultData rangeAggs(@RequestBody RangeQuery content) throws Exception {
//		ResultData data = elasticsearchService.rangeAggs(content);
//		return data;
//	}
//
//	/**
//	 * histogram聚集接口
//	 *
//	 * @param content
//	 * @return
//	 * @throws Exception
//	 */
//	@ApiOperation("直方图聚集")
//	@RequestMapping(value = "/aggs/histogramAggs", method = RequestMethod.POST)
//	@ResponseBody
//	public ResultData histogramAggs(@RequestBody QueryCommand query) throws Exception {
//		ResultData data = elasticsearchService.histogramAggs(query);
//		return data;
//	}
//
//	/**
//	 * datehistogram聚集接口
//	 *
//	 * @param content
//	 * @return
//	 * @throws Exception
//	 */
//	@ApiOperation("日期直方图聚集")
//	@RequestMapping(value = "/aggs/datehistogramAggs", method = RequestMethod.POST)
//	@ResponseBody
//	public ResultData datehistogramAggs(@RequestBody QueryCommand query) throws Exception {
//		ResultData data = elasticsearchService.datehistogramAggs(query);
//		return data;
//	}
//
//	@ApiOperation("按国家嵌套对象词条聚集")
//	@RequestMapping(value = "/aggs/nestedTermsAggs", method = RequestMethod.GET)
//	@ResponseBody
//	public ResultData nestedTermsAggs() throws Exception {
//		ResultData data = elasticsearchService.nestedTermsAggs();
//		return data;
//	}
//
//	@ApiOperation("索引一个日志文档")
//	@RequestMapping(value = "/index/indexSougoulog", method = RequestMethod.POST)
//	@ResponseBody
//	MSG indexDoc(@RequestBody Sougoulog log) {
//		return elasticsearchService.indexDoc(log);
//	}
//
//	@ApiOperation("索引一组json日志文档")
//	@RequestMapping(value = "/index/indexJsonSougoulog", method = RequestMethod.POST)
//	@ResponseBody
//	MSG indexJsonDoc(@RequestBody List<Sougoulog> log) {
//		return elasticsearchService.indexJsonDoc(log);
//	}
//
//	/**
//	 * 创建索引并设置字段类型
//	 *
//	 * @param indexname
//	 * @param indextype
//	 * @param jsonMap
//	 * @return
//	 * @throws Exception rest api: PUT sougoulog { "settings": { "analysis": { "filter": {
//	 *                   "my_filter": { "type": "stop", "stopwords": "" } }, "tokenizer": {
//	 *                   "my_tokenizer": { "type": "standard", "max_token_length": "1" } },
//	 *                   "analyzer": { "my_analyzer": { "type": "custom", "char_filter": "",
//	 *                   "tokenizer": "my_tokenizer", "filter": "my_filter" } } } }, "mappings": {
//	 *                   "_doc": { "properties": { "id": { "type": "integer" }, "clicknum": {
//	 *                   "type": "integer" }, "keywords": { "type": "text", "analyzer":
//	 *                   "my_analyzer", "fields": { "keyword": { "type": "keyword", "ignore_above":
//	 *                   256 } } }, "rank": { "type": "integer" }, "url": { "type": "text",
//	 *                   "analyzer": "my_analyzer" }, "userid": { "type": "text", "analyzer":
//	 *                   "my_analyzer", "fields": { "keyword": { "type": "keyword", "ignore_above":
//	 *                   256 } } }, "visittime": { "type": "date", "format": "HH:mm:ss" }
//	 *                   <p>
//	 *                   } } } }
//	 */
//	@ApiOperation("创建各索引并设置字段类型:sougoulog")
//	@RequestMapping(value = "/index/createIndexMapping", method = RequestMethod.GET)
//	@ResponseBody
//	MSG createMapping() throws Exception {
//		return elasticsearchService.createMapping();
//	}
//
//	@ApiOperation("向索引sougoulog导入数据")
//	@RequestMapping(value = "/index/indexDocs", method = RequestMethod.GET)
//	@ResponseBody
//	MSG indexDocs() throws Exception {
//		return elasticsearchService.indexDocs();
//	}
//
//	@ApiOperation("创建shop索引")
//	@RequestMapping(value = "/index/createShopMapping", method = RequestMethod.GET)
//	@ResponseBody
//	MSG createShopMapping() throws Exception {
//		return elasticsearchService.createShopMapping();
//	}
//
//	@ApiOperation("导入shop经纬度数据")
//	@RequestMapping(value = "/index/importShops", method = RequestMethod.GET)
//	@ResponseBody
//	MSG importShops() throws Exception {
//		return elasticsearchService.importShops();
//	}
//
//	@ApiOperation("向索引添加一个文档")
//	@RequestMapping(value = "/index/indexDoc/{indexname}/{id}", method = RequestMethod.POST)
//	@ResponseBody
//	MSG indexDoc(@PathVariable String indexname, @PathVariable String id,
//		@RequestBody Map<String, Object> jsonMap) {
//		return elasticsearchService.indexDoc(indexname, id, jsonMap);
//	}
//
//	@ApiOperation("向索引修改一个文档")
//	@RequestMapping(value = "/index/indexDoc/{indexname}/{id}", method = RequestMethod.PUT)
//	@ResponseBody
//	MSG updateIndexDoc(@PathVariable String indexname, @PathVariable String id,
//		@RequestBody Map<String, Object> jsonMap) {
//		return elasticsearchService.updateIndexDoc(indexname, id, jsonMap);
//	}
//
//	@ApiOperation("删除一个索引中的文档")
//	@RequestMapping(value = "/index/indexDocs/{indexname}/{id}", method = RequestMethod.DELETE)
//	@ResponseBody
//	MSG indexDocs(@PathVariable String indexname, @PathVariable String id) {
//		return elasticsearchService.indexDocs(indexname, id);
//	}
//
//	@ApiOperation("创建城市索引")
//	@RequestMapping(value = "/index/createCityMapping", method = RequestMethod.GET)
//	@ResponseBody
//	MSG createCityMapping() throws Exception {
//		return elasticsearchService.createCityMapping();
//	}
//
//
//	@ApiOperation("导入城市和国家数据-嵌套对象")
//	@RequestMapping(value = "/index/importCitys", method = RequestMethod.GET)
//	@ResponseBody
//	MSG importCitys() throws Exception {
//		return elasticsearchService.importCitys();
//	}
//
//	/**
//	 * 使用join字段构建索引一对多关联
//	 */
//	@ApiOperation("创建一对多关联索引")
//	@RequestMapping(value = "/index/createJoinMapping", method = RequestMethod.GET)
//	@ResponseBody
//	MSG createJoinMapping() throws Exception {
//		return elasticsearchService.createJoinMapping();
//	}
//
//	@ApiOperation("导入城市和国家数据-join")
//	@RequestMapping(value = "/index/importJoinCitys", method = RequestMethod.GET)
//	@ResponseBody
//	MSG importJoinCitys() throws Exception {
//		return elasticsearchService.importJoinCitys();
//	}
//
//	@ApiOperation("获取一个日志数据")
//	@RequestMapping(value = "/search/sougoulog/{id}", method = RequestMethod.GET)
//	@ResponseBody
//	public ResultData sougouLog(@PathVariable String id) throws Exception {
//		return elasticsearchService.sougouLog(id);
//	}
//
//	@ApiOperation("获取日志数据的总数")
//	@RequestMapping(value = "/sougoulognumber", method = RequestMethod.GET)
//	@ResponseBody
//	public ResultData sougouLogNumber() throws Exception {
//		return elasticsearchService.sougouLogNumber();
//	}
//
//	@ApiOperation("分页查询搜狗日志")
//	@RequestMapping(value = "/search/sougoulog", method = RequestMethod.POST)
//	@ResponseBody
//	public DataGrid<Object> listsougoulog(@RequestParam(value = "current") int current,
//		@RequestParam(value = "rowCount") int rowCount
//		, @RequestParam(value = "searchPhrase") String searchPhrase,
//		@RequestParam(value = "startdate", required = false) String startdate
//		, @RequestParam(value = "enddate", required = false) String enddate) {
//		return elasticsearchService.listSougouLog(current, rowCount, searchPhrase, startdate,
//			enddate);
//	}
//
//	@ApiOperation("query_string全字段查找-普通分页版")
//	@RequestMapping(value = "/search/query_string", method = RequestMethod.POST)
//	@ResponseBody
//	public ResultData queryString(@RequestBody ElasticSearchRequest request) {
//		return elasticsearchService.queryString(request);
//	}
//
//	@ApiOperation("query_string全字段查找-滚动分页版")
//	@RequestMapping(value = "/search/query_string/scroll", method = RequestMethod.POST)
//	@ResponseBody
//	public ResultData scrollQueryString(@RequestBody ElasticSearchRequest request) {
//		return elasticsearchService.scrollQueryString(request);
//	}
//
//	@ApiOperation("经纬度搜索")
//	@RequestMapping(value = "/search/geosearch", method = RequestMethod.POST)
//	@ResponseBody
//	public DataTable<Object> geoSearch(@RequestBody GeoDistance geo) {
//		return elasticsearchService.geoSearch(geo);
//	}
//
//	@ApiOperation("分页查询城市索引-嵌套父文档")
//	@RequestMapping(value = "/search/city", method = RequestMethod.POST)
//	@ResponseBody
//	public DataGrid<Object> listCitys(@RequestParam(value = "current") int current,
//		@RequestParam(value = "rowCount") int rowCount
//		, @RequestParam(value = "searchPhrase") String searchPhrase) {
//		return elasticsearchService.listCitys(current, rowCount, searchPhrase);
//	}
//
//	@ApiOperation("join搜索-用城市搜国家")
//	@RequestMapping(value = "/search/haschild", method = RequestMethod.POST)
//	@ResponseBody
//	public DataTable<Object> hasChild(@RequestBody JoinParams param) {
//		return elasticsearchService.hasChild(param);
//	}
//
//	@ApiOperation("join搜索-用国家搜城市")
//	@RequestMapping(value = "/search/hasparent", method = RequestMethod.POST)
//	@ResponseBody
//	public DataTable<Object> hasParent(@RequestBody JoinParams param) {
//		return elasticsearchService.hasParent(param);
//	}
//
//	@ApiOperation("导出搜索结果为Excel")
//	@RequestMapping(value = "/search/exportExcel", method = RequestMethod.POST)
//	@ResponseBody
//	public void exportExcel(HttpServletResponse response, @RequestBody ElasticSearchRequest query) {
//		elasticsearchService.exportExcel(response, query);
//	}
}
