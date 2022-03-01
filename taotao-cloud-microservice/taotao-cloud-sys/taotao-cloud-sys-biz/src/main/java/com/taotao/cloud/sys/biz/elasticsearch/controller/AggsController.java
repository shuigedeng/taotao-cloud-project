package com.taotao.cloud.sys.biz.elasticsearch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import boot.spring.elastic.service.AggsService;
import boot.spring.pagemodel.QueryCommand;
import boot.spring.pagemodel.RangeQuery;
import boot.spring.pagemodel.ResultData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "聚集统计接口")
@Controller
public class AggsController {
	@Autowired
	AggsService aggsService;
	
	/**
	 * terms聚集接口
	 * @param content
	 * @return
	 * @throws Exception 
	 */
    @ApiOperation("词条聚集")
	@RequestMapping(value = "/termsAggs", method = RequestMethod.POST)
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
	@RequestMapping(value = "/rangeAggs", method = RequestMethod.POST)
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
	@RequestMapping(value = "/histogramAggs", method = RequestMethod.POST)
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
	@RequestMapping(value = "/datehistogramAggs", method = RequestMethod.POST)
	@ResponseBody
    public ResultData datehistogramAggs(@RequestBody QueryCommand query) throws Exception{
		ResultData data = aggsService.datehistogramAggs(query);
		return data;
	}	
    
    @ApiOperation("按国家嵌套对象词条聚集")
	@RequestMapping(value = "/nestedTermsAggs", method = RequestMethod.GET)
	@ResponseBody
    public ResultData nestedTermsAggs() throws Exception{
		ResultData data = aggsService.nestedTermsAggs();
		return data;
	}    
    
	@RequestMapping(value = "/analysis", method = RequestMethod.GET)
    public String analysis() {
		return "analysis";
	}    
}
