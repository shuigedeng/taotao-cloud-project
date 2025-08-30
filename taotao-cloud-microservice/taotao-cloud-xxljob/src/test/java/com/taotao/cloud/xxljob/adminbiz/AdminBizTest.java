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

package com.taotao.cloud.xxljob.adminbiz;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.xxl.job.core.biz.AdminBiz;
import com.xxl.job.core.biz.client.AdminBizClient;
import com.xxl.job.core.biz.model.HandleCallbackParam;
import com.xxl.job.core.biz.model.RegistryParam;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobContext;
import com.xxl.job.core.enums.RegistryConfig;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * admin api test
 *
 * @author xuxueli 2017-07-28 22:14:52
 */
public class AdminBizTest {

    // admin-client
    private static String addressUrl = "http://127.0.0.1:8080/xxl-job-admin/";
    private static String accessToken = null;
    private static int timeoutSecond = 3;

    @Test
    public void callback() throws Exception {
        AdminBiz adminBiz = new AdminBizClient(addressUrl, accessToken, timeoutSecond);

        HandleCallbackParam param = new HandleCallbackParam();
        param.setLogId(1);
        param.setHandleCode(XxlJobContext.HANDLE_CODE_SUCCESS);

        List<HandleCallbackParam> callbackParamList = Arrays.asList(param);

        ReturnT<String> returnT = adminBiz.callback(callbackParamList);

        assertTrue(returnT.getCode() == ReturnT.ofSuccess()_CODE);
    }

    /**
     * registry executor
     *
     * @throws Exception
     */
    @Test
    public void registry() throws Exception {
        AdminBiz adminBiz = new AdminBizClient(addressUrl, accessToken, timeoutSecond);

        RegistryParam registryParam =
                new RegistryParam(
                        RegistryConfig.RegistType.EXECUTOR.name(),
                        "xxl-job-executor-example",
                        "127.0.0.1:9999");
        ReturnT<String> returnT = adminBiz.registry(registryParam);

        assertTrue(returnT.getCode() == ReturnT.ofSuccess()_CODE);
    }

    /**
     * registry executor remove
     *
     * @throws Exception
     */
    @Test
    public void registryRemove() throws Exception {
        AdminBiz adminBiz = new AdminBizClient(addressUrl, accessToken, timeoutSecond);

        RegistryParam registryParam =
                new RegistryParam(
                        RegistryConfig.RegistType.EXECUTOR.name(),
                        "xxl-job-executor-example",
                        "127.0.0.1:9999");
        ReturnT<String> returnT = adminBiz.registryRemove(registryParam);

        assertTrue(returnT.getCode() == ReturnT.ofSuccess()_CODE);
    }
}
