package com.taotao.cloud.sys.biz.controller.tools.elasticsearch.controller;

import com.taotao.cloud.sys.biz.controller.tools.elasticsearch.elastic.service.SearchService;
import com.taotao.cloud.sys.biz.controller.tools.elasticsearch.pagemodel.DataGrid;
import com.taotao.cloud.sys.biz.controller.tools.elasticsearch.pagemodel.DataTable;
import com.taotao.cloud.sys.biz.controller.tools.elasticsearch.pagemodel.ElasticSearchRequest;
import com.taotao.cloud.sys.biz.controller.tools.elasticsearch.pagemodel.FilterCommand;
import com.taotao.cloud.sys.biz.controller.tools.elasticsearch.pagemodel.GeoDistance;
import com.taotao.cloud.sys.biz.controller.tools.elasticsearch.pagemodel.JoinParams;
import com.taotao.cloud.sys.biz.controller.tools.elasticsearch.pagemodel.QueryCommand;
import com.taotao.cloud.sys.biz.controller.tools.elasticsearch.pagemodel.ResultData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.util.IOUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(tags = "搜索接口")
@Controller
public class SearchController {

	@Autowired
	SearchService searchService;

	
	@RequestMapping(value = "/sougoulog", method = RequestMethod.GET)
	public String sougoulog() {
		return "sougoulog";
	}
	
	@RequestMapping(value = "/distance", method = RequestMethod.GET)
	public String distance() {
		return "distance";
	}	

	@RequestMapping(value = "/city", method = RequestMethod.GET)
	public String city() {
		return "city";
	}
	
	@RequestMapping(value = "/haschild", method = RequestMethod.GET)
	public String haschild() {
		return "haschild";
	}
	
	@RequestMapping(value = "/hasparent", method = RequestMethod.GET)
	public String hasparent() {
		return "hasparent";
	}
	
    @ApiOperation("获取一个日志数据")
	@RequestMapping(value = "/sougoulog/{id}", method = RequestMethod.GET)
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
	@RequestMapping(value = "/sougoulog", method = RequestMethod.POST)
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
	@RequestMapping(value = "/query_string", method = RequestMethod.POST)
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
	@RequestMapping(value = "/query_string/scroll", method = RequestMethod.POST)
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
	@RequestMapping(value = "/geosearch", method = RequestMethod.POST)
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
	@RequestMapping(value = "/city", method = RequestMethod.POST)
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
	@RequestMapping(value = "/haschild", method = RequestMethod.POST)
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
	@RequestMapping(value = "/hasparent", method = RequestMethod.POST)
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
	@RequestMapping(value="/exportExcel",method = RequestMethod.POST)
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
							HSSFCell  cell = null;   //设置单元格的数据类型
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
