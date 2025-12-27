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

package com.taotao.cloud.workflow.biz.form.controller;

import com.taotao.boot.common.utils.json.JacksonUtils;
import com.taotao.cloud.workflow.biz.common.model.form.postbatchtab.PostBatchTabForm;
import com.taotao.cloud.workflow.biz.common.model.form.postbatchtab.PostBatchTabInfoVO;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.PostBatchTabEntity;
import com.taotao.cloud.workflow.biz.form.service.PostBatchTabService;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 发文呈批表 */
@Tag(tags = "发文呈批表", value = "PostBatchTab")
@RestController
@RequestMapping("/api/workflow/Form/PostBatchTab")
public class PostBatchTabController {

    @Autowired
    private PostBatchTabService postBatchTabService;

    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取发文呈批表信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取发文呈批表信息")
    @GetMapping("/{id}")
    public Result<PostBatchTabInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        PostBatchTabInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JacksonUtils.getJsonToBean(operator.getDraftData(), PostBatchTabInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            PostBatchTabEntity entity = postBatchTabService.getInfo(id);
            vo = JacksonUtils.getJsonToBean(entity, PostBatchTabInfoVO.class);
        }
        return Result.success(vo);
    }

    /**
     * 新建发文呈批表
     *
     * @param postBatchTabForm 表单对象
     * @return
     */
    @Operation("新建发文呈批表")
    @PostMapping
    public Result create(@RequestBody PostBatchTabForm postBatchTabForm) throws WorkFlowException {
        PostBatchTabEntity entity = JacksonUtils.getJsonToBean(postBatchTabForm, PostBatchTabEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(postBatchTabForm.getStatus())) {
            postBatchTabService.save(entity.getId(), entity);
            return Result.success(MsgCode.SU002.get());
        }
        postBatchTabService.submit(entity.getId(), entity, postBatchTabForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 修改发文呈批表
     *
     * @param postBatchTabForm 表单对象
     * @param id 主键
     * @return
     */
    @Operation("修改发文呈批表")
    @PutMapping("/{id}")
    public Result update(@RequestBody PostBatchTabForm postBatchTabForm, @PathVariable("id") String id)
            throws WorkFlowException {
        PostBatchTabEntity entity = JacksonUtils.getJsonToBean(postBatchTabForm, PostBatchTabEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(postBatchTabForm.getStatus())) {
            postBatchTabService.save(id, entity);
            return Result.success(MsgCode.SU002.get());
        }
        postBatchTabService.submit(id, entity, postBatchTabForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
