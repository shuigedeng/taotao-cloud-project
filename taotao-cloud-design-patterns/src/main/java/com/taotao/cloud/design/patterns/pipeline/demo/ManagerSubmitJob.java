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

package com.taotao.cloud.design.patterns.pipeline.demo;

import com.taotao.cloud.design.patterns.pipeline.AbstractDemoJob;
import com.taotao.cloud.design.patterns.pipeline.DemoPipelineProduct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 审核-信息提交-业务实现
 *
 * @author
 * @date 2023/05/12 14:36
 */
@Service
@Slf4j
public class ManagerSubmitJob extends AbstractDemoJob {

    /**
     * 子类执行逻辑
     *
     * @param tradeId     流程Id
     * @param productData 请求数据
     * @return
     * @throws Exception 异常
     */
    @Override
    DemoPipelineProduct.DemoSignalEnum execute(
            String tradeId, DemoPipelineProduct.DemoProductData productData) throws Exception {
        DemoReq userRequestData = productData.getUserRequestData();
        try {
            /*
             * DB操作
             */
            log.info("任务[{}]信息提交,线程号:{}", JSONUtil.toJsonStr(userRequestData), tradeId);
            productData.setUserResponseData(DemoResp.buildRes("成功"));
        } catch (Exception ex) {
            log.error("审核-信息提交-DB操作失败,入参:{}", JSONUtil.toJsonStr(userRequestData), ex);
            throw ex;
        }
        return DemoPipelineProduct.DemoSignalEnum.NORMAL;
    }
}
