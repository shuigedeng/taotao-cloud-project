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

package com.taotao.cloud.workflow.biz.engine.controller;

import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.common.utils.common.JsonUtils;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.cloud.workflow.api.vo.UserEntity;
import com.taotao.cloud.workflow.biz.common.base.vo.PaginationVO;
import com.taotao.cloud.workflow.biz.common.constant.MsgCode;
import com.taotao.cloud.workflow.biz.common.model.engine.flowcomment.FlowCommentForm;
import com.taotao.cloud.workflow.biz.common.model.engine.flowcomment.FlowCommentInfoVO;
import com.taotao.cloud.workflow.biz.common.model.engine.flowcomment.FlowCommentListVO;
import com.taotao.cloud.workflow.biz.common.model.engine.flowcomment.FlowCommentPagination;
import com.taotao.cloud.workflow.biz.common.util.UploaderUtil;
import com.taotao.cloud.workflow.biz.covert.FlowTaskConvert;
import com.taotao.cloud.workflow.biz.engine.entity.FlowCommentEntity;
import com.taotao.cloud.workflow.biz.engine.service.FlowCommentService;
import com.taotao.cloud.workflow.biz.engine.util.ServiceAllUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 流程评论 */
@Validated
@Tag(name = "工作流程-流程评论", description = "工作流程-流程评论")
@RestController
@RequestMapping("/api/workflow/engine/flow-comment")
public class FlowCommentController {

    @Autowired
    private ServiceAllUtil serviceUtil;

    @Autowired
    private FlowCommentService flowCommentService;

    @Operation(summary = "分页获取流程评论列表", description = "分页获取流程评论列表")
    @GetMapping("/page")
    public Result<PageResult<FlowCommentListVO>> list(FlowCommentPagination pagination) {
        List<FlowCommentEntity> list = flowCommentService.getlist(pagination);
        List<FlowCommentListVO> listVO = FlowTaskConvert.INSTANCE.convertComment(list);

        List<Long> userId =
                list.stream().map(FlowCommentEntity::getCreatorUserId).toList();
        List<UserEntity> userName = serviceUtil.getUserName(userId);
        for (FlowCommentListVO commentModel : listVO) {
            UserEntity userEntity = userName.stream()
                    .filter(t -> t.getId().equals(commentModel.getCreatorUserId()))
                    .findFirst()
                    .orElse(null);
            commentModel.setIsDel(commentModel.getCreatorUserId().equals(SecurityUtils.getUserId()));
            commentModel.setCreatorUserName(userEntity != null ? userEntity.getRealName() : "");
            commentModel.setCreatorUserId(userEntity != null ? userEntity.getId() : 0L);
            if (userEntity != null) {
                commentModel.setCreatorUserHeadIcon(UploaderUtil.uploaderImg(userEntity.getHeadIcon()));
            }
        }
        PaginationVO vo = JsonUtils.getJsonToBean(pagination, PaginationVO.class);
        return Result.success(null);
    }

    @Operation(summary = "获取流程评论信息", description = "获取流程评论信息")
    @GetMapping("/{id}")
    public Result<FlowCommentInfoVO> info(@PathVariable("id") String id) {
        FlowCommentEntity entity = flowCommentService.getInfo(id);
        return Result.success(FlowTaskConvert.INSTANCE.convert(entity));
    }

    @Operation(summary = "新建流程评论", description = "新建流程评论")
    @PostMapping
    public Result<String> create(@RequestBody @Valid FlowCommentForm commentForm) throws DataException {
        FlowCommentEntity entity = FlowTaskConvert.INSTANCE.convert(commentForm);
        flowCommentService.create(entity);
        return Result.success(MsgCode.SU002.get());
    }

    @Operation(summary = "更新流程评论", description = "更新流程评论")
    @PutMapping("/{id}")
    public Result<String> update(@PathVariable("id") String id, @RequestBody @Valid FlowCommentForm commentForm)
            throws DataException {
        FlowCommentEntity info = flowCommentService.getInfo(id);
        if (info != null) {
            FlowCommentEntity entity = FlowTaskConvert.INSTANCE.convert(commentForm);
            flowCommentService.update(id, entity);
            return Result.success(MsgCode.SU004.get());
        }
        return Result.fail(MsgCode.FA002.get());
    }

    @Operation(summary = "删除流程评论", description = "删除流程评论")
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable("id") String id) {
        FlowCommentEntity entity = flowCommentService.getInfo(id);
        if (entity.getCreatorUserId().equals(SecurityUtils.getUserId())) {
            flowCommentService.delete(entity);
            return Result.success(MsgCode.SU003.get());
        }
        return Result.success(MsgCode.FA003.get());
    }
}
