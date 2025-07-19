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
import org.dromara.hutool.json.JSONUtil;
import org.springframework.stereotype.Service;

/**
 * 解锁-实现层
 *
 * @author
 * @date 2023/05/17 17:00
 */
@Service
@Slf4j
public class CheckRequestUnLockJob extends AbstractDemoJob {

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
        log.info("任务[{}]解锁,线程号:{}", JSONUtil.toJsonStr(userRequestData), tradeId);
        return DemoPipelineProduct.DemoSignalEnum.NORMAL;
    }
}
