package com.taotao.cloud.tenant.biz;

import com.taotao.cloud.common.utils.common.PropertyUtils;
import com.taotao.cloud.web.annotation.TaoTaoCloudApplication;
import org.springframework.boot.SpringApplication;


@TaoTaoCloudApplication
public class TaoTaoCloudTenantApplication {

	public static void main(String[] args) {
		PropertyUtils.setDefaultProperty("taotao-cloud-tenant");

		SpringApplication.run(TaoTaoCloudTenantApplication.class, args);

		//Environment env = application.getEnvironment();
		//log.info("\n----------------------------------------------------------\n\t" +
		//		"应用 '{}' 运行成功! 访问连接:\n\t" +
		//		"Swagger文档: \t\thttp://{}:{}/doc.html\n\t" +
		//		"数据库监控: \t\thttp://{}:{}/druid\n" +
		//		"----------------------------------------------------------",
		//	env.getProperty("spring.application.name"),
		//	InetAddress.getLocalHost().getHostAddress(),
		//	env.getProperty("server.port", "8080"),
		//	"127.0.0.1",
		//	env.getProperty("server.port", "8080"));
	}

}
