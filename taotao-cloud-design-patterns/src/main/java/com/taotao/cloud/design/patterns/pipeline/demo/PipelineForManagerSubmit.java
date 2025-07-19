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

import com.taotao.cloud.design.patterns.pipeline.DemoPipelineNode;
import com.taotao.cloud.design.patterns.pipeline.DemoPipelineProduct;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 管道工厂入口-审核流水线
 *
 * @author
 * @date 2023/05/15 19:52
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PipelineForManagerSubmit {

    /**
     * 审核-管道节点
     */
    private final DemoPipelineNode managerSubmitNode = new DemoPipelineNode();

    /**
     * 审核-管道任务-提交-防刷锁-加锁
     */
    private final CheckRequestLockJob checkRequestLockJob;

    /**
     * 审核-管道任务-提交-防刷锁-解锁
     */
    private final CheckRequestUnLockJob checkRequestUnLockJob;

    /**
     * 审核-管道任务-参数验证
     */
    private final ManagerCheckParamJob managerCheckParamJob;

    /**
     * 审核-管道任务-事务操作
     */
    private final ManagerSubmitJob managerSubmitJob;

    /**
     * 组装审核的处理链
     */
    @PostConstruct
    private void assembly() {
        assemblyManagerSubmit();
    }

    /**
     * 组装处理链
     */
    private void assemblyManagerSubmit() {

        managerSubmitNode
                // 参数验证及填充
                .flax(managerCheckParamJob)
                // 防刷锁
                .flax(checkRequestLockJob)
                // 事务操作
                .flax(managerSubmitJob)
                // 锁释放
                .flax((ignore) -> true, checkRequestUnLockJob);
    }

    /**
     * 审核-提交处理
     *
     * @param requestData 入参
     * @return
     */
    public DemoResp managerSubmitCheck(DemoReq requestData) {
        DemoPipelineProduct initialProduct = managerSubmitCheckInitial(requestData);
        DemoPipelineProduct finalProduct = managerSubmitNode.execute(initialProduct);
        if (Objects.isNull(finalProduct) || Objects.nonNull(finalProduct.getException())) {
            return DemoResp.buildRes("未知异常");
        }
        return finalProduct.getProductData().getUserResponseData();
    }

    /**
     * 审核-初始化申请的流水线数据
     *
     * @param requestData 入参
     * @return 初始的流水线数据
     */
    private DemoPipelineProduct managerSubmitCheckInitial(DemoReq requestData) {
        // 初始化
        return DemoPipelineProduct.builder()
                .signal(DemoPipelineProduct.DemoSignalEnum.NORMAL)
                .tradeId(UUID.randomUUID().toString())
                .productData(
                        DemoPipelineProduct.DemoProductData.builder()
                                .userRequestData(requestData)
                                .build())
                .build();
    }
}
