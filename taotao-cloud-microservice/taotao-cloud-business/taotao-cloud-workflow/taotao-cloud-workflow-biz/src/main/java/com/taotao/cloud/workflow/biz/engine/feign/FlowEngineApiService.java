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

package com.taotao.cloud.workflow.biz.engine.feign;

import com.alibaba.fastjson2.JSONArray;
import com.taotao.cloud.workflow.api.feign.flow.FlowEngineApi;
import com.taotao.cloud.workflow.api.vo.entity.DictionaryDataEntity;
import com.taotao.cloud.workflow.biz.common.base.ActionResult;
import com.taotao.cloud.workflow.biz.common.base.vo.DownloadVO;
import com.taotao.cloud.workflow.biz.common.base.vo.ListVO;
import com.taotao.cloud.workflow.biz.common.base.vo.PageListVO;
import com.taotao.cloud.workflow.biz.common.base.vo.PaginationVO;
import com.taotao.cloud.workflow.biz.common.constant.MsgCode;
import com.taotao.cloud.workflow.biz.common.model.FormAllModel;
import com.taotao.cloud.workflow.biz.common.model.FormEnum;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.FlowEngineCrForm;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.FlowEngineInfoVO;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.FlowEngineListVO;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.FlowEngineModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.FlowEngineSelectVO;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.FlowEngineUpForm;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.FlowExportModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.FlowPageListVO;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.FlowPagination;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.PaginationFlowEngine;
import com.taotao.cloud.workflow.biz.common.model.visiual.FormDataField;
import com.taotao.cloud.workflow.biz.common.model.visiual.FormDataModel;
import com.taotao.cloud.workflow.biz.common.model.visiual.RecursionForm;
import com.taotao.cloud.workflow.biz.common.model.visiual.fields.FieLdsModel;
import com.taotao.cloud.workflow.biz.common.util.FileUtil;
import com.taotao.cloud.workflow.biz.common.util.JsonUtil;
import com.taotao.cloud.workflow.biz.common.util.enums.ModuleTypeEnum;
import com.taotao.cloud.workflow.biz.engine.entity.FlowEngineEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowTaskStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowEngineService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskService;
import com.taotao.cloud.workflow.biz.engine.util.ServiceAllUtil;
import com.taotao.cloud.workflow.biz.engine.util.VisualDevTableCre;
import com.taotao.cloud.workflow.biz.exception.WorkFlowException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/** 流程设计 */
@Api(tags = "流程引擎", value = "FlowEngine")
@Service
public class FlowEngineApiService implements FlowEngineApi {

    @Autowired
    private FlowEngineService flowEngineService;

    @Autowired
    private FlowTaskService flowTaskService;

    @Autowired
    private ServiceAllUtil serviceUtil;

    @Autowired
    private VisualDevTableCre visualDevTableCre;

    /**
     * 获取流程设计列表
     *
     * @return
     */
    @ApiOperation("获取流程引擎列表")
    @GetMapping
    public ActionResult list(FlowPagination pagination) {
        List<FlowEngineEntity> list = flowEngineService.getPageList(pagination);
        List<DictionaryDataEntity> dictionList = serviceUtil.getDictionName(
                list.stream().map(t -> t.getCategory()).toList());
        for (FlowEngineEntity entity : list) {
            DictionaryDataEntity dataEntity = dictionList.stream()
                    .filter(t -> t.getEnCode().equals(entity.getCategory()))
                    .findFirst()
                    .orElse(null);
            entity.setCategory(dataEntity != null ? dataEntity.getFullName() : "");
        }
        PaginationVO paginationVO = JsonUtil.getJsonToBean(pagination, PaginationVO.class);
        List<FlowPageListVO> listVO = JsonUtil.getJsonToList(list, FlowPageListVO.class);
        return ActionResult.page(listVO, paginationVO);
    }

    /**
     * 获取流程设计列表
     *
     * @return
     */
    @ApiOperation("流程引擎下拉框")
    @GetMapping("/Selector")
    public ActionResult<ListVO<FlowEngineListVO>> listSelect(Integer type) {
        PaginationFlowEngine pagination = new PaginationFlowEngine();
        pagination.setFormType(type);
        pagination.setEnabledMark(1);
        pagination.setType(0);
        List<FlowEngineListVO> treeList = flowEngineService.getTreeList(pagination, true);
        ListVO vo = new ListVO();
        vo.setList(treeList);
        return ActionResult.success(vo);
    }

    /**
     * 主表属性
     *
     * @return
     */
    @ApiOperation("表单主表属性")
    @GetMapping("/{id}/FormDataFields")
    public ActionResult<ListVO<FormDataField>> getFormDataField(@PathVariable("id") String id)
            throws WorkFlowException {
        FlowEngineEntity entity = flowEngineService.getInfo(id);
        List<FormDataField> formDataFieldList = new ArrayList<>();
        if (entity.getFormType() == 1) {
            List<FlowEngineModel> list = JsonUtil.getJsonToList(entity.getFormData(), FlowEngineModel.class);
            for (FlowEngineModel model : list) {
                FormDataField formDataField = new FormDataField();
                formDataField.setLabel(model.getFiledName());
                formDataField.setVModel(model.getFiledId());
                formDataFieldList.add(formDataField);
            }
        } else {
            // formTempJson
            FormDataModel formData = JsonUtil.getJsonToBean(entity.getFormData(), FormDataModel.class);
            List<FieLdsModel> list = JsonUtil.getJsonToList(formData.getFields(), FieLdsModel.class);
            List<TableModel> tableModelList = JsonUtil.getJsonToList(entity.getFlowTables(), TableModel.class);
            List<FormAllModel> formAllModel = new ArrayList<>();
            RecursionForm recursionForm = new RecursionForm(list, tableModelList);
            FormCloumnUtil.recursionForm(recursionForm, formAllModel);
            // 主表数据
            List<FormAllModel> mast = formAllModel.stream()
                    .filter(t -> FormEnum.mast.getMessage().equals(t.getWorkflowKey()))
                    .toList();
            for (FormAllModel model : mast) {
                FieLdsModel fieLdsModel = model.getFormColumnModel().getFieLdsModel();
                String vmodel = fieLdsModel.getVModel();
                String workflowKey = fieLdsModel.getConfig().getWorkflowKey();
                if (StringUtil.isNotEmpty(vmodel)
                        && !WorkflowKeyConsts.RELATIONFORM.equals(workflowKey)
                        && !WorkflowKeyConsts.RELATIONFLOW.equals(workflowKey)) {
                    FormDataField formDataField = new FormDataField();
                    formDataField.setLabel(fieLdsModel.getConfig().getLabel());
                    formDataField.setVModel(fieLdsModel.getVModel());
                    formDataFieldList.add(formDataField);
                }
            }
        }
        ListVO<FormDataField> listVO = new ListVO();
        listVO.setList(formDataFieldList);
        return ActionResult.success(listVO);
    }

    /**
     * 列表
     *
     * @return
     */
    @ApiOperation("表单列表")
    @GetMapping("/{id}/FieldDataSelect")
    public ActionResult<ListVO<FlowEngineSelectVO>> getFormData(@PathVariable("id") String id) {
        List<FlowTaskEntity> flowTaskList =
                flowTaskService
                        .getTaskList(id, FlowTaskEntity::getId, FlowTaskEntity::getFullName, FlowTaskEntity::getEnCode)
                        .stream()
                        .filter(t -> FlowTaskStatusEnum.Adopt.getCode().equals(t.getStatus()))
                        .toList();
        List<FlowEngineSelectVO> vo = new ArrayList<>();
        for (FlowTaskEntity taskEntity : flowTaskList) {
            FlowEngineSelectVO selectVO = JsonUtil.getJsonToBean(taskEntity, FlowEngineSelectVO.class);
            selectVO.setFullName(taskEntity.getFullName() + "/" + taskEntity.getEnCode());
            vo.add(selectVO);
        }
        ListVO listVO = new ListVO();
        listVO.setList(vo);
        return ActionResult.success(listVO);
    }

    /**
     * 可见引擎下拉框
     *
     * @return
     */
    @ApiOperation("可见引擎下拉框")
    @GetMapping("/ListAll")
    public ActionResult<ListVO<FlowEngineListVO>> listAll() {
        PaginationFlowEngine pagination = new PaginationFlowEngine();
        List<FlowEngineListVO> treeList = flowEngineService.getTreeList(pagination, false);
        ListVO vo = new ListVO();
        vo.setList(treeList);
        return ActionResult.success(vo);
    }

    /**
     * 可见的流程引擎列表
     *
     * @return
     */
    @ApiOperation("可见的流程引擎列表")
    @GetMapping("/PageListAll")
    public ActionResult<PageListVO<FlowPageListVO>> listAll(FlowPagination pagination) {
        List<FlowEngineEntity> list = flowEngineService.getListAll(pagination, true);
        PaginationVO paginationVO = JsonUtil.getJsonToBean(pagination, PaginationVO.class);
        List<FlowPageListVO> listVO = JsonUtil.getJsonToList(list, FlowPageListVO.class);
        return ActionResult.page(listVO, paginationVO);
    }

    /**
     * 获取流程引擎信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取流程引擎信息")
    @GetMapping("/{id}")
    public ActionResult<FlowEngineInfoVO> info(@PathVariable("id") String id) throws WorkFlowException {
        FlowEngineEntity flowEntity = flowEngineService.getInfo(id);
        FlowEngineInfoVO vo = JsonUtil.getJsonToBean(flowEntity, FlowEngineInfoVO.class);
        return ActionResult.success(vo);
    }

    /**
     * 新建流程设计
     *
     * @return
     */
    @ApiOperation("新建流程引擎")
    @PostMapping
    public ActionResult create(@RequestBody @Valid FlowEngineCrForm flowEngineCrForm) throws WorkFlowException {
        FlowEngineEntity flowEngineEntity = JsonUtil.getJsonToBean(flowEngineCrForm, FlowEngineEntity.class);
        if (flowEngineService.isExistByFullName(flowEngineEntity.getFullName(), flowEngineEntity.getId())) {
            return ActionResult.fail("流程名称不能重复");
        }
        if (flowEngineService.isExistByEnCode(flowEngineEntity.getEnCode(), flowEngineEntity.getId())) {
            return ActionResult.fail("流程编码不能重复");
        }
        flowEngineService.create(flowEngineEntity);
        return ActionResult.success(MsgCode.SU001.get());
    }

    /**
     * 更新流程设计
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("更新流程引擎")
    @PutMapping("/{id}")
    public ActionResult update(@PathVariable("id") String id, @RequestBody @Valid FlowEngineUpForm flowEngineUpForm)
            throws WorkFlowException {
        FlowEngineEntity flowEngineEntity = JsonUtil.getJsonToBean(flowEngineUpForm, FlowEngineEntity.class);
        if (flowEngineService.isExistByFullName(flowEngineUpForm.getFullName(), id)) {
            return ActionResult.fail("流程名称不能重复");
        }
        if (flowEngineService.isExistByEnCode(flowEngineUpForm.getEnCode(), id)) {
            return ActionResult.fail("流程编码不能重复");
        }
        boolean flag = flowEngineService.updateVisible(id, flowEngineEntity);
        if (flag == false) {
            return ActionResult.success(MsgCode.FA002.get());
        }
        return ActionResult.success(MsgCode.SU004.get());
    }

    /**
     * 删除流程设计
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("删除流程引擎")
    @DeleteMapping("/{id}")
    public ActionResult<String> delete(@PathVariable("id") String id) throws WorkFlowException {
        FlowEngineEntity entity = flowEngineService.getInfo(id);
        flowEngineService.delete(entity);
        return ActionResult.success(MsgCode.SU003.get());
    }

    /**
     * 复制流程表单
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("复制流程表单")
    @PostMapping("/{id}/Actions/Copy")
    public ActionResult copy(@PathVariable("id") String id) throws WorkFlowException {
        FlowEngineEntity flowEngineEntity = flowEngineService.getInfo(id);
        if (flowEngineEntity != null) {
            String copyNum = UUID.randomUUID().toString().substring(0, 5);
            flowEngineEntity.setFullName(flowEngineEntity.getFullName() + ".副本" + copyNum);
            flowEngineEntity.setEnCode(flowEngineEntity.getEnCode() + copyNum);
            flowEngineEntity.setCreatorTime(new Date());
            flowEngineEntity.setId(null);
            if (flowEngineEntity.getFormType() != 1) {
                List<TableModel> tableModelList =
                        JsonUtil.getJsonToList(flowEngineEntity.getFlowTables(), TableModel.class);
                if (tableModelList.size() == 0) {
                    throw new WorkFlowException(MsgCode.WF008.get());
                }
            }
            flowEngineService.copy(flowEngineEntity);
            return ActionResult.success(MsgCode.SU007.get());
        }
        return ActionResult.fail(MsgCode.FA004.get());
    }

    /**
     * 流程表单状态
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("更新流程表单状态")
    @PutMapping("/{id}/Actions/State")
    public ActionResult state(@PathVariable("id") String id) throws WorkFlowException {
        FlowEngineEntity entity = flowEngineService.getInfo(id);
        if (entity != null) {
            entity.setEnabledMark("1".equals(String.valueOf(entity.getEnabledMark())) ? 0 : 1);
            flowEngineService.update(id, entity);
            return ActionResult.success("更新表单成功");
        }
        return ActionResult.fail(MsgCode.FA002.get());
    }

    /**
     * 发布流程引擎
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("发布流程设计")
    @PostMapping("/Release/{id}")
    public ActionResult release(@PathVariable("id") String id) throws WorkFlowException {
        FlowEngineEntity entity = flowEngineService.getInfo(id);
        if (entity != null) {
            entity.setEnabledMark(1);
            flowEngineService.update(id, entity);
            return ActionResult.success(MsgCode.SU011.get());
        }
        return ActionResult.fail(MsgCode.FA011.get());
    }

    /**
     * 停止流程引擎
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("停止流程设计")
    @PostMapping("/Stop/{id}")
    public ActionResult stop(@PathVariable("id") String id) throws WorkFlowException {
        FlowEngineEntity entity = flowEngineService.getInfo(id);
        if (entity != null) {
            entity.setEnabledMark(0);
            flowEngineService.update(id, entity);
            return ActionResult.success(MsgCode.SU008.get());
        }
        return ActionResult.fail(MsgCode.FA008.get());
    }

    /**
     * 工作流导出
     *
     * @param id 主键值
     * @return
     * @throws WorkFlowException
     */
    @ApiOperation("工作流导出")
    @GetMapping("/{id}/Actions/ExportData")
    public ActionResult exportData(@PathVariable("id") String id) throws WorkFlowException {
        DownloadVO downloadVO = flowEngineService.exportData(id);
        return ActionResult.success(downloadVO);
    }

    /**
     * 工作流导入
     *
     * @param multipartFile 文件
     * @return
     * @throws WorkFlowException
     */
    @ApiOperation("工作流导入")
    @PostMapping(value = "/Actions/ImportData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ActionResult ImportData(@RequestPart("file") MultipartFile multipartFile) throws WorkFlowException {
        // 判断是否为.json结尾
        if (FileUtil.existsSuffix(multipartFile, ModuleTypeEnum.FLOW_FLOWENGINE.getTableName())) {
            return ActionResult.fail(MsgCode.IMP002.get());
        }
        // 获取文件内容
        String fileContent = FileUtil.getFileContent(multipartFile);
        FlowExportModel vo = JsonUtil.getJsonToBean(fileContent, FlowExportModel.class);
        return flowEngineService.ImportData(vo.getFlowEngine(), vo.getVisibleList());
    }

    @Override
    @ApiOperation("无表生成有表")
    @PostMapping(value = "/tableCre")
    public TableModels tableCre(@RequestBody TableCreModels tableCreModels) throws WorkFlowException {
        JSONArray jsonArray = JsonUtil.getListToJsonArray(tableCreModels.getJsonArray());
        List<TableModel> tableModels = visualDevTableCre.tableList(
                jsonArray, tableCreModels.getFormAllModel(), tableCreModels.getTable(), tableCreModels.getLinkId());
        TableModels tableModels1 = new TableModels();
        tableModels1.setJsonArray(JsonUtil.getJsonToList(jsonArray));
        tableModels1.setTable(tableModels);
        return tableModels1;
    }

    @Override
    @ApiOperation("创建流程引擎")
    @PostMapping(value = "/create")
    public void create(@RequestBody FlowEngineEntity flowEngineEntity) throws WorkFlowException {
        flowEngineService.create(flowEngineEntity);
    }

    @Override
    @ApiOperation("获取流程引擎")
    @GetMapping(value = "/getInfoByID/{id}")
    public FlowEngineEntity getInfoByID(@PathVariable("id") String id) {
        return flowEngineService.getById(id);
    }

    @Override
    @ApiOperation("获取流程引擎")
    @PostMapping(value = "/updateByID/{id}")
    public void updateByID(@PathVariable("id") String id, @RequestBody FlowEngineEntity flowEngineEntity)
            throws WorkFlowException {
        flowEngineService.update(id, flowEngineEntity);
    }

    @Override
    @PostMapping(value = "/getAppPageList")
    public FlowAppPageModel getAppPageList(@RequestBody FlowPagination pagination) {
        List<FlowEngineEntity> pageList = flowEngineService.getPageList(pagination);
        PaginationVO paginationVO = JsonUtil.getJsonToBean(pagination, PaginationVO.class);
        FlowAppPageModel model = new FlowAppPageModel(pageList, paginationVO);
        return model;
    }

    @Override
    @PostMapping(value = "/getFlowList")
    public List<FlowEngineEntity> getFlowList(@RequestBody List<String> id) {
        return flowEngineService.getFlowList(id);
    }
}
