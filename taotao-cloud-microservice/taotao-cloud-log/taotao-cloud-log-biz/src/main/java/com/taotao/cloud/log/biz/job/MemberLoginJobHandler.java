/**
 * Project Name: my-projects
 * Package Name: com.taotao.cloud.uc.biz.job
 * Date: 2020/6/16 14:43
 * Author: dengtao
 */
package com.taotao.cloud.log.biz.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * <br>
 *
 * @author dengtao
 * @version v1.0.0
 * @date 2020/6/16 14:43
 */
@Component
public class MemberLoginJobHandler {

	@XxlJob("MemberLoginJobHandler")
	public ReturnT<String> userJobHandler(String param) throws Exception {
		XxlJobLogger.log("XXL-JOB, Hello World.");

		for (int i = 0; i < 5; i++) {
			XxlJobLogger.log("beat at:" + i);
			System.out.println("XXL-JOB测试-----" + i);
			TimeUnit.SECONDS.sleep(2);
		}
		return ReturnT.SUCCESS;
	}
}
