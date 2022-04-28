/**
 * Project Name: my-projects
 * Package Name: com.taotao.cloud.sys.biz.job
 * Date: 2020/6/16 14:43
 * Author: shuigedeng
 */
package com.taotao.cloud.sys.biz.timetask.xxljob;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * UserJobHandler
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 11:54:43
 */
@Component
public class UserJobHandler {

    @XxlJob("UserJobHandler")
    public ReturnT<String> userJobHandler(String param) throws Exception {
	    XxlJobHelper.log("XXL-JOB, Hello World.");

        for (int i = 0; i < 5; i++) {
	        XxlJobHelper.log("beat at:" + i);
            System.out.println("XXL-JOB测试-----" + i);
            TimeUnit.SECONDS.sleep(2);
        }
        return ReturnT.SUCCESS;
    }
}
