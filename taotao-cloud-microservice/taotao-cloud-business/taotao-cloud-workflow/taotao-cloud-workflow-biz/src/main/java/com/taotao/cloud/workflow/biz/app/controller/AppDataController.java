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

package com.taotao.cloud.workflow.biz.app.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import jakarta.validation.Valid;
import taotao.cloud.workflow.biz.base.ActionResult;
import taotao.cloud.workflow.biz.base.vo.ListVO;
import taotao.cloud.workflow.biz.base.vo.PaginationVO;
import taotao.cloud.workflow.biz.engine.model.flowengine.FlowPagination;
import taotao.cloud.workflow.biz.entity.AppDataEntity;
import taotao.cloud.workflow.biz.model.AppDataCrForm;
import taotao.cloud.workflow.biz.model.AppDataListAllVO;
import taotao.cloud.workflow.biz.model.AppDataListVO;
import taotao.cloud.workflow.biz.model.AppFlowListAllVO;
import taotao.cloud.workflow.biz.service.AppDataService;
import taotao.cloud.workflow.biz.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * app常用数据
 *
 * @author 
 * 
 * 
 * @since 2021-07-08
 */
@Api(tags = "app常用数据", value = "data")
@RestController
@RequestMapping("/app/Data")
public class AppDataController {

    @Autowired
    private AppDataService appDataService;

    /**
     * 常用数据
     *
     * @return
     */
    @ApiOperation("常用数据")
    @GetMapping
    public ActionResult list(String type) {
        List<AppDataEntity> list = appDataService.getList(type);
        List<AppDataListVO> data = JsonUtil.getJsonToList(list, AppDataListVO.class);
        ListVO listVO = new ListVO();
        listVO.setList(data);
        return ActionResult.success(listVO);
    }

    /**
     * 新建
     *
     * @param appDataCrForm dto实体
     * @return
     */
    @PostMapping
    @ApiOperation("新建")
    public ActionResult create(@RequestBody @Valid AppDataCrForm appDataCrForm) {
        AppDataEntity entity = JsonUtil.getJsonToBean(appDataCrForm, AppDataEntity.class);
        if (appDataService.isExistByObjectId(entity.getObjectId())) {
            return ActionResult.fail("常用数据已存在");
        }
        appDataService.create(entity);
        return ActionResult.success("创建成功");
    }

    /**
     * 删除
     *
     * @param objectId 对象主键
     * @return
     */
    @ApiOperation("删除")
    @DeleteMapping("/{objectId}")
    public ActionResult create(@PathVariable("objectId") String objectId) {
        AppDataEntity entity = appDataService.getInfo(objectId);
        if (entity != null) {
            appDataService.delete(entity);
            return ActionResult.success("删除成功");
        }
        return ActionResult.fail("删除失败，数据不存在");
    }

    /**
     * 所有流程
     *
     * @return
     */
    @ApiOperation("所有流程")
    @GetMapping("/getFlowList")
    public ActionResult getFlowList(FlowPagination pagination) {
        List<AppFlowListAllVO> list = appDataService.getFlowList(pagination);
        PaginationVO paginationVO = JsonUtil.getJsonToBean(pagination, PaginationVO.class);
        return ActionResult.page(list, paginationVO);
    }

    /**
     * 所有应用
     *
     * @return
     */
    @ApiOperation("所有应用")
    @GetMapping("/getDataList")
    public ActionResult getAllList() {
        List<AppDataListAllVO> result = appDataService.getDataList("2");
        ListVO listVO = new ListVO();
        listVO.setList(result);
        return ActionResult.success(listVO);
    }

    /**
     * 删除app常用数据
     *
     * @return
     */
    @GetMapping("/deleObject/{id}")
    public void deleObject(@PathVariable("id") String id) {
        appDataService.delete(id);
    }
}
