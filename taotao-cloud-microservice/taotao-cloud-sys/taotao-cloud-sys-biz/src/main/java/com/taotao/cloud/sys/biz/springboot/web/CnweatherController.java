package com.taotao.cloud.sys.biz.springboot.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hrhx.springboot.domain.Cnweather;
import com.hrhx.springboot.mysql.service.CnweatherService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
/**
 * 
 * @author duhongming
 *
 */
@RestController
@RequestMapping(value = "/cnweather")
public class CnweatherController {
	@Autowired
	private CnweatherService cnweatherService;

	@ApiOperation(value = "获取某个城市的一段时间的天气", notes = "/cnweather/query")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "cnweather", value = "天气详细信息", required = false, dataType = "Cnweather")
	})
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	public List<Cnweather> getByParentId(@ModelAttribute Cnweather cnweather) {
		try{//默认先执行Mysql				
			return cnweatherService.getByCityNameAndStartDateAndEndDate(cnweather.getCityName(), cnweather.getStartDate(), cnweather.getEndDate());
		}catch(Exception e){//出现异常用MongoDB			
			//return cnareaRepository.findByLevelAndParentId(cnarea.getLevel(),cnarea.getId());
			return null;
		}
	}

}
