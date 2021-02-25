package com.taotao.cloud.spring.source.springmvc_annotation.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;

//Springɨcontroller;
@ComponentScan(value="com.atguigu",excludeFilters={
	@Filter(type=FilterType.ANNOTATION,classes={Controller.class})
})
public class RootConfig {

}
