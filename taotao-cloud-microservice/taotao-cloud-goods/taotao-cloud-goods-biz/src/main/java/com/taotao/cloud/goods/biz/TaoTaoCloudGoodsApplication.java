package com.taotao.cloud.goods.biz;


import com.taotao.cloud.web.annotation.TaoTaoCloudApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * TaoTaoCloudGoodsApplication
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-15 20:59:38
 */
//@ForestScan(basePackages = "com.taotao.cloud.sys.biz.forest")
@MapperScan(basePackages = "com.taotao.cloud.goods.biz.mapper")
//@EnableJpaRepositories(basePackages = "com.taotao.cloud.sys.biz.repository.inf")
@TaoTaoCloudApplication
public class TaoTaoCloudGoodsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaoTaoCloudGoodsApplication.class, args);
	}

}
