package com.taotao.cloud.sys.biz.springboot.mysql.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.hrhx.springboot.domain.News;
/**
 * 
 * @author duhongming
 *
 */
@Service
public class NewsService {
	
	 @Autowired
	 private JdbcTemplate jdbcTemplate;
	 
	 public void save(News news){
		 jdbcTemplate.update("insert into news(url, docno ,contenttitle ,content) values(?,?,?,?)",
				 news.getUrl(),news.getDocno(),news.getContenttitle(),news.getContent());
	 }
}
