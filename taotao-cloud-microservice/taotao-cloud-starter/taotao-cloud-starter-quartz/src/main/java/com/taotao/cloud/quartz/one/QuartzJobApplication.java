package com.taotao.cloud.quartz.one;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**   
* 定时任务
* @author xxm  
* @date 2021/11/2 
*/
@ComponentScan
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
public class QuartzJobApplication {
}
