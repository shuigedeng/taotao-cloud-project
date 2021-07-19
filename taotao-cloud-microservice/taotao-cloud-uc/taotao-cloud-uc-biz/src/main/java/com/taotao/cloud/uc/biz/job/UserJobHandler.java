/**
 * Project Name: my-projects
 * Package Name: com.taotao.cloud.uc.biz.job
 * Date: 2020/6/16 14:43
 * Author: shuigedeng
 */
package com.taotao.cloud.uc.biz.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

/**
 * <br>
 *
 * @author shuigedeng
 * @version v1.0.0
 * @since 2020/6/16 14:43
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
