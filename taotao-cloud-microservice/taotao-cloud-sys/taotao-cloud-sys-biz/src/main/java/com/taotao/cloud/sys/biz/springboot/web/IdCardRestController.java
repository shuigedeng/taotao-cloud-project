package com.taotao.cloud.sys.biz.springboot.web;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hrhx.springboot.domain.IndentityInfoBean;
import com.hrhx.springboot.aop.ResultBean;
import com.hrhx.springboot.mysql.service.IdCardService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
/**
 * 
 * @author duhongming
 *
 */
@Api(value="根据身份证码获取信息",tags="js调用")
@RestController
@RequestMapping(value = "/idcard")
public class IdCardRestController {
	
	@Autowired
	private IdCardService idCardService;
	
	@ApiOperation(value = "根据身份证码获取信息", notes = "/idcard/query?idCard=320303198209095933")
	@ApiResponses({
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
    })
	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public ResultBean<IndentityInfoBean> query(
			@ApiParam(required = true, name = "idCard", value= "身份证号", defaultValue= "320303198209095933") 
			@RequestParam( value = "idCard") String idCard) throws IOException, ParseException {
		return new ResultBean<IndentityInfoBean>(idCardService.getIndentityInfoBean(idCard));
	}
}
