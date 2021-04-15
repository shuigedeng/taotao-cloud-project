package com.taotao.cloud.spring.source.spring_annotation.test;

import com.taotao.cloud.spring.source.spring_annotation.bean.Boss;
import com.taotao.cloud.spring.source.spring_annotation.bean.Car;
import com.taotao.cloud.spring.source.spring_annotation.bean.Color;
import com.taotao.cloud.spring.source.spring_annotation.config.MainConifgOfAutowired;
import com.taotao.cloud.spring.source.spring_annotation.service.BookService;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class IOCTest_Autowired {

	@Test
	public void test01(){
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConifgOfAutowired.class);

		BookService bookService = applicationContext.getBean(BookService.class);
		System.out.println(bookService);

		//BookDao bean = applicationContext.getBean(BookDao.class);
		//System.out.println(bean);

		Boss boss = applicationContext.getBean(Boss.class);
		System.out.println(boss);
		Car car = applicationContext.getBean(Car.class);
		System.out.println(car);

		Color color = applicationContext.getBean(Color.class);
		System.out.println(color);
		System.out.println(applicationContext);
		applicationContext.close();
	}

}
