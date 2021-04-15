package com.taotao.cloud.spring.source.spring_annotation.test;

import com.taotao.cloud.spring.source.spring_annotation.aop.MathCalculator;
import com.taotao.cloud.spring.source.spring_annotation.config.MainConfigOfAOP;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class IOCTest_AOP {

	@Test
	public void test01(){
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfAOP.class);

		//1ҪԼ
//		MathCalculator mathCalculator = new MathCalculator();
//		mathCalculator.div(1, 1);
		MathCalculator mathCalculator = applicationContext.getBean(MathCalculator.class);

		mathCalculator.div(1, 0);

		applicationContext.close();
	}

}
