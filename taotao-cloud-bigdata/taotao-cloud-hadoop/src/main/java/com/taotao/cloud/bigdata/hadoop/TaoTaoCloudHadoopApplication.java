package com.taotao.cloud.bigdata.hadoop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 目前先作为一个单独的springboot项目
 * <p>
 * 之后如果有需要可以添加到toatoa cloud中 作为一个资源服务器( 添加依赖 添加注解 添加配置)
 *
 * @author dengtao
 * @since 2020/10/30 16:06
 * @version 1.0.0
 */
@SpringBootApplication
public class TaoTaoCloudHadoopApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaoTaoCloudHadoopApplication.class, args);
	}

}
