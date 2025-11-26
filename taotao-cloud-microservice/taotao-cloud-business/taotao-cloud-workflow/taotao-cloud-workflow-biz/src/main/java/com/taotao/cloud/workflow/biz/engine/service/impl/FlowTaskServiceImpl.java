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


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.boot.common.utils.common.JsonUtils;
import com.taotao.boot.data.mybatis.mybatisplus.utils.MpUtils;
import com.taotao.cloud.workflow.api.vo.UserRelationEntity;
import com.taotao.cloud.workflow.biz.common.constant.MsgCode;
import com.taotao.cloud.workflow.biz.common.database.util.DataSourceUtil;
import com.taotao.cloud.workflow.biz.common.database.util.DbTypeUtil;
import com.taotao.cloud.workflow.biz.common.model.engine.flowbefore.FlowBatchModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowtask.FlowTaskListModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowtask.PaginationFlowTask;
import com.taotao.cloud.workflow.biz.common.util.DateUtil;
import com.taotao.cloud.workflow.biz.engine.entity.FlowDelegateEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskCirculateEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskNodeEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorRecordEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowTaskStatusEnum;
import com.taotao.cloud.workflow.biz.engine.mapper.FlowTaskMapper;
import com.taotao.cloud.workflow.biz.engine.service.FlowDelegateService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskCirculateService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskNodeService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorRecordService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskService;
import com.taotao.cloud.workflow.biz.engine.util.FlowNature;
import com.taotao.cloud.workflow.biz.engine.util.ServiceAllUtil;
import com.taotao.cloud.workflow.biz.exception.WorkFlowException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import com.taotao.boot.common.utils.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 流程任务 */
@Slf4j
@Service
public class FlowTaskServiceImpl extends ServiceImpl<FlowTaskMapper, FlowTaskEntity> implements FlowTaskService {

    @Autowired
    private ServiceAllUtil serviceUtil;

    @Autowired
    private FlowDelegateService flowDelegateService;

    @Autowired
    private FlowTaskNodeService flowTaskNodeService;

    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    @Autowired
    private FlowTaskOperatorRecordService flowTaskOperatorRecordService;

    @Autowired
    private FlowTaskCirculateService flowTaskCirculateService;

    @Autowired
    private DataSourceUtil dataSourceUtil;

    @Override
    public List<FlowTaskEntity> getMonitorList(PaginationFlowTask paginationFlowTask) {
        // 定义变量判断是否需要使用修改时间倒序
        boolean flag = false;
        QueryWrapper<FlowTaskEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().gt(FlowTaskEntity::getStatus, FlowTaskStatusEnum.Draft.getCode());
        // 关键字（流程名称、流程编码）
        String keyWord = paginationFlowTask.getKeyword() != null ? paginationFlowTask.getKeyword() : null;
        if (!StringUtils.isEmpty(keyWord)) {
            flag = true;
            queryWrapper.lambda().and(t -> t.like(FlowTaskEntity::getEnCode, keyWord)
                    .or()
                    .like(FlowTaskEntity::getFullName, keyWord));
        }
        // 日期范围（近7天、近1月、近3月、自定义）
        String startTime = paginationFlowTask.getStartTime() != null ? paginationFlowTask.getStartTime() : null;
        String endTime = paginationFlowTask.getEndTime() != null ? paginationFlowTask.getEndTime() : null;
        if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {
            flag = true;
            Date startTimes = DateUtil.stringToDate(DateUtil.daFormatYmd(Long.parseLong(startTime)) + " 00:00:00");
            Date endTimes = DateUtil.stringToDate(DateUtil.daFormatYmd(Long.parseLong(endTime)) + " 23:59:59");
            queryWrapper
                    .lambda()
                    .ge(FlowTaskEntity::getCreatorTime, startTimes)
                    .le(FlowTaskEntity::getCreatorTime, endTimes);
        }
        // 流程状态
        if (ObjectUtil.isNotNull(paginationFlowTask.getStatus())) {
            flag = true;
            queryWrapper.lambda().eq(FlowTaskEntity::getStatus, paginationFlowTask.getStatus());
        }
        // 所属流程
        String flowId = paginationFlowTask.getFlowId() != null ? paginationFlowTask.getFlowId() : null;
        if (!StringUtils.isEmpty(flowId)) {
            flag = true;
            queryWrapper.lambda().eq(FlowTaskEntity::getFlowId, flowId);
        }
        // 所属分类
        String flowCategory =
                paginationFlowTask.getFlowCategory() != null ? paginationFlowTask.getFlowCategory() : null;
        if (!StringUtils.isEmpty(flowCategory)) {
            flag = true;
            queryWrapper.lambda().eq(FlowTaskEntity::getFlowCategory, flowCategory);
        }
        // 发起人员
        String creatorUserId =
                paginationFlowTask.getCreatorUserId() != null ? paginationFlowTask.getCreatorUserId() : null;
        if (!StringUtils.isEmpty(creatorUserId)) {
            flag = true;
            queryWrapper.lambda().eq(FlowTaskEntity::getCreatorUserId, creatorUserId);
        }
        // 排序
        //        if ("desc".equals(paginationFlowTask.getSort().toLowerCase())) {
        //            queryWrapper.lambda().orderByDesc(FlowTaskEntity::getCreatorTime);
        //        } else {
        queryWrapper.lambda().orderByAsc(FlowTaskEntity::getSortCode).orderByDesc(FlowTaskEntity::getCreatorTime);
        //        }
        if (flag) {
            queryWrapper.lambda().orderByDesc(FlowTaskEntity::getLastModifyTime);
        }
        Page<FlowTaskEntity> page = new Page<>(paginationFlowTask.getCurrentPage(), paginationFlowTask.getPageSize());
        IPage<FlowTaskEntity> flowTaskEntityPage = this.page(page, queryWrapper);
        return paginationFlowTask.setData(flowTaskEntityPage.getRecords(), page.getTotal());
    }

    @Override
    public List<FlowTaskEntity> getLaunchList(PaginationFlowTask paginationFlowTask) {
        QueryWrapper<FlowTaskEntity> queryWrapper = new QueryWrapper<>();
        String userId = userProvider.get().getUserId();
        queryWrapper.lambda().select(FlowTaskEntity::getId).eq(FlowTaskEntity::getCreatorUserId, userId);
        // 关键字（流程名称、流程编码）
        String keyWord = paginationFlowTask.getKeyword() != null ? paginationFlowTask.getKeyword() : null;
        if (!StringUtils.isEmpty(keyWord)) {
            queryWrapper.lambda().and(t -> t.like(FlowTaskEntity::getEnCode, keyWord)
                    .or()
                    .like(FlowTaskEntity::getFullName, keyWord));
        }
        // 日期范围（近7天、近1月、近3月、自定义）
        String startTime = paginationFlowTask.getStartTime() != null ? paginationFlowTask.getStartTime() : null;
        String endTime = paginationFlowTask.getEndTime() != null ? paginationFlowTask.getEndTime() : null;
        if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {
            Date startTimes = DateUtil.stringToDate(DateUtil.daFormatYmd(Long.parseLong(startTime)) + " 00:00:00");
            Date endTimes = DateUtil.stringToDate(DateUtil.daFormatYmd(Long.parseLong(endTime)) + " 23:59:59");
            queryWrapper
                    .lambda()
                    .ge(FlowTaskEntity::getCreatorTime, startTimes)
                    .le(FlowTaskEntity::getCreatorTime, endTimes);
        }
        // 所属流程
        String flowName = paginationFlowTask.getFlowId() != null ? paginationFlowTask.getFlowId() : null;
        if (!StringUtils.isEmpty(flowName)) {
            queryWrapper.lambda().eq(FlowTaskEntity::getFlowId, flowName);
        }
        // 流程状态
        if (ObjectUtil.isNotNull(paginationFlowTask.getStatus())) {
            queryWrapper.lambda().eq(FlowTaskEntity::getStatus, paginationFlowTask.getStatus());
        }
        // 所属分类
        String flowCategory =
                paginationFlowTask.getFlowCategory() != null ? paginationFlowTask.getFlowCategory() : null;
        if (!StringUtils.isEmpty(flowCategory)) {
            queryWrapper.lambda().eq(FlowTaskEntity::getFlowCategory, flowCategory);
        }
        // 排序
        if ("asc".equals(paginationFlowTask.getSort().toLowerCase())) {
            queryWrapper.lambda().orderByAsc(FlowTaskEntity::getStatus).orderByAsc(FlowTaskEntity::getStartTime);
        } else {
            queryWrapper.lambda().orderByAsc(FlowTaskEntity::getStatus).orderByDesc(FlowTaskEntity::getStartTime);
        }
        Page<FlowTaskEntity> page = new Page<>(paginationFlowTask.getCurrentPage(), paginationFlowTask.getPageSize());
        IPage<FlowTaskEntity> flowTaskEntityPage = this.page(page, queryWrapper);
        if (!flowTaskEntityPage.getRecords().isEmpty()) {
            List<String> ids =
                    flowTaskEntityPage.getRecords().stream().map(m -> m.getId()).toList();
            queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(FlowTaskEntity::getId, ids);
            // 排序
            if ("asc".equals(paginationFlowTask.getSort().toLowerCase())) {
                queryWrapper.lambda().orderByAsc(FlowTaskEntity::getStatus).orderByAsc(FlowTaskEntity::getStartTime);
            } else {
                queryWrapper.lambda().orderByAsc(FlowTaskEntity::getStatus).orderByDesc(FlowTaskEntity::getStartTime);
            }
            flowTaskEntityPage.setRecords(this.list(queryWrapper));
        }
        return paginationFlowTask.setData(flowTaskEntityPage.getRecords(), page.getTotal());
    }

    @Override
    public IPage<FlowTaskListModel> getWaitList(PaginationFlowTask paginationFlowTask) {
        List<FlowTaskListModel> result = getWaitListAll(paginationFlowTask);
        return MpUtils.toPage(paginationFlowTask, result);
    }

    @Override
    public IPage<FlowTaskListModel> getBatchWaitList(PaginationFlowTask paginationFlowTask) {
        paginationFlowTask.setIsBatch(1);
        List<FlowTaskListModel> result = getWaitListAll(paginationFlowTask);
        return MpUtils.toPage(paginationFlowTask, result);
    }

    @Override
    public List<FlowTaskListModel> getWaitListAll(PaginationFlowTask paginationFlowTask) {
        String userId = userProvider.get().getUserId();
        StringBuilder dbSql = new StringBuilder();
        // 查询自己的待办
        dbSql.append(" AND (");
        // 委托审核
        StringJoiner joiner = new StringJoiner(",");
        joiner.add("'" + userId + "'");
        // 是否批量
        List<FlowDelegateEntity> flowDelegateList = flowDelegateService.getUser(userId);
        for (FlowDelegateEntity delegateEntity : flowDelegateList) {
            joiner.add("'" + delegateEntity.getCreatorUserId() + "'");
        }
        dbSql.append("o.F_HandleId in (" + joiner.toString() + " ) )");
        // 关键字（流程名称、流程编码）
        String keyWord = paginationFlowTask.getKeyword() != null ? paginationFlowTask.getKeyword() : null;
        if (!StringUtils.isEmpty(keyWord)) {
            dbSql.append(" AND (t.F_EnCode like '%" + keyWord + "%' or t.F_FullName like '%" + keyWord + "%') ");
        }

        // 日期范围（近7天、近1月、近3月、自定义）
        String startTime = paginationFlowTask.getStartTime() != null ? paginationFlowTask.getStartTime() : null;
        String endTime = paginationFlowTask.getEndTime() != null ? paginationFlowTask.getEndTime() : null;
        if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {
            if (DbTypeUtil.checkOracle(dataSourceUtil)) {
                String startTimes = DateUtil.daFormatYmd(Long.parseLong(startTime)) + " 00:00:00";
                String endTimes = DateUtil.daFormatYmd(Long.parseLong(endTime)) + " 23:59:59";
                dbSql.append(" AND o.F_CreatorTime Between TO_DATE('"
                        + startTimes
                        + "','yyyy-mm-dd HH24:mi:ss') AND TO_DATE('"
                        + endTimes
                        + "','yyyy-mm-dd HH24:mi:ss') ");
            } else {
                String startTimes = DateUtil.daFormatYmd(Long.parseLong(startTime)) + " 00:00:00";
                String endTimes = DateUtil.daFormatYmd(Long.parseLong(endTime)) + " 23:59:59";
                dbSql.append(" AND o.F_CreatorTime Between '" + startTimes + "' AND '" + endTimes + "' ");
            }
        }
        // 所属流程
        String flowId = paginationFlowTask.getFlowId() != null ? paginationFlowTask.getFlowId() : null;
        if (!StringUtils.isEmpty(flowId)) {
            dbSql.append(" AND t.F_FlowId = '" + flowId + "'");
        }
        // 所属分类
        String flowCategory =
                paginationFlowTask.getFlowCategory() != null ? paginationFlowTask.getFlowCategory() : null;
        if (!StringUtils.isEmpty(flowCategory)) {
            dbSql.append(" AND t.F_FlowCategory = '" + flowCategory + "'");
        }
        // 发起人员
        String creatorUserId =
                paginationFlowTask.getCreatorUserId() != null ? paginationFlowTask.getCreatorUserId() : null;
        if (!StringUtils.isEmpty(creatorUserId)) {
            dbSql.append(" AND t.F_CreatorUserId = '" + creatorUserId + "'");
        }
        // 节点编码
        String nodeCode = paginationFlowTask.getNodeCode() != null ? paginationFlowTask.getNodeCode() : null;
        if (!StringUtils.isEmpty(nodeCode)) {
            dbSql.append(" AND o.F_NodeCode = '" + nodeCode + "'");
        }
        Integer isBatch = paginationFlowTask.getIsBatch() != null ? paginationFlowTask.getIsBatch() : null;
        if (!ObjectUtil.isEmpty(isBatch)) {
            dbSql.append(" AND t.F_IsBatch = " + isBatch + " ");
        }
        // 排序
        StringBuilder orderBy = new StringBuilder();
        if ("desc".equals(paginationFlowTask.getSort().toLowerCase())) {
            orderBy.append(" Order by F_CreatorTime DESC");
        } else {
            orderBy.append(" Order by F_CreatorTime ASC");
        }
        String sql = dbSql.toString() + " " + orderBy.toString();
        List<FlowTaskListModel> data = this.baseMapper.getWaitList(sql);
        List<FlowTaskListModel> result = new LinkedList<>();
        for (FlowTaskListModel entity : data) {
            List<Date> list = StringUtils.isNotEmpty(entity.getDescription())
                    ? JsonUtils.getJsonToList(entity.getDescription(), Date.class)
                    : new ArrayList<>();
            boolean delegate = true;
            boolean isuser = entity.getHandleId().equals(userId);
            entity.setFullName(!isuser ? entity.getFullName() + "(委托)" : entity.getFullName());
            List<FlowDelegateEntity> flowList = flowDelegateList.stream()
                    .filter(t -> t.getFlowId().equals(entity.getFlowId()))
                    .toList();
            // 判断是否有自己审核
            if (!isuser) {
                // 是否委托当前流程引擎 true是 flas否
                delegate = flowList.stream().anyMatch(t -> t.getCreatorUserId().equals(entity.getHandleId()));
            }
            if (delegate) {
                result.add(entity);
                Date date = new Date();
                boolean del =
                        list.stream().filter(t -> t.getTime() > date.getTime()).count() > 0;
                if (del) {
                    result.remove(entity);
                }
            }
        }
        return result;
    }

    @Override
    public IPage<FlowTaskListModel> getTrialList(PaginationFlowTask paginationFlowTask) {
        String userId = userProvider.get().getUserId();
        Map<String, Object> queryParam = new HashMap<>(16);
        StringBuilder dbSql = new StringBuilder();
        // 关键字（流程名称、流程编码）
        String keyWord = paginationFlowTask.getKeyword() != null ? paginationFlowTask.getKeyword() : null;
        if (!StringUtils.isEmpty(keyWord)) {
            dbSql.append(" AND (t.F_EnCode like '%" + keyWord + "%' or t.F_FullName like '%" + keyWord + "%') ");
        }
        // 日期范围（近7天、近1月、近3月、自定义）
        String startTime = paginationFlowTask.getStartTime() != null ? paginationFlowTask.getStartTime() : null;
        String endTime = paginationFlowTask.getEndTime() != null ? paginationFlowTask.getEndTime() : null;
        if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {
            if (DbTypeUtil.checkOracle(dataSourceUtil)) {
                String startTimes = DateUtil.daFormatYmd(Long.parseLong(startTime)) + " 00:00:00";
                String endTimes = DateUtil.daFormatYmd(Long.parseLong(endTime)) + " 23:59:59";
                dbSql.append(" AND r.F_HandleTime Between TO_DATE('"
                        + startTimes
                        + "','yyyy-mm-dd HH24:mi:ss') AND TO_DATE('"
                        + endTimes
                        + "','yyyy-mm-dd HH24:mi:ss') ");
            } else {
                String startTimes = DateUtil.daFormatYmd(Long.parseLong(startTime)) + " 00:00:00";
                String endTimes = DateUtil.daFormatYmd(Long.parseLong(endTime)) + " 23:59:59";
                dbSql.append(" AND r.F_HandleTime Between '" + startTimes + "' AND '" + endTimes + "' ");
            }
        }
        // 所属流程
        String flowId = paginationFlowTask.getFlowId() != null ? paginationFlowTask.getFlowId() : null;
        if (!StringUtils.isEmpty(flowId)) {
            dbSql.append(" AND t.F_FlowId = '" + flowId + "' ");
        }
        // 所属分类
        String flowCategory =
                paginationFlowTask.getFlowCategory() != null ? paginationFlowTask.getFlowCategory() : null;
        if (!StringUtils.isEmpty(flowCategory)) {
            dbSql.append(" AND t.F_FlowCategory = '" + flowCategory + "' ");
        }
        // 发起人员
        String creatorUserId =
                paginationFlowTask.getCreatorUserId() != null ? paginationFlowTask.getCreatorUserId() : null;
        if (!StringUtils.isEmpty(creatorUserId)) {
            dbSql.append(" AND t.F_CreatorUserId = '" + creatorUserId + "' ");
        }
        // 排序
        if ("desc".equals(paginationFlowTask.getSort().toLowerCase())) {
            dbSql.append(" Order by t.F_LastModifyTime DESC ");
        } else {
            dbSql.append(" Order by t.F_LastModifyTime ASC ");
        }
        dbSql.append(", F_CreatorTime desc");
        queryParam.put("handleId", userId);
        queryParam.put("sql", dbSql.toString());
        List<FlowTaskListModel> data = this.baseMapper.getTrialList(queryParam);
        return MpUtils.toPage(paginationFlowTask, data);
    }

    @Override
    public List<FlowTaskListModel> getTrialList() {
        String userId = userProvider.get().getUserId();
        Map<String, Object> queryParam = new HashMap<>(16);
        StringBuilder dbSql = new StringBuilder();
        queryParam.put("handleId", userId);
        queryParam.put("sql", dbSql.toString());
        List<FlowTaskListModel> data = this.baseMapper.getTrialList(queryParam);
        return data;
    }

    @Override
    public List<FlowTaskEntity> getWaitList() {
        String userId = userProvider.get().getUserId();
        StringBuilder dbSql = new StringBuilder();
        // 查询自己的待办
        dbSql.append(" AND (");
        dbSql.append("o.F_HandleId = '" + userId + "' ");
        // 委托审核
        List<FlowDelegateEntity> flowDelegateList = flowDelegateService.getUser(userId);
        if (flowDelegateList.size() > 0) {
            dbSql.append(" OR ");
            for (int i = 0; i < flowDelegateList.size(); i++) {
                FlowDelegateEntity delegateEntity = flowDelegateList.get(i);
                // 委托的人
                dbSql.append(" o.F_HandleId = '" + delegateEntity.getCreatorUserId() + "' ");
                if (flowDelegateList.size() - 1 > i) {
                    dbSql.append(" OR ");
                }
            }
            dbSql.append(")");
        } else {
            dbSql.append(")");
        }
        List<FlowTaskListModel> data = this.baseMapper.getWaitList(dbSql.toString());
        // 返回数据
        List<FlowTaskEntity> result = JsonUtils.getJsonToList(data, FlowTaskEntity.class);
        return result;
    }

    @Override
    public List<FlowTaskEntity> getDashboardWaitList() {
        String userId = userProvider.get().getUserId();
        StringBuilder dbSql = new StringBuilder();
        // 查询自己的待办
        dbSql.append(" AND (");
        dbSql.append("o.F_HandleId = '" + userId + "' ");
        // 委托审核
        List<FlowDelegateEntity> flowDelegateList = flowDelegateService.getUser(userId);
        if (flowDelegateList.size() > 0) {
            dbSql.append(" OR ");
            for (int i = 0; i < flowDelegateList.size(); i++) {
                FlowDelegateEntity delegateEntity = flowDelegateList.get(i);
                // 委托的人
                dbSql.append(" o.F_HandleId = '" + delegateEntity.getCreatorUserId() + "' ");
                if (flowDelegateList.size() - 1 > i) {
                    dbSql.append(" OR ");
                }
            }
            dbSql.append(")");
        } else {
            dbSql.append(")");
        }
        List<FlowTaskListModel> data = this.baseMapper.getWaitList(dbSql.toString());
        // 返回数据
        List<FlowTaskEntity> result = JsonUtils.toList(data, FlowTaskEntity.class);
        return result;
    }

    @Override
    public List<FlowTaskEntity> getAllWaitList() {
        String userId = userProvider.get().getUserId();
        StringBuilder dbSql = new StringBuilder();
        // 查询自己的待办
        dbSql.append(" AND (");
        dbSql.append("o.F_HandleId = '" + userId + "' )  Order by F_CreatorTime DESC");
        List<FlowTaskListModel> data = this.baseMapper.getWaitList(dbSql.toString());
        List<FlowTaskEntity> result = JsonUtils.toList(data, FlowTaskEntity.class);
        return result;
    }

    @Override
    public List<FlowTaskEntity> getDashboardAllWaitList() {
        String userId = userProvider.get().getUserId();
        StringBuilder dbSql = new StringBuilder();
        // 查询自己的待办
        dbSql.append(" AND (");
        dbSql.append("o.F_HandleId = '" + userId + "' )  Order by F_CreatorTime DESC");
        List<FlowTaskListModel> data = this.baseMapper.getWaitList(dbSql.toString());
        List<FlowTaskEntity> result = JsonUtils.getJsonToList(data, FlowTaskEntity.class);
        if (result.size() > 20) {
            return result.subList(0, 20);
        }
        return result;
    }

    @Override
    public IPage<FlowTaskListModel> getCirculateList(PaginationFlowTask paginationFlowTask) {
        String userId = userProvider.get().getUserId();
        List<String> userIdList = new ArrayList<>();
        userIdList.add(userId);
        List<UserRelationEntity> list = serviceUtil.getListByUserIdAll(userIdList);
        List<String> userRelationList = list.stream().map(u -> u.getObjectId()).toList();
        String[] objectId = (String.join(",", userRelationList) + "," + userId).split(",");
        // 传阅人员
        StringBuilder dbSql = new StringBuilder();
        dbSql.append(" AND (");
        for (int i = 0; i < objectId.length; i++) {
            dbSql.append("c.F_ObjectId = '" + objectId[i] + "'");
            if (objectId.length - 1 > i) {
                dbSql.append(" OR ");
            }
        }
        dbSql.append(")");
        // 关键字（流程名称、流程编码）
        String keyWord = paginationFlowTask.getKeyword() != null ? paginationFlowTask.getKeyword() : null;
        if (!StringUtils.isEmpty(keyWord)) {
            dbSql.append(" AND (t.F_EnCode like "
                    + " '%"
                    + keyWord
                    + "%' "
                    + " or t.F_FullName like"
                    + " '%"
                    + keyWord
                    + "%') ");
        }
        // 日期范围（近7天、近1月、近3月、自定义）
        String startTime = paginationFlowTask.getStartTime() != null ? paginationFlowTask.getStartTime() : null;
        String endTime = paginationFlowTask.getEndTime() != null ? paginationFlowTask.getEndTime() : null;
        if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {
            if (DbTypeUtil.checkOracle(dataSourceUtil)) {
                String startTimes = DateUtil.daFormatYmd(Long.parseLong(startTime)) + " 00:00:00";
                String endTimes = DateUtil.daFormatYmd(Long.parseLong(endTime)) + " 23:59:59";
                dbSql.append(" AND c.F_CreatorTime Between TO_DATE('"
                        + startTimes
                        + "','yyyy-mm-dd HH24:mi:ss') AND TO_DATE('"
                        + endTimes
                        + "','yyyy-mm-dd HH24:mi:ss') ");
            } else {
                String startTimes = DateUtil.daFormatYmd(Long.parseLong(startTime)) + " 00:00:00";
                String endTimes = DateUtil.daFormatYmd(Long.parseLong(endTime)) + " 23:59:59";
                dbSql.append(" AND c.F_CreatorTime Between  '" + startTimes + "' AND '" + endTimes + "' ");
            }
        }
        // 所属流程
        String flowId = paginationFlowTask.getFlowId() != null ? paginationFlowTask.getFlowId() : null;
        if (!StringUtils.isEmpty(flowId)) {
            dbSql.append(" AND t.F_FlowId = '" + flowId + "'");
        }
        // 所属分类
        String flowCategory =
                paginationFlowTask.getFlowCategory() != null ? paginationFlowTask.getFlowCategory() : null;
        if (!StringUtils.isEmpty(flowCategory)) {
            dbSql.append(" AND t.F_FlowCategory = '" + flowCategory + "'");
        }
        // 发起人员
        String creatorUserId =
                paginationFlowTask.getCreatorUserId() != null ? paginationFlowTask.getCreatorUserId() : null;
        if (!StringUtils.isEmpty(creatorUserId)) {
            dbSql.append(" AND t.F_CreatorUserId = '" + creatorUserId + "'");
        }
        // 排序
        if ("desc".equalsIgnoreCase(paginationFlowTask.getSort())) {
            dbSql.append(" Order by F_CreatorTime DESC");
        } else {
            dbSql.append(" Order by F_CreatorTime DESC");
        }
        List<FlowTaskListModel> data = this.baseMapper.getCirculateList(dbSql.toString());
        return MpUtils.toPage(paginationFlowTask, data);
    }

    @Override
    public FlowTaskEntity getInfo(String id) {
        QueryWrapper<FlowTaskEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().and(t -> t.eq(FlowTaskEntity::getId, id));
        FlowTaskEntity entity = this.getOne(queryWrapper);
        if (entity == null) {
            queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().and(t -> t.eq(FlowTaskEntity::getProcessId, id));
            entity = this.getOne(queryWrapper);
        }
        return entity;
    }

    @Override
    public void update(FlowTaskEntity entity) {
        this.updateById(entity);
    }

    @Override
    public void create(FlowTaskEntity entity) {
        this.save(entity);
    }

    @Override
    public FlowTaskEntity getInfoSubmit(String id, SFunction<FlowTaskEntity, ?>... columns) {
        List<FlowTaskEntity> list = getInfosSubmit(new String[] {id}, columns);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<FlowTaskEntity> getInfosSubmit(String[] ids, SFunction<FlowTaskEntity, ?>... columns) {
        List<FlowTaskEntity> resultList = Collections.emptyList();
        if (ids == null || ids.length == 0) {
            return resultList;
        }
        LambdaQueryWrapper<FlowTaskEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (ids.length == 1) {
            queryWrapper.select(columns).and(t -> t.eq(FlowTaskEntity::getId, ids[0]));
            resultList = this.list(queryWrapper);
            if (resultList.isEmpty()) {
                queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(columns).and(t -> t.eq(FlowTaskEntity::getProcessId, ids[0]));
                resultList = this.list(queryWrapper);
            }
        } else {
            queryWrapper.select(FlowTaskEntity::getId).and(t -> {
                t.in(FlowTaskEntity::getId, ids).or().in(FlowTaskEntity::getProcessId, ids);
            });
            List<String> resultIds = this.listObjs(queryWrapper, Object::toString);
            if (!resultIds.isEmpty()) {
                queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(columns).in(FlowTaskEntity::getId, resultIds);
                resultList = this.list(queryWrapper);
            }
        }
        return resultList;
    }

    @Override
    public void delete(FlowTaskEntity entity) throws WorkFlowException {
        if (!checkStatus(entity.getStatus())) {
            throw new WorkFlowException(MsgCode.WF116.get());
        } else {
            this.removeById(entity.getId());
            QueryWrapper<FlowTaskNodeEntity> node = new QueryWrapper<>();
            node.lambda().eq(FlowTaskNodeEntity::getTaskId, entity.getId());
            flowTaskNodeService.remove(node);
            QueryWrapper<FlowTaskOperatorEntity> operator = new QueryWrapper<>();
            operator.lambda().eq(FlowTaskOperatorEntity::getTaskId, entity.getId());
            flowTaskOperatorService.remove(operator);
            QueryWrapper<FlowTaskOperatorRecordEntity> record = new QueryWrapper<>();
            record.lambda().eq(FlowTaskOperatorRecordEntity::getTaskId, entity.getId());
            flowTaskOperatorRecordService.remove(record);
            QueryWrapper<FlowTaskCirculateEntity> circulate = new QueryWrapper<>();
            circulate.lambda().eq(FlowTaskCirculateEntity::getTaskId, entity.getId());
            flowTaskCirculateService.remove(circulate);
        }
    }

    @Override
    public void deleteChild(FlowTaskEntity entity) {
        this.removeById(entity.getId());
        QueryWrapper<FlowTaskNodeEntity> node = new QueryWrapper<>();
        node.lambda().eq(FlowTaskNodeEntity::getTaskId, entity.getId());
        flowTaskNodeService.remove(node);
        QueryWrapper<FlowTaskOperatorEntity> operator = new QueryWrapper<>();
        operator.lambda().eq(FlowTaskOperatorEntity::getTaskId, entity.getId());
        flowTaskOperatorService.remove(operator);
        QueryWrapper<FlowTaskOperatorRecordEntity> record = new QueryWrapper<>();
        record.lambda().eq(FlowTaskOperatorRecordEntity::getTaskId, entity.getId());
        flowTaskOperatorRecordService.remove(record);
        QueryWrapper<FlowTaskCirculateEntity> circulate = new QueryWrapper<>();
        circulate.lambda().eq(FlowTaskCirculateEntity::getTaskId, entity.getId());
        flowTaskCirculateService.remove(circulate);
    }

    @Override
    public void delete(String[] ids) throws WorkFlowException {
        if (ids.length > 0) {
            List<FlowTaskEntity> flowTaskList = getOrderStaList(Arrays.asList(ids));
            boolean isDel = flowTaskList.stream().anyMatch(t -> t.getFlowType() == 1);
            if (isDel) {
                throw new WorkFlowException(MsgCode.WF117.get());
            }
            isDel = flowTaskList.stream()
                    .anyMatch(t -> !FlowNature.ParentId.equals(t.getParentId()) && StrUtil.isNotEmpty(t.getParentId()));
            if (isDel) {
                throw new WorkFlowException(MsgCode.WF118.get());
            }
            QueryWrapper<FlowTaskEntity> task = new QueryWrapper<>();
            task.lambda().in(FlowTaskEntity::getId, ids);
            this.remove(task);
            QueryWrapper<FlowTaskNodeEntity> node = new QueryWrapper<>();
            node.lambda().in(FlowTaskNodeEntity::getTaskId, ids);
            flowTaskNodeService.remove(node);
            QueryWrapper<FlowTaskOperatorEntity> operator = new QueryWrapper<>();
            operator.lambda().in(FlowTaskOperatorEntity::getTaskId, ids);
            flowTaskOperatorService.remove(operator);
            QueryWrapper<FlowTaskOperatorRecordEntity> record = new QueryWrapper<>();
            record.lambda().in(FlowTaskOperatorRecordEntity::getTaskId, ids);
            flowTaskOperatorRecordService.remove(record);
            QueryWrapper<FlowTaskCirculateEntity> circulate = new QueryWrapper<>();
            circulate.lambda().in(FlowTaskCirculateEntity::getTaskId, ids);
            flowTaskCirculateService.remove(circulate);
        }
    }

    @Override
    public List<FlowTaskEntity> getTaskList(String id) {
        QueryWrapper<FlowTaskEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowTaskEntity::getFlowId, id);
        return this.list(queryWrapper);
    }

    @Override
    public List<FlowTaskEntity> getOrderStaList(List<String> id) {
        List<FlowTaskEntity> list = new ArrayList<>();
        if (id.size() > 0) {
            QueryWrapper<FlowTaskEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(FlowTaskEntity::getId, id);
            list = this.list(queryWrapper);
        }
        return list;
    }

    @Override
    public List<FlowTaskEntity> getChildList(String id, SFunction<FlowTaskEntity, ?>... columns) {
        QueryWrapper<FlowTaskEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(columns).in(FlowTaskEntity::getParentId, id);
        return this.list(queryWrapper);
    }

    @Override
    public List<FlowTaskEntity> getTaskFlowList(String flowId) {
        QueryWrapper<FlowTaskEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowTaskEntity::getFlowId, flowId);
        List<Integer> list = new ArrayList() {
            {
                add(2);
                add(5);
            }
        };
        queryWrapper.lambda().notIn(FlowTaskEntity::getStatus, list);
        return this.list(queryWrapper);
    }

    @Override
    public List<FlowTaskEntity> getFlowList(String flowId) {
        QueryWrapper<FlowTaskEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowTaskEntity::getFlowId, flowId);
        queryWrapper.lambda().select(FlowTaskEntity::getId);
        return list(queryWrapper);
    }

    @Override
    public List<FlowBatchModel> batchFlowSelector() {
        List<FlowTaskOperatorEntity> operatorList = flowTaskOperatorService.getBatchList();
        List<String> taskIdList =
                operatorList.stream().map(FlowTaskOperatorEntity::getTaskId).toList();
        List<FlowTaskEntity> taskList = getOrderStaList(taskIdList);
        Map<String, List<FlowTaskEntity>> flowIdList = taskList.stream()
                .filter(t -> ObjectUtil.isNotEmpty(t.getIsBatch()) && t.getIsBatch() == 1)
                .collect(Collectors.groupingBy(FlowTaskEntity::getFlowId));
        List<FlowBatchModel> batchFlowList = new ArrayList<>();
        for (String key : flowIdList.keySet()) {
            List<FlowTaskEntity> flowTaskList = flowIdList.get(key);
            List<String> flowTask =
                    flowTaskList.stream().map(FlowTaskEntity::getId).toList();
            if (flowTaskList.size() > 0) {
                String flowName = flowTaskList.stream()
                        .map(FlowTaskEntity::getFlowName)
                        .distinct()
                        .collect(Collectors.joining(","));
                String flowId = flowTaskList.stream()
                        .map(FlowTaskEntity::getFlowId)
                        .distinct()
                        .collect(Collectors.joining(","));
                Long count = operatorList.stream()
                        .filter(t -> flowTask.contains(t.getTaskId()))
                        .count();
                FlowBatchModel batchModel = new FlowBatchModel();
                batchModel.setNum(count);
                batchModel.setId(flowId);
                batchModel.setFullName(flowName + "(" + count + ")");
                batchFlowList.add(batchModel);
            }
        }
        batchFlowList = batchFlowList.stream()
                .sorted(Comparator.comparing(FlowBatchModel::getNum).reversed())
                .toList();
        return batchFlowList;
    }

    /**
     * 验证有效状态
     *
     * @param status 状态编码
     * @return
     */
    private boolean checkStatus(int status) {
        return status == FlowTaskStatusEnum.Draft.getCode()
                || status == FlowTaskStatusEnum.Reject.getCode()
                || status == FlowTaskStatusEnum.Revoke.getCode();
    }
}
