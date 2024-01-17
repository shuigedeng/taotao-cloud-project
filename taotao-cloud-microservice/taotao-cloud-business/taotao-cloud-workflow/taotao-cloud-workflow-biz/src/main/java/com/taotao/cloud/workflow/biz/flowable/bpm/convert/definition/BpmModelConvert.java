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

package com.taotao.cloud.workflow.biz.flowable.bpm.convert.definition;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.date.DateUtils;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.module.bpm.controller.admin.definition.vo.model.*;
import com.taotao.cloud.flowable.biz.bpm.dal.dataobject.definition.BpmFormDO;
import com.taotao.cloud.flowable.biz.bpm.service.definition.dto.BpmModelMetaInfoRespDTO;
import com.taotao.cloud.flowable.biz.bpm.service.definition.dto.BpmProcessDefinitionCreateReqDTO;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.flowable.common.engine.impl.db.SuspensionState;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * 流程模型 Convert
 *
 * @author yunlongn
 */
@Mapper
public interface BpmModelConvert {

    BpmModelConvert INSTANCE = Mappers.getMapper(BpmModelConvert.class);

    default List<BpmModelPageItemRespVO> convertList(
            List<Model> list,
            Map<Long, BpmFormDO> formMap,
            Map<String, Deployment> deploymentMap,
            Map<String, ProcessDefinition> processDefinitionMap) {
        return CollectionUtils.convertList(list, model -> {
            BpmModelMetaInfoRespDTO metaInfo =
                    JsonUtils.parseObject(model.getMetaInfo(), BpmModelMetaInfoRespDTO.class);
            BpmFormDO form = metaInfo != null ? formMap.get(metaInfo.getFormId()) : null;
            Deployment deployment = model.getDeploymentId() != null ? deploymentMap.get(model.getDeploymentId()) : null;
            ProcessDefinition processDefinition =
                    model.getDeploymentId() != null ? processDefinitionMap.get(model.getDeploymentId()) : null;
            return convert(model, form, deployment, processDefinition);
        });
    }

    default BpmModelPageItemRespVO convert(
            Model model, BpmFormDO form, Deployment deployment, ProcessDefinition processDefinition) {
        BpmModelPageItemRespVO modelRespVO = new BpmModelPageItemRespVO();
        modelRespVO.setId(model.getId());
        modelRespVO.setCreateTime(DateUtils.of(model.getCreateTime()));
        // 通用 copy
        copyTo(model, modelRespVO);
        // Form
        if (form != null) {
            modelRespVO.setFormId(form.getId());
            modelRespVO.setFormName(form.getName());
        }
        // ProcessDefinition
        modelRespVO.setProcessDefinition(this.convert(processDefinition));
        if (modelRespVO.getProcessDefinition() != null) {
            modelRespVO
                    .getProcessDefinition()
                    .setSuspensionState(
                            processDefinition.isSuspended()
                                    ? SuspensionState.SUSPENDED.getStateCode()
                                    : SuspensionState.ACTIVE.getStateCode());
            modelRespVO.getProcessDefinition().setDeploymentTime(DateUtils.of(deployment.getDeploymentTime()));
        }
        return modelRespVO;
    }

    default BpmModelRespVO convert(Model model) {
        BpmModelRespVO modelRespVO = new BpmModelRespVO();
        modelRespVO.setId(model.getId());
        modelRespVO.setCreateTime(DateUtils.of(model.getCreateTime()));
        // 通用 copy
        copyTo(model, modelRespVO);
        return modelRespVO;
    }

    default void copyTo(Model model, BpmModelBaseVO to) {
        to.setName(model.getName());
        to.setKey(model.getKey());
        to.setCategory(model.getCategory());
        // metaInfo
        BpmModelMetaInfoRespDTO metaInfo = JsonUtils.parseObject(model.getMetaInfo(), BpmModelMetaInfoRespDTO.class);
        copyTo(metaInfo, to);
    }

    BpmModelCreateReqVO convert(BpmModeImportReqVO bean);

    default BpmProcessDefinitionCreateReqDTO convert2(Model model, BpmFormDO form) {
        BpmProcessDefinitionCreateReqDTO createReqDTO = new BpmProcessDefinitionCreateReqDTO();
        createReqDTO.setModelId(model.getId());
        createReqDTO.setName(model.getName());
        createReqDTO.setKey(model.getKey());
        createReqDTO.setCategory(model.getCategory());
        BpmModelMetaInfoRespDTO metaInfo = JsonUtils.parseObject(model.getMetaInfo(), BpmModelMetaInfoRespDTO.class);
        // metaInfo
        copyTo(metaInfo, createReqDTO);
        // form
        if (form != null) {
            createReqDTO.setFormConf(form.getConf());
            createReqDTO.setFormFields(form.getFields());
        }
        return createReqDTO;
    }

    void copyTo(BpmModelMetaInfoRespDTO from, @MappingTarget BpmProcessDefinitionCreateReqDTO to);

    void copyTo(BpmModelMetaInfoRespDTO from, @MappingTarget BpmModelBaseVO to);

    BpmModelPageItemRespVO.ProcessDefinition convert(ProcessDefinition bean);

    default void copy(Model model, BpmModelCreateReqVO bean) {
        model.setName(bean.getName());
        model.setKey(bean.getKey());
        model.setMetaInfo(buildMetaInfoStr(null, bean.getDescription(), null, null, null, null));
    }

    default void copy(Model model, BpmModelUpdateReqVO bean) {
        model.setName(bean.getName());
        model.setCategory(bean.getCategory());
        model.setMetaInfo(buildMetaInfoStr(
                JsonUtils.parseObject(model.getMetaInfo(), BpmModelMetaInfoRespDTO.class),
                bean.getDescription(),
                bean.getFormType(),
                bean.getFormId(),
                bean.getFormCustomCreatePath(),
                bean.getFormCustomViewPath()));
    }

    default String buildMetaInfoStr(
            BpmModelMetaInfoRespDTO metaInfo,
            String description,
            Integer formType,
            Long formId,
            String formCustomCreatePath,
            String formCustomViewPath) {
        if (metaInfo == null) {
            metaInfo = new BpmModelMetaInfoRespDTO();
        }
        // 只有非空，才进行设置，避免更新时的覆盖
        if (StrUtil.isNotEmpty(description)) {
            metaInfo.setDescription(description);
        }
        if (Objects.nonNull(formType)) {
            metaInfo.setFormType(formType);
            metaInfo.setFormId(formId);
            metaInfo.setFormCustomCreatePath(formCustomCreatePath);
            metaInfo.setFormCustomViewPath(formCustomViewPath);
        }
        return JsonUtils.toJsonString(metaInfo);
    }
}
