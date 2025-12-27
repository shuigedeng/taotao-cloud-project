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


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.common.utils.json.JacksonUtils;
import com.taotao.cloud.workflow.api.vo.entity.DictionaryDataEntity;
import com.taotao.cloud.workflow.biz.common.base.vo.DownloadVO;
import com.taotao.cloud.workflow.biz.common.base.vo.PaginationVO;
import com.taotao.cloud.workflow.biz.common.config.ConfigValueUtil;
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
import com.taotao.cloud.workflow.biz.common.model.visiual.FormCloumnUtil;
import com.taotao.cloud.workflow.biz.common.model.visiual.FormDataField;
import com.taotao.cloud.workflow.biz.common.model.visiual.FormDataModel;
import com.taotao.cloud.workflow.biz.common.model.visiual.RecursionForm;
import com.taotao.cloud.workflow.biz.common.model.visiual.TableModel;
import com.taotao.cloud.workflow.biz.common.model.visiual.fields.FieLdsModel;
import com.taotao.cloud.workflow.biz.common.util.FileUtil;
import com.taotao.cloud.workflow.biz.common.util.enums.ModuleTypeEnum;
import com.taotao.cloud.workflow.biz.common.util.file.fileinfo.DataFileExport;
import com.taotao.cloud.workflow.biz.engine.entity.FlowEngineEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowTaskStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowEngineService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskService;
import com.taotao.cloud.workflow.biz.engine.util.ServiceAllUtil;
import com.taotao.cloud.workflow.biz.engine.util.VisualDevTableCre;
import com.taotao.cloud.workflow.biz.exception.WorkFlowException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/** 流程设计 */
@Validated
@Tag(name = "工作流程-流程引擎", description = "工作流程-流程引擎")
@RestController
@RequestMapping("/api/workflow/engine/flow-engine")
public class FlowEngineController {

    @Autowired
    private FlowEngineService flowEngineService;

    @Autowired
    private FlowTaskService flowTaskService;

    @Autowired
    private DataFileExport fileExport;

    @Autowired
    private ServiceAllUtil serviceUtil;

    @Autowired
    private ConfigValueUtil configValueUtil;

    @Autowired
    private VisualDevTableCre visualDevTableCre;

    @Operation(summary = "分页获取流程引擎列表", description = "分页获取流程引擎列表")
    @GetMapping("/page")
    public Result<PageResult<FlowPageListVO>> list(FlowPagination pagination) {
        IPage<FlowEngineEntity> entityPage = flowEngineService.getPageList(pagination);
        List<FlowEngineEntity> records = entityPage.getRecords();
        List<DictionaryDataEntity> dictionList = serviceUtil.getDictionName(
                records.stream().map(FlowEngineEntity::getCategory).toList());
        for (FlowEngineEntity entity : records) {
            DictionaryDataEntity dataEntity = dictionList.stream()
                    .filter(t -> t.getEnCode().equals(entity.getCategory()))
                    .findFirst()
                    .orElse(null);
            entity.setCategory(dataEntity != null ? dataEntity.getFullName() : "");
        }
        return Result.success(MpUtils.convertMybatisPage(entityPage, FlowPageListVO.class));
    }

    @Operation(summary = "获取流程设计列表", description = "获取流程设计列表")
    @GetMapping
    public Result<List<FlowEngineListVO>> listSelect(@RequestParam("type") Integer type) {
        PaginationFlowEngine pagination = new PaginationFlowEngine();
        pagination.setFormType(type);
        pagination.setEnabledMark(1);
        pagination.setType(0);
        List<FlowEngineListVO> treeList = flowEngineService.getTreeList(pagination, true);
        return Result.success(treeList);
    }

    @Operation(summary = "表单主表属性", description = "表单主表属性")
    @GetMapping("/form-data/fields/{id}")
    public Result<List<FormDataField>> getFormDataField(@PathVariable("id") String id) throws WorkFlowException {
        FlowEngineEntity entity = flowEngineService.getInfo(id);
        List<FormDataField> formDataFieldList = new ArrayList<>();
        if (entity.getFormType() == 1) {
            List<FlowEngineModel> list = JacksonUtils.toList(entity.getFormData(), FlowEngineModel.class);
            for (FlowEngineModel model : list) {
                FormDataField formDataField = new FormDataField();
                formDataField.setLabel(model.getFiledName());
                formDataField.setVModel(model.getFiledId());
                formDataFieldList.add(formDataField);
            }
        } else {
            // formTempJson
            FormDataModel formData = JacksonUtils.toObject(entity.getFormData(), FormDataModel.class);
            List<FieLdsModel> list = JacksonUtils.toList(formData.getFields(), FieLdsModel.class);
            List<TableModel> tableModelList = JacksonUtils.toList(entity.getFlowTables(), TableModel.class);

            List<FormAllModel> formAllModel = new ArrayList<>();
            RecursionForm recursionForm = new RecursionForm(list, tableModelList);
            FormCloumnUtil.recursionForm(recursionForm, formAllModel);

            // 主表数据
            List<FormAllModel> mast = formAllModel.stream()
                    .filter(t -> FormEnum.mast.getMessage().equals(t.getFlowKey()))
                    .toList();

            for (FormAllModel model : mast) {
                FieLdsModel fieLdsModel = model.getFormColumnModel().getFieLdsModel();
                String vmodel = fieLdsModel.getVModel();
                String flowKey = fieLdsModel.getConfig().getFlowKey();
                if (StrUtil.isNotEmpty(vmodel)
                        && !FlowKeyConsts.RELATIONFORM.equals(flowKey)
                        && !FlowKeyConsts.RELATIONFLOW.equals(flowKey)) {
                    FormDataField formDataField = new FormDataField();
                    formDataField.setLabel(fieLdsModel.getConfig().getLabel());
                    formDataField.setVModel(fieLdsModel.getVModel());
                    formDataFieldList.add(formDataField);
                }
            }
        }
        return Result.success(formDataFieldList);
    }

    @Operation(summary = "表单列表", description = "表单列表")
    @GetMapping("/field-data/select/{id}")
    public Result<List<FlowEngineSelectVO>> getFormData(@PathVariable("id") String id) {
        List<FlowTaskEntity> flowTaskList = flowTaskService.getTaskList(id).stream()
                .filter(t -> FlowTaskStatusEnum.Adopt.getCode().equals(t.getStatus()))
                .toList();

        List<FlowEngineSelectVO> vo = new ArrayList<>();
        for (FlowTaskEntity taskEntity : flowTaskList) {
            FlowEngineSelectVO selectVO = JacksonUtils.toObject(taskEntity, FlowEngineSelectVO.class);
            selectVO.setFullName(taskEntity.getFullName() + "/" + taskEntity.getEnCode());
            vo.add(selectVO);
        }
        return Result.success(vo);
    }

    @Operation(summary = "可见引擎下拉框", description = "可见引擎下拉框")
    @GetMapping("/visible")
    public Result<List<FlowEngineListVO>> listAll() {
        PaginationFlowEngine pagination = new PaginationFlowEngine();
        List<FlowEngineListVO> treeList = flowEngineService.getTreeList(pagination, false);
        return Result.success(treeList);
    }

    @Operation(summary = "可见的流程引擎列表", description = "可见的流程引擎列表")
    @GetMapping("/visible/page")
    public Result<PageResult<FlowPageListVO>> listAll(FlowPagination pagination) {
        List<FlowEngineEntity> list = flowEngineService.getListAll(pagination, true);

        PaginationVO paginationVO = JacksonUtils.getJsonToBean(pagination, PaginationVO.class);
        List<FlowPageListVO> listVO = JacksonUtils.getJsonToList(list, FlowPageListVO.class);
        return Result.page(listVO, paginationVO);
    }

    @Operation(summary = "获取流程引擎信息", description = "获取流程引擎信息")
    @GetMapping("/{id}")
    public Result<FlowEngineInfoVO> info(@PathVariable("id") String id) throws WorkFlowException {
        FlowEngineEntity flowEntity = flowEngineService.getInfo(id);
        FlowEngineInfoVO vo = JacksonUtils.toObject(flowEntity, FlowEngineInfoVO.class);
        return Result.success(vo);
    }

    @Operation(summary = "新建流程引擎", description = "新建流程引擎")
    @PostMapping
    public Result<Boolean> create(@Valid @RequestBody FlowEngineCrForm flowEngineCrForm) throws WorkFlowException {
        FlowEngineEntity flowEngineEntity = JacksonUtils.toObject(flowEngineCrForm, FlowEngineEntity.class);
        if (flowEngineService.isExistByFullName(flowEngineEntity.getFullName(), flowEngineEntity.getId())) {
            throw new WorkFlowException("流程名称不能重复");
        }

        if (flowEngineService.isExistByEnCode(flowEngineEntity.getEnCode(), flowEngineEntity.getId())) {
            throw new WorkFlowException("流程编码不能重复");
        }

        if (flowEngineEntity.getFormType() != 1) {
            FormDataModel formData = JacksonUtils.toObject(flowEngineEntity.getFormData(), FormDataModel.class);

            List<FieLdsModel> list = JacksonUtils.toList(formData.getFields(), FieLdsModel.class);
            List<TableModel> tableModelList = JacksonUtils.toList(flowEngineEntity.getFlowTables(), TableModel.class);
            RecursionForm recursionForm = new RecursionForm(list, tableModelList);
            List<FormAllModel> formAllModel = new ArrayList<>();
            if (FormCloumnUtil.repetition(recursionForm, formAllModel)) {
                throw new WorkFlowException("子表重复");
            }
        }
        return Result.success(flowEngineService.create(flowEngineEntity));
    }

    @Operation(summary = "更新流程引擎", description = "更新流程引擎")
    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable("id") String id, @RequestBody @Valid FlowEngineUpForm flowEngineUpForm)
            throws WorkFlowException {
        FlowEngineEntity flowEngineEntity = JacksonUtils.toObject(flowEngineUpForm, FlowEngineEntity.class);
        if (flowEngineService.isExistByFullName(flowEngineUpForm.getFullName(), id)) {
            throw new WorkFlowException("流程名称不能重复");
        }
        if (flowEngineService.isExistByEnCode(flowEngineUpForm.getEnCode(), id)) {
            throw new WorkFlowException("流程编码不能重复");
        }

        if (flowEngineEntity.getFormType() != 1) {
            FormDataModel formData = JacksonUtils.toObject(flowEngineEntity.getFormData(), FormDataModel.class);
            List<FieLdsModel> list = JacksonUtils.toList(formData.getFields(), FieLdsModel.class);
            List<TableModel> tableModelList = JacksonUtils.toList(flowEngineEntity.getFlowTables(), TableModel.class);
            RecursionForm recursionForm = new RecursionForm(list, tableModelList);
            List<FormAllModel> formAllModel = new ArrayList<>();
            if (FormCloumnUtil.repetition(recursionForm, formAllModel)) {
                throw new WorkFlowException("子表重复");
            }
        }
        return Result.success(flowEngineService.updateVisible(id, flowEngineEntity));
    }

    @Operation(summary = "删除流程引擎", description = "删除流程引擎")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable("id") String id) throws WorkFlowException {
        FlowEngineEntity entity = flowEngineService.getInfo(id);
        List<FlowTaskEntity> taskNodeList = flowTaskService.getTaskList(entity.getId());
        if (taskNodeList.size() > 0) {
            throw new WorkFlowException("引擎在使用，不可删除");
        }
        flowEngineService.delete(entity);
        return Result.success(true);
    }

    @Operation(summary = "复制流程表单", description = "复制流程表单")
    @PostMapping("/actions/copy/{id}")
    public Result<Boolean> copy(@PathVariable("id") String id) throws WorkFlowException {
        FlowEngineEntity flowEngineEntity = flowEngineService.getInfo(id);
        Optional.ofNullable(flowEngineEntity).orElseThrow(() -> new WorkFlowException("引擎不存在"));

        String copyNum = UUID.randomUUID().toString().substring(0, 5);
        flowEngineEntity.setFullName(flowEngineEntity.getFullName() + ".副本" + copyNum);
        flowEngineEntity.setEnCode(flowEngineEntity.getEnCode() + copyNum);
        flowEngineEntity.setCreatorTime(new Date());
        flowEngineEntity.setId(null);
        flowEngineService.copy(flowEngineEntity);

        return Result.success(true);
    }

    @Operation(summary = "更新流程表单状态", description = "更新流程表单状态")
    @PutMapping("/actions/state/{id}")
    public Result<Boolean> state(@PathVariable("id") String id) throws WorkFlowException {
        FlowEngineEntity entity = flowEngineService.getInfo(id);
        Optional.ofNullable(entity)
                .orElseThrow(() -> new WorkFlowException("引擎不存在"))
                .setEnabledMark("1".equals(String.valueOf(entity.getEnabledMark())) ? 0 : 1);
        flowEngineService.update(id, entity);

        return Result.success(true);
    }

    @Operation(summary = "发布流程引擎", description = "发布流程引擎")
    @PostMapping("/actions/release/{id}")
    public Result<Boolean> release(@PathVariable("id") String id) throws WorkFlowException {
        FlowEngineEntity entity = flowEngineService.getInfo(id);
        Optional.ofNullable(entity)
                .orElseThrow(() -> new WorkFlowException("引擎不存在"))
                .setEnabledMark(1);
        flowEngineService.update(id, entity);
        return Result.success(true);
    }

    @Operation(summary = "停止流程引擎", description = "停止流程引擎")
    @PostMapping("/actions/stop/{id}")
    public Result<Boolean> stop(@PathVariable("id") String id) throws WorkFlowException {
        FlowEngineEntity entity = flowEngineService.getInfo(id);
        Optional.ofNullable(entity)
                .orElseThrow(() -> new WorkFlowException("引擎在使用不存在"))
                .setEnabledMark(0);
        flowEngineService.update(id, entity);
        return Result.success(true);
    }

    @Operation(summary = "工作流导出", description = "工作流导出")
    @GetMapping("/actions/export/{id}")
    public Result exportData(@PathVariable("id") String id) throws WorkFlowException {
        FlowExportModel model = flowEngineService.exportData(id);
        DownloadVO downloadVO = fileExport.exportFile(
                model,
                configValueUtil.getTemporaryFilePath(),
                model.getFlowEngine().getFullName(),
                ModuleTypeEnum.FLOW_FLOWENGINE.getTableName());
        return Result.success(downloadVO);
    }

    @Operation(summary = "工作流导入", description = "工作流导入")
    @PostMapping(value = "/actions/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result ImportData(@RequestPart("file") MultipartFile multipartFile) throws WorkFlowException {
        // 判断是否为.json结尾
        if (FileUtil.existsSuffix(multipartFile, ModuleTypeEnum.FLOW_FLOWENGINE.getTableName())) {
            return Result.fail(MsgCode.IMP002.get());
        }
        // 获取文件内容
        String fileContent = FileUtil.getFileContent(multipartFile, configValueUtil.getTemporaryFilePath());
        FlowExportModel vo = JacksonUtils.getJsonToBean(fileContent, FlowExportModel.class);
        return flowEngineService.ImportData(vo.getFlowEngine(), vo.getVisibleList());
    }
}
