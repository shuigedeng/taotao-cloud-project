package com.taotao.cloud.sys.biz.springboot.mysql.service.impl;

import java.util.List;

import com.hrhx.springboot.mysql.service.CnweatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.hrhx.springboot.domain.Cnweather;
/**
 * 
 * @author duhongming
 *
 */
@Service
public class CnweatherServiceImpl implements CnweatherService {
	
    @Autowired
    private JdbcTemplate jdbcTemplate;

	@Override
	public List<Cnweather> getByCityNameAndStartDateAndEndDate(String  cityName,String startDate,String endDate) throws Exception{
		try{
			return jdbcTemplate.query("select * from cnweather where city_name=? and date_str between ? and ?",new Object[]{cityName,startDate,endDate},new BeanPropertyRowMapper<>(Cnweather.class));
		}catch(Exception e){
			throw new Exception();
		}
	}
    
 
}
