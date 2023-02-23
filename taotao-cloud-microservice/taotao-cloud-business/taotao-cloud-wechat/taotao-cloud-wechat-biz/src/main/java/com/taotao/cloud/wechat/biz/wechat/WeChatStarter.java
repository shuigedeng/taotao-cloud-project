package com.taotao.cloud.wechat.biz.wechat;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * 微信操作封装
 *
 * @author xxm
 * @date 2022/7/15
 */
@ComponentScan
@MapperScan(annotationClass = Mapper.class)
@ConfigurationPropertiesScan
public class WeChatStarter {

}
