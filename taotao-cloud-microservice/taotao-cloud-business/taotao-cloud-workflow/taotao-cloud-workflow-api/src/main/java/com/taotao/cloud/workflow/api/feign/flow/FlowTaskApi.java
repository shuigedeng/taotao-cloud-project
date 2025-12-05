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

package com.taotao.cloud.workflow.api.feign.flow;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/** api接口 */
// @HttpExchange(name = FeignName.WORKFLOW_SERVER_NAME, fallback = FlowTaskApiFallback.class, path =
// "/Engine/FlowTask")
public interface FlowTaskApi {
//    /**
//     * 列表（待我审批）
//     *
//     * @return
//     */
//    @GetMapping("/GetWaitList")
//    List<FlowTaskEntity> getWaitList();
//
//    /**
//     * 列表（我已审批）
//     *
//     * @return
//     */
//    @GetMapping("/GetTrialList")
//    List<FlowTaskListModel> getTrialList();
//
//    /**
//     * 列表（待我审批）
//     *
//     * @return
//     */
//    @GetMapping("/GetAllWaitList")
//    List<FlowTaskListModel> getAllWaitList();
//
//    /**
//     * 列表（订单状态）
//     *
//     * @return
//     */
//    @GetMapping("/GetOrderStaList/{idAll}")
//    List<FlowTaskEntity> getOrderStaList(@PathVariable("idAll") String idAll);
//
//    /**
//     * 提交
//     *
//     * @param flowSumbitModel
//     * @throws WorkFlowException
//     */
//    @PostMapping("/Submit")
//    void submit(@RequestBody FlowSumbitModel flowSumbitModel) throws WorkFlowException;
//
//    /**
//     * 撤回
//     *
//     * @param flowRevokeModel
//     * @throws WorkFlowException
//     */
//    @PostMapping("/Revoke")
//    void revoke(@RequestBody FlowRevokeModel flowRevokeModel) throws WorkFlowException;
//
//    /**
//     * 流程节点
//     *
//     * @param id
//     * @return
//     */
//    @GetMapping("/getList/{id}")
//    List<FlowTaskNodeEntity> getList(@PathVariable("id") String id);
//
//    /**
//     * 获取流程引擎实体
//     *
//     * @param id
//     * @return
//     * @throws WorkFlowException
//     */
//    @PostMapping("/getFlowEngine/{id}")
//    FlowEngineEntity getInfo(@PathVariable("id") String id) throws WorkFlowException;
//
//    /**
//     * 获取流程引擎实体
//     *
//     * @param id
//     * @return
//     * @throws WorkFlowException
//     */
//    @PostMapping("/getEngineInfo/{id}")
//    FlowEngineEntity getEngineInfo(@PathVariable("id") String id);
//
//    /**
//     * 更新流程引擎
//     *
//     * @param id
//     * @param engineEntity
//     * @throws WorkFlowException
//     */
//    @PostMapping("/updateFlowEngine/{id}")
//    void update(@PathVariable("id") String id, @RequestBody FlowEngineEntity engineEntity) throws WorkFlowException;
//
//    /** 删除流程引擎 */
//    @PostMapping("/deleteFlowEngine")
//    void delete(@RequestBody FlowEngineEntity engineEntity) throws WorkFlowException;
//
//    /**
//     * 创建流程任务
//     *
//     * @param id
//     * @return
//     */
//    @PostMapping("/createFlowTask/{id}")
//    FlowTaskEntity getInfoSubmit(@PathVariable("id") String id);
//
//    /**
//     * 删除流程任务
//     *
//     * @param taskEntity
//     * @throws WorkFlowException
//     */
//    @PostMapping("/deleteFlowTask")
//    void deleteFlowTask(@RequestBody FlowTaskEntity taskEntity) throws WorkFlowException;
//
//    /**
//     * 查询引擎
//     *
//     * @param encode
//     * @return
//     * @throws WorkFlowException
//     */
//    @GetMapping("/getInfoByEnCode/{encode}")
//    FlowEngineEntity getInfoByEnCode(@PathVariable("encode") String encode) throws WorkFlowException;
//
//    /**
//     * 工作流的列表
//     *
//     * @return
//     */
//    @GetMapping("/getFlowFormList")
//    List<FlowEngineEntity> getFlowFormList();
//
//    /**
//     * 列表
//     *
//     * @return
//     */
//    @GetMapping("/getDelegateList")
//    List<FlowDelegateEntity> getDelegateList();
}
