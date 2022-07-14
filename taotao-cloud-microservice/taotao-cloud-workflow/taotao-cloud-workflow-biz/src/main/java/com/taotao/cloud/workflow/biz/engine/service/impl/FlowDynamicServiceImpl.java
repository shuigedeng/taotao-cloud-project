package com.taotao.cloud.workflow.biz.engine.service.impl;

import com.taotao.cloud.common.utils.common.JsonUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taotao.cloud.workflow.biz.engine.entity.FlowEngineEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.model.flowtask.FlowTaskInfoVO;
import com.taotao.cloud.workflow.biz.engine.service.FlowDynamicService;
import com.taotao.cloud.workflow.biz.engine.service.FlowEngineService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskService;
import com.taotao.cloud.workflow.biz.engine.util.FlowDataUtil;
import com.taotao.cloud.workflow.biz.engine.util.ServiceAllUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.table.TableModel;

/**
 * 在线开发工作流
 *
 */
@Slf4j
@Service
public class FlowDynamicServiceImpl implements FlowDynamicService {

    @Autowired
    private FlowTaskService flowTaskService;
    @Autowired
    private FlowEngineService flowEngineService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private FlowDataUtil flowDataUtil;
    @Autowired
    private ServiceAllUtil serviceUtil;

    @Override
    public FlowTaskInfoVO info(FlowTaskEntity entity, String taskOperatorId) throws WorkFlowException {
        FlowEngineEntity flowEntity = flowEngineService.getInfo(entity.getFlowId());
        List<TableModel> tableModelList = JsonUtil.getJsonToList(flowEntity.getFlowTables(), TableModel.class);
        FlowTaskInfoVO vo = JsonUtil.getJsonToBean(entity, FlowTaskInfoVO.class);
        boolean infoData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo.setData(operator.getDraftData());
                    infoData = false;
                }
            }
        }
        if (infoData) {
            //formTempJson
            FormDataModel formData = JsonUtil.getJsonToBean(entity.getFlowForm(), FormDataModel.class);
            List<FieLdsModel> list = JsonUtil.getJsonToList(formData.getFields(), FieLdsModel.class);
            DbLinkEntity link = serviceUtil.getDbLink(flowEntity.getDbLinkId());
            Map<String, Object> result = flowDataUtil.info(list, entity, tableModelList, false, link);
            vo.setData(JsonUtil.getObjectToString(result));
        }
        return vo;
    }

    @Override
    public void save(String id, FlowTaskForm flowTaskForm) throws WorkFlowException {
        String flowId = flowTaskForm.getFlowId();
        String data = flowTaskForm.getData();
        FlowEngineEntity entity = flowEngineService.getInfo(flowId);
        UserInfo info = userProvider.get();
        String billNo = "单据规则不存在";
        String title = info.getUserName() + "的" + entity.getFullName();
        String formId = RandomUtil.uuId();
        //tableJson
        List<TableModel> tableModelList = JsonUtil.getJsonToList(entity.getFlowTables(), TableModel.class);
        //formTempJson
        FormDataModel formData = JsonUtil.getJsonToBean(entity.getFormData(), FormDataModel.class);
        List<FieLdsModel> list = JsonUtil.getJsonToList(formData.getFields(), FieLdsModel.class);
        RecursionForm recursionForm = new RecursionForm(list, tableModelList);
        List<FormAllModel> formAllModel = new ArrayList<>();
        FormCloumnUtil.recursionForm(recursionForm, formAllModel);
        //主表的单据数据
        Map<String, String> billData = new HashMap<>(16);
        boolean type = id != null;
        if (type) {
            formId = id;
        }
        //表单值
        Map<String, Object> dataMap = JsonUtil.stringToMap(data);
        Map<String, Object> result = new HashMap<>(16);
        DbLinkEntity link = serviceUtil.getDbLink(entity.getDbLinkId());
        if (type) {
            result = flowDataUtil.update(dataMap, list, tableModelList, formId, link);
        } else {
            result = flowDataUtil.create(dataMap, list, tableModelList, formId, billData, link);
        }
        //流程信息
        ModelUtil.save(id, flowId, formId, title, 1, billNo, result);
    }

    @Override
    public void submit(String id, FlowTaskForm flowTaskForm) throws WorkFlowException {
        String flowId = flowTaskForm.getFlowId();
        String data = flowTaskForm.getData();
        String freeUserId = flowTaskForm.getFreeApproverUserId();
        Map<String, List<String>> candidateList = flowTaskForm.getCandidateList();
        FlowEngineEntity entity = flowEngineService.getInfo(flowId);
        UserInfo info = userProvider.get();
        String billNo = "单据规则不存在";
        String title = info.getUserName() + "的" + entity.getFullName();
        String formId = RandomUtil.uuId();
        //tableJson
        List<TableModel> tableModelList = JsonUtil.getJsonToList(entity.getFlowTables(), TableModel.class);
        //formTempJson
        FormDataModel formData = JsonUtil.getJsonToBean(entity.getFormData(), FormDataModel.class);
        List<FieLdsModel> list = JsonUtil.getJsonToList(formData.getFields(), FieLdsModel.class);
        List<FormAllModel> formAllModel = new ArrayList<>();
        RecursionForm recursionForm = new RecursionForm(list, tableModelList);
        FormCloumnUtil.recursionForm(recursionForm, formAllModel);
        //主表的单据数据
        Map<String, String> billData = new HashMap<>(16);
        boolean type = id != null;
        if (type) {
            formId = id;
        }
        //表单值
        Map<String, Object> dataMap = JsonUtil.stringToMap(data);
        Map<String, Object> result = new HashMap<>(16);
        DbLinkEntity link = serviceUtil.getDbLink(entity.getDbLinkId());
        if (type) {
            result = flowDataUtil.update(dataMap, list, tableModelList, formId, link);
        } else {
            result = flowDataUtil.create(dataMap, list, tableModelList, formId, billData, link);
        }
        //流程信息
        ModelUtil.submit(id, flowId, formId, title, 1, billNo, result, freeUserId, candidateList);
    }

    @Override
    public Map<String, Object> getData(String flowId, String id) throws WorkFlowException {
        FlowTaskEntity entity = flowTaskService.getInfo(id);
        FlowEngineEntity flowentity = flowEngineService.getInfo(flowId);
        List<TableModel> tableModelList = JsonUtil.getJsonToList(flowentity.getFlowTables(), TableModel.class);
        //formTempJson
        FormDataModel formData = JsonUtil.getJsonToBean(entity.getFlowForm(), FormDataModel.class);
        List<FieLdsModel> list = JsonUtil.getJsonToList(formData.getFields(), FieLdsModel.class);
        DbLinkEntity link = serviceUtil.getDbLink(flowentity.getDbLinkId());
        Map<String, Object> resultData = flowDataUtil.info(list, entity, tableModelList, true, link);
        return resultData;
    }

}
