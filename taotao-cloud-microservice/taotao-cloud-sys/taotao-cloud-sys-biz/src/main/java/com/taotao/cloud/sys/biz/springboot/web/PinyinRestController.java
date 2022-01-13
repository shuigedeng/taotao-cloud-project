package com.taotao.cloud.sys.biz.springboot.web;

import com.taotao.cloud.web.base.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hrhx.springboot.aop.ResultBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
/**
 * 
 * @author duhongming
 *
 */
@Api(value="根据汉字获取拼音",tags="java调用")
@RestController
@RequestMapping(value = "/pinyin")
public class PinyinRestController extends BaseController {
	
	@ApiOperation(value = "获取汉字拼音的全拼 ", notes = "/pinyin/analysis")
	@ApiResponses({
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
    })
	@RequestMapping(value = "/analysis", method = RequestMethod.POST)
	public ResultBean<String> analysis(
			
			@ApiParam(required = true, name = "chineseCharacter", value= "汉字字符", defaultValue="柜子" ) 
			@RequestParam( value = "chineseCharacter") String chineseCharacter,
			
			@ApiParam(required = true, name = "isUpperCase", value= "是否大写", defaultValue="true" ) 
			@RequestParam( value = "isUpperCase") String isUpperCase,
			
			@ApiParam(required = true, name = "isForS", value= "f是全拼；s是首拼", defaultValue="s" ) 
			@RequestParam( value = "isForS") String isForS
			
			) throws BadHanyuPinyinOutputFormatCombination{
		
		String pinyin = "";
//		if(StringUtils.isBlank(isForS)||S.equals(isForS)){
//			pinyin = PinyinUtil.chineseToPinYinS(chineseCharacter);
//		}else{
//			pinyin = PinyinUtil.chineseToPinYinF(chineseCharacter);
//		}
		
		if(StringUtils.isBlank(isUpperCase)||TRUE.equals(isUpperCase)){
			return new ResultBean<String>(pinyin.toUpperCase());
		}else{
			return new ResultBean<String>(pinyin.toLowerCase());
		}
	}
	
	@ApiOperation(value = "第一个全拼，剩余首拼 ", notes = "/pinyin/analysis")
	@ApiResponses({
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
    })
	@RequestMapping(value = "/xingming", method = RequestMethod.POST)
	public ResultBean<String> xingming(
			
			@ApiParam(required = true, name = "chineseCharacter", value= "姓名", defaultValue="杜洪明" ) 
			@RequestParam( value = "chineseCharacter") String chineseCharacter,
			
			@ApiParam(required = true, name = "isUpperCase", value= "是否大写", defaultValue="false" ) 
			@RequestParam( value = "isUpperCase") String isUpperCase
			
			) throws BadHanyuPinyinOutputFormatCombination{

		String pinyin = "";
//				PinyinUtil.getPinyinFirstCharExt(chineseCharacter);

		if(StringUtils.isBlank(isUpperCase)||FALSE.equals(isUpperCase)){
			return new ResultBean<String>(pinyin.toLowerCase());
		}else{
			return new ResultBean<String>(pinyin.toUpperCase());
		}
	}
	
}
