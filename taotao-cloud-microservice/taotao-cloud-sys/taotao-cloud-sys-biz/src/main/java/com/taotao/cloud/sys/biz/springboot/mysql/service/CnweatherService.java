package com.taotao.cloud.sys.biz.springboot.mysql.service;

import java.util.List;

import com.hrhx.springboot.domain.Cnweather;
/**
 * 
 * @author duhongming
 *
 */
public interface CnweatherService {
	/**
	 * 获取某一个城市某一时间段的天气
	 * @param cityName
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	List<Cnweather> getByCityNameAndStartDateAndEndDate(String  cityName,String startDate,String endDate) throws Exception;
}
