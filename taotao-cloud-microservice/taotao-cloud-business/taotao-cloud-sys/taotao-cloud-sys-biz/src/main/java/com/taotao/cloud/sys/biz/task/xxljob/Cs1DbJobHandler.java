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

package com.taotao.cloud.sys.biz.task.xxljob;
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

import com.taotao.boot.common.utils.log.LogUtils;
import com.xxl.tool.response.Response;
import com.xxl.job.core.handler.annotation.XxlJob;
import java.util.concurrent.CountDownLatch;
import org.springframework.stereotype.Component;

/**
 * Cs1DbJobHandler
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 11:54:37
 */
@Component
public class Cs1DbJobHandler {

    // @Autowired
    // private ICs1DbService cs1DbService;

    @XxlJob("Cs1DbJobHandler")
    public Response<String> execute(String s) {
        try {
            String[] tableNameList = new String[] {"order_amazon", "order_amazon_detail"}; // 要同步的表名

            CountDownLatch latch = new CountDownLatch(tableNameList.length); // 设置与表相同的线程计数器，同时备份表
            for (String tableName : tableNameList) {
                new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    // cs1DbService.tableOperation(tableName);
                                    Thread.sleep(10000);
                                } catch (Exception e) {
                                    LogUtils.error(e);
                                } finally {
                                    latch.countDown();
                                }
                            }
                        })
                        .start();
            }
            latch.await();
            return Response.ofSuccess();
        } catch (InterruptedException e) {
            LogUtils.error(e);
            return Response.ofFail();
        }
    }
}
