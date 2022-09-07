package com.taotao.cloud.data.mybatis.plus.datascope.perm;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/**
* 数据权限
* @author xxm
* @date 2021/11/23
*/
@ComponentScan
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
public class DataPermApplication {
}
