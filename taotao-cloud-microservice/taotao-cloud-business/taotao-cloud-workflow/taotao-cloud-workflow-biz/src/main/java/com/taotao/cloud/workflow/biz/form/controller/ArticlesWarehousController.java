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
import com.taotao.cloud.workflow.biz.common.model.form.articleswarehous.ArticlesWarehousForm;
import com.taotao.cloud.workflow.biz.common.model.form.articleswarehous.ArticlesWarehousInfoVO;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.ArticlesWarehousEntity;
import com.taotao.cloud.workflow.biz.form.service.ArticlesWarehousService;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 用品入库申请表 */
@Tag(tags = "用品入库申请表", value = "ArticlesWarehous")
@RestController
@RequestMapping("/api/workflow/Form/ArticlesWarehous")
public class ArticlesWarehousController {

    @Autowired
    private ArticlesWarehousService articlesWarehousService;

    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取用品入库申请表信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取用品入库申请表信息")
    @GetMapping("/{id}")
    public Result<ArticlesWarehousInfoVO> info(@PathVariable("id") String id, String taskOperatorId)
            throws DataException {
        ArticlesWarehousInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JacksonUtils.getJsonToBean(operator.getDraftData(), ArticlesWarehousInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            ArticlesWarehousEntity entity = articlesWarehousService.getInfo(id);
            vo = JacksonUtils.getJsonToBean(entity, ArticlesWarehousInfoVO.class);
        }
        return Result.success(vo);
    }

    /**
     * 新建用品入库申请表
     *
     * @param articlesWarehousForm 表单对象
     * @return
     */
    @Operation("新建用品入库申请表")
    @PostMapping
    public Result create(@RequestBody @Valid ArticlesWarehousForm articlesWarehousForm) throws WorkFlowException {
        if (articlesWarehousForm.getEstimatePeople() != null
                && StringUtil.isNotEmpty(articlesWarehousForm.getEstimatePeople())
                && !RegexUtils.checkDigit2(articlesWarehousForm.getEstimatePeople())) {
            return Result.fail("数量只能输入正整数");
        }
        ArticlesWarehousEntity entity = JacksonUtils.getJsonToBean(articlesWarehousForm, ArticlesWarehousEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(articlesWarehousForm.getStatus())) {
            articlesWarehousService.save(entity.getId(), entity);
            return Result.success(MsgCode.SU002.get());
        }
        articlesWarehousService.submit(entity.getId(), entity, articlesWarehousForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 修改用品入库申请表
     *
     * @param articlesWarehousForm 表单对象
     * @param id 主键
     * @return
     */
    @Operation("修改用品入库申请表")
    @PutMapping("/{id}")
    public Result update(@RequestBody @Valid ArticlesWarehousForm articlesWarehousForm, @PathVariable("id") String id)
            throws WorkFlowException {
        if (articlesWarehousForm.getEstimatePeople() != null
                && StringUtil.isNotEmpty(articlesWarehousForm.getEstimatePeople())
                && !RegexUtils.checkDigit2(articlesWarehousForm.getEstimatePeople())) {
            return Result.fail("数量只能输入正整数");
        }
        ArticlesWarehousEntity entity = JacksonUtils.getJsonToBean(articlesWarehousForm, ArticlesWarehousEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(articlesWarehousForm.getStatus())) {
            articlesWarehousService.save(id, entity);
            return Result.success(MsgCode.SU002.get());
        }
        articlesWarehousService.submit(id, entity, articlesWarehousForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
