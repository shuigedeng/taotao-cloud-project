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

package com.taotao.cloud.workflow.biz.engine.service.impl;

import org.dromara.hutoolcore.util.ObjectUtil;
import org.dromara.hutoolcore.util.StrUtil;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.common.utils.common.CodeUtil;
import com.taotao.boot.common.utils.common.IdGeneratorUtils;
import com.taotao.boot.common.utils.common.JsonUtils;
import com.taotao.boot.common.utils.common.RandomUtils;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.cloud.workflow.api.vo.entity.DictionaryDataEntity;
import com.taotao.cloud.workflow.biz.common.constant.MsgCode;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.FlowEngineListVO;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.FlowExportModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.FlowPagination;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.PaginationFlowEngine;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.childnode.ChildNode;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.childnode.Properties;
import com.taotao.cloud.workflow.biz.common.util.UserProvider;
import com.taotao.cloud.workflow.biz.common.util.context.SpringContext;
import com.taotao.cloud.workflow.biz.covert.FlowTaskConvert;
import com.taotao.cloud.workflow.biz.engine.entity.FlowEngineEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowEngineVisibleEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskEntity;
import com.taotao.cloud.workflow.biz.engine.mapper.FlowEngineMapper;
import com.taotao.cloud.workflow.biz.engine.service.FlowEngineService;
import com.taotao.cloud.workflow.biz.engine.service.FlowEngineVisibleService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskService;
import com.taotao.cloud.workflow.biz.engine.util.ServiceAllUtil;
import com.taotao.cloud.workflow.biz.exception.WorkFlowException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 流程引擎 */
@Service
public class FlowEngineServiceImpl extends ServiceImpl<FlowEngineMapper, FlowEngineEntity>
        implements FlowEngineService {

    @Autowired
    private FlowTaskService flowTaskService;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private ServiceAllUtil serviceUtil;

    @Autowired
    private FlowEngineVisibleService flowEngineVisibleService;

    @Override
    public IPage<FlowEngineEntity> getPageList(FlowPagination pagination) {
        // 定义变量判断是否需要使用修改时间倒序
        boolean flag = false;
        PaginationFlowEngine engine = new PaginationFlowEngine();
        engine.setFormType(2);
        engine.setType(1);
        List<FlowEngineEntity> engineEntities = getList(engine);
        List<String> id = engineEntities.stream().map(FlowEngineEntity::getId).toList();
        QueryWrapper<FlowEngineEntity> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotEmpty(pagination.getKeyword())) {
            flag = true;
            queryWrapper.lambda().like(FlowEngineEntity::getFullName, pagination.getKeyword());
        }
        if (id.size() > 0) {
            queryWrapper.lambda().notIn(FlowEngineEntity::getId, id);
        }
        if (StrUtil.isNotEmpty(pagination.getCategory())) {
            flag = true;
            queryWrapper.lambda().eq(FlowEngineEntity::getCategory, pagination.getCategory());
        }
        // 排序
        queryWrapper.lambda().orderByAsc(FlowEngineEntity::getSortCode).orderByDesc(FlowEngineEntity::getCreatorTime);
        if (flag) {
            queryWrapper.lambda().orderByDesc(FlowEngineEntity::getLastModifyTime);
        }
        Page<FlowEngineEntity> page = new Page<>(pagination.getCurrentPage(), pagination.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<FlowEngineEntity> getList(PaginationFlowEngine pagination) {
        // 定义变量判断是否需要使用修改时间倒序
        boolean flag = false;
        QueryWrapper<FlowEngineEntity> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotEmpty(pagination.getType())) {
            flag = true;
            queryWrapper.lambda().eq(FlowEngineEntity::getType, pagination.getType());
        }
        if (StrUtil.isNotEmpty(pagination.getKeyword())) {
            flag = true;
            queryWrapper.lambda().like(FlowEngineEntity::getFullName, pagination.getKeyword());
        }
        if (ObjectUtil.isNotEmpty(pagination.getFormType())) {
            flag = true;
            queryWrapper.lambda().eq(FlowEngineEntity::getFormType, pagination.getFormType());
        }
        if (ObjectUtil.isNotEmpty(pagination.getEnabledMark())) {
            flag = true;
            queryWrapper.lambda().eq(FlowEngineEntity::getEnabledMark, pagination.getEnabledMark());
        }
        // 排序
        queryWrapper.lambda().orderByAsc(FlowEngineEntity::getSortCode).orderByDesc(FlowEngineEntity::getCreatorTime);
        if (flag) {
            queryWrapper.lambda().orderByDesc(FlowEngineEntity::getLastModifyTime);
        }
        queryWrapper
                .lambda()
                .select(
                        FlowEngineEntity::getId,
                        FlowEngineEntity::getEnCode,
                        FlowEngineEntity::getFullName,
                        FlowEngineEntity::getFormType,
                        FlowEngineEntity::getType,
                        FlowEngineEntity::getIcon,
                        FlowEngineEntity::getCategory,
                        FlowEngineEntity::getIconBackground,
                        FlowEngineEntity::getVisibleType,
                        FlowEngineEntity::getCreatorUser,
                        FlowEngineEntity::getSortCode,
                        FlowEngineEntity::getEnabledMark,
                        FlowEngineEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

    @Override
    public List<FlowEngineEntity> getList() {
        QueryWrapper<FlowEngineEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByAsc(FlowEngineEntity::getSortCode).orderByDesc(FlowEngineEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

    @Override
    public List<FlowEngineEntity> getListAll(FlowPagination pagination, boolean isPage) {
        // 定义变量判断是否需要使用修改时间倒序
        boolean flag = false;
        UserInfo userInfo = userProvider.get();
        List<String> id = flowEngineVisibleService.getVisibleFlowList(userInfo.getUserId()).stream()
                .map(FlowEngineVisibleEntity::getFlowId)
                .toList();
        List<FlowEngineEntity> flowFormTypeList = getFlowFormTypeList();
        List<String> formTypeId =
                flowFormTypeList.stream().map(FlowEngineEntity::getId).toList();
        id.addAll(formTypeId);
        QueryWrapper<FlowEngineEntity> queryWrapper = new QueryWrapper<>();
        if (id.size() > 0) {
            queryWrapper.lambda().in(FlowEngineEntity::getId, id);
        }
        if (StrUtil.isNotEmpty(pagination.getKeyword())) {
            flag = true;
            queryWrapper.lambda().like(FlowEngineEntity::getFullName, pagination.getKeyword());
        }
        if (StrUtil.isNotEmpty(pagination.getCategory())) {
            flag = true;
            queryWrapper.lambda().eq(FlowEngineEntity::getCategory, pagination.getCategory());
        }
        queryWrapper.lambda().orderByAsc(FlowEngineEntity::getSortCode).orderByDesc(FlowEngineEntity::getCreatorTime);
        if (flag) {
            queryWrapper.lambda().orderByDesc(FlowEngineEntity::getLastModifyTime);
        }
        queryWrapper
                .lambda()
                .select(
                        FlowEngineEntity::getId,
                        FlowEngineEntity::getEnCode,
                        FlowEngineEntity::getFullName,
                        FlowEngineEntity::getFormType,
                        FlowEngineEntity::getType,
                        FlowEngineEntity::getIcon,
                        FlowEngineEntity::getCategory,
                        FlowEngineEntity::getIconBackground,
                        FlowEngineEntity::getVisibleType,
                        FlowEngineEntity::getCreatorUser,
                        FlowEngineEntity::getSortCode,
                        FlowEngineEntity::getEnabledMark,
                        FlowEngineEntity::getCreatorTime);
        if (isPage) {
            Page<FlowEngineEntity> page = new Page<>(pagination.getCurrentPage(), pagination.getPageSize());
            IPage<FlowEngineEntity> userPage = this.page(page, queryWrapper);
            return pagination.setData(userPage.getRecords(), page.getTotal());
        } else {
            return this.list(queryWrapper);
        }
    }

    @Override
    public List<FlowEngineEntity> getFlowFormList() {
        return getListAll(new FlowPagination(), false);
    }

    @Override
    public List<FlowEngineEntity> getFlowFormTypeList() {
        List<Integer> visibleType = new ArrayList<>();
        visibleType.add(0);
        QueryWrapper<FlowEngineEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowEngineEntity::getEnabledMark, 1);
        queryWrapper.lambda().in(FlowEngineEntity::getVisibleType, visibleType);
        queryWrapper.lambda().eq(FlowEngineEntity::getType, 0);
        queryWrapper.lambda().select(FlowEngineEntity::getId);
        return this.list(queryWrapper);
    }

    @Override
    public List<FlowEngineEntity> getFlowList(List<String> id) {
        List<FlowEngineEntity> list = new ArrayList<>();
        if (id.size() > 0) {
            QueryWrapper<FlowEngineEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(FlowEngineEntity::getId, id);
            list = this.list(queryWrapper);
        }
        return list;
    }

    @Override
    public FlowEngineEntity getInfo(String id) throws WorkFlowException {
        QueryWrapper<FlowEngineEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowEngineEntity::getId, id);
        FlowEngineEntity flowEngineEntity = this.getOne(queryWrapper);
        if (flowEngineEntity == null) {
            throw new WorkFlowException(MsgCode.WF113.get());
        }
        return flowEngineEntity;
    }

    @Override
    public FlowEngineEntity getInfoByEnCode(String enCode) throws WorkFlowException {
        QueryWrapper<FlowEngineEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowEngineEntity::getEnCode, enCode).eq(FlowEngineEntity::getEnabledMark, 1);
        FlowEngineEntity flowEngineEntity = this.getOne(queryWrapper);
        if (flowEngineEntity == null) {
            throw new WorkFlowException(MsgCode.WF113.get());
        }
        return flowEngineEntity;
    }

    @Override
    public boolean isExistByFullName(String fullName, String id) {
        QueryWrapper<FlowEngineEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowEngineEntity::getFullName, fullName);
        queryWrapper.lambda().eq(FlowEngineEntity::getType, 0);
        if (!StringUtils.isEmpty(id)) {
            queryWrapper.lambda().ne(FlowEngineEntity::getId, id);
        }
        return this.count(queryWrapper) > 0;
    }

    @Override
    public boolean isExistByEnCode(String enCode, String id) {
        QueryWrapper<FlowEngineEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowEngineEntity::getEnCode, enCode);
        if (!StringUtils.isEmpty(id)) {
            queryWrapper.lambda().ne(FlowEngineEntity::getId, id);
        }
        return this.count(queryWrapper) > 0;
    }

    @Override
    public void delete(FlowEngineEntity entity) throws WorkFlowException {
        List<FlowTaskEntity> taskNodeList = flowTaskService.getTaskList(entity.getId());
        if (taskNodeList.size() > 0) {
            throw new WorkFlowException("引擎在使用，不可删除");
        }
        this.removeById(entity.getId());
        QueryWrapper<FlowEngineVisibleEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowEngineVisibleEntity::getFlowId, entity.getId());
        flowEngineVisibleService.remove(queryWrapper);
        Object service = SpringContext.getBean("appDataServiceImpl");
        Class[] types = new Class[] {String.class};
        Object[] datas = new Object[] {entity.getId()};
        ReflectionUtil.invokeMethod(service, "delete", types, datas);
    }

    @Override
    public Boolean create(FlowEngineEntity entity) {
        List<FlowEngineVisibleEntity> visibleList = visibleList(entity);
        if (entity.getId() == null) {
            entity.setId(CodeUtil.getNextYouBianCode(entity.getCategory()));
        }

        entity.setVersion(StrUtil.isEmpty(entity.getVersion()) ? "1" : entity.getVersion());
        entity.setCreatorUser(SecurityUtils.getUserId());
        entity.setVisibleType(visibleList.size() == 0 ? 0 : 1);
        this.save(entity);

        for (FlowEngineVisibleEntity flowEngineVisibleEntity : visibleList) {
            flowEngineVisibleEntity.setId(IdGeneratorUtils.getIdStr());
            flowEngineVisibleEntity.setFlowId(entity.getId());
            flowEngineVisibleEntity.setSortCode(RandomUtils.randomLong(100));
            flowEngineVisibleService.save(flowEngineVisibleEntity);
        }
        return true;
    }

    @Override
    @DSTransactional
    public void copy(FlowEngineEntity entity) throws WorkFlowException {
        try {
            entity.setVersion("1");
            this.create(entity);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new WorkFlowException(MsgCode.WF103.get());
        }
    }

    @Override
    public boolean updateVisible(String id, FlowEngineEntity entity) throws WorkFlowException {
        List<FlowEngineVisibleEntity> visibleList = visibleList(entity);
        entity.setId(id);
        entity.setLastModifyTime(new Date());
        entity.setLastModifyUser(SecurityUtils.getUserId());
        entity.setVisibleType(visibleList.size() == 0 ? 0 : 1);
        String num = "1";
        FlowEngineEntity info = getInfo(id);
        BigDecimal b1 = new BigDecimal(StrUtil.isEmpty(info.getVersion()) ? "0" : info.getVersion());
        BigDecimal b2 = new BigDecimal(num);
        entity.setVersion(String.valueOf(b1.add(b2)));
        boolean flag = this.updateById(entity);
        if (flag) {
            QueryWrapper<FlowEngineVisibleEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(FlowEngineVisibleEntity::getFlowId, entity.getId());
            flowEngineVisibleService.remove(queryWrapper);
            for (int i = 0; i < visibleList.size(); i++) {
                visibleList.get(i).setId(IdGeneratorUtils.getIdStr());
                visibleList.get(i).setFlowId(entity.getId());
                visibleList.get(i).setSortCode(Long.parseLong(i + ""));
                flowEngineVisibleService.save(visibleList.get(i));
            }
        }
        return flag;
    }

    @Override
    public void update(String id, FlowEngineEntity entity) throws WorkFlowException {
        String num = "1";
        FlowEngineEntity info = getInfo(id);
        BigDecimal b1 = new BigDecimal(StrUtil.isEmpty(info.getVersion()) ? "0" : info.getVersion());
        BigDecimal b2 = new BigDecimal(num);
        entity.setVersion(String.valueOf(b1.add(b2)));
        entity.setId(id);
        entity.setLastModifyTime(new Date());
        entity.setLastModifyUser(SecurityUtils.getUserId());
        this.updateById(entity);
    }

    @Override
    public boolean first(String id) {
        boolean isOk = false;
        // 获取要上移的那条数据的信息
        FlowEngineEntity upEntity = this.getById(id);
        Long upSortCode = upEntity.getSortCode() == null ? 0 : upEntity.getSortCode();
        // 查询上几条记录
        QueryWrapper<FlowEngineEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .lambda()
                .lt(FlowEngineEntity::getSortCode, upSortCode)
                .eq(FlowEngineEntity::getCategory, upEntity.getCategory())
                .orderByDesc(FlowEngineEntity::getSortCode);
        List<FlowEngineEntity> downEntity = this.list(queryWrapper);
        if (downEntity.size() > 0) {
            // 交换两条记录的sort值
            Long temp = upEntity.getSortCode();
            upEntity.setSortCode(downEntity.get(0).getSortCode());
            downEntity.get(0).setSortCode(temp);
            updateById(downEntity.get(0));
            updateById(upEntity);
            isOk = true;
        }
        return isOk;
    }

    @Override
    public boolean next(String id) {
        boolean isOk = false;
        // 获取要下移的那条数据的信息
        FlowEngineEntity downEntity = this.getById(id);
        Long upSortCode = downEntity.getSortCode() == null ? 0 : downEntity.getSortCode();
        // 查询下几条记录
        QueryWrapper<FlowEngineEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .lambda()
                .gt(FlowEngineEntity::getSortCode, upSortCode)
                .eq(FlowEngineEntity::getCategory, downEntity.getCategory())
                .orderByAsc(FlowEngineEntity::getSortCode);
        List<FlowEngineEntity> upEntity = this.list(queryWrapper);
        if (upEntity.size() > 0) {
            // 交换两条记录的sort值
            Long temp = downEntity.getSortCode();
            downEntity.setSortCode(upEntity.get(0).getSortCode());
            upEntity.get(0).setSortCode(temp);
            updateById(upEntity.get(0));
            updateById(downEntity);
            isOk = true;
        }
        return isOk;
    }

    @Override
    public List<FlowEngineListVO> getTreeList(PaginationFlowEngine pagination, boolean isList) {
        List<FlowEngineEntity> data;
        if (isList) {
            data = getList(pagination);
        } else {
            data = getFlowFormList();
        }
        List<DictionaryDataEntity> dictionList = serviceUtil.getDiList();
        Map<String, List<FlowEngineEntity>> dataList =
                data.stream().collect(Collectors.groupingBy(FlowEngineEntity::getCategory));

        List<FlowEngineListVO> listVOS = new LinkedList<>();
        for (DictionaryDataEntity entity : dictionList) {
            FlowEngineListVO model = new FlowEngineListVO();
            model.setFullName(entity.getFullName());
            model.setId(entity.getId());
            List<FlowEngineEntity> childList =
                    dataList.get(entity.getEnCode()) != null ? dataList.get(entity.getEnCode()) : new ArrayList<>();
            model.setNum(childList.size());
            if (childList.size() > 0) {
                model.setChildren(FlowTaskConvert.INSTANCE.convertComment(childList));
            }
            listVOS.add(model);
        }
        return listVOS;
    }

    private List<FlowEngineVisibleEntity> visibleList(FlowEngineEntity entity) {
        List<FlowEngineVisibleEntity> visibleList = new ArrayList<>();
        if (entity.getFlowTemplateJson() != null) {
            ChildNode childNode = JsonUtils.toObject(entity.getFlowTemplateJson(), ChildNode.class);
            Properties properties = childNode.getProperties();
            // 可见的用户
            for (String user : properties.getInitiator()) {
                FlowEngineVisibleEntity visible = new FlowEngineVisibleEntity();
                visible.setOperatorId(user);
                visible.setOperatorType("user");
                visibleList.add(visible);
            }
            // 可见的部门
            for (String position : properties.getInitiatePos()) {
                FlowEngineVisibleEntity visible = new FlowEngineVisibleEntity();
                visible.setOperatorId(position);
                visible.setOperatorType("position");
                visibleList.add(visible);
            }
            // 可见的角色
            List<String> roleList =
                    properties.getInitiateRole() != null ? properties.getInitiateRole() : new ArrayList<>();
            for (String role : roleList) {
                FlowEngineVisibleEntity visible = new FlowEngineVisibleEntity();
                visible.setOperatorId(role);
                visible.setOperatorType("role");
                visibleList.add(visible);
            }
        }
        return visibleList;
    }

    @Override
    public FlowExportModel exportData(String id) throws WorkFlowException {
        FlowEngineEntity entity = getInfo(id);
        List<FlowEngineVisibleEntity> visibleList = flowEngineVisibleService.getVisibleFlowList(entity.getId());
        FlowExportModel model = new FlowExportModel();
        model.setFlowEngine(entity);
        model.setVisibleList(visibleList);
        return model;
    }

    @Override
    @DSTransactional
    public Result ImportData(FlowEngineEntity entity, List<FlowEngineVisibleEntity> visibleList)
            throws WorkFlowException {
        if (entity != null) {
            if (isExistByFullName(entity.getFullName(), null)) {
                return Result.fail("流程名称不能重复");
            }
            if (isExistByEnCode(entity.getEnCode(), null)) {
                return Result.fail("流程编码不能重复");
            }
            try {
                this.save(entity);
                if (visibleList != null) {
                    for (int i = 0; i < visibleList.size(); i++) {
                        flowEngineVisibleService.save(visibleList.get(i));
                    }
                }
            } catch (Exception e) {
                throw new WorkFlowException(MsgCode.IMP003.get());
            }
            return Result.success(MsgCode.IMP001.get());
        }
        return Result.fail("导入数据格式不正确");
    }
}
