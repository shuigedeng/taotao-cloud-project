/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.operation.biz.task;

import com.xxl.tool.response.Response;
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
public class WithdrawJobHandler {

    @XxlJob("WithdrawJobHandler")
    public Response<String> userJobHandler(String param) throws Exception {
        XxlJobHelper.log("XXL-JOB, Hello World.");

        for (int i = 0; i < 5; i++) {
            XxlJobHelper.log("beat at:" + i);
            LogUtils.info("XXL-JOB测试-----" + i);
            TimeUnit.SECONDS.sleep(2);
        }
        return Response.ofSuccess();
    }
}
