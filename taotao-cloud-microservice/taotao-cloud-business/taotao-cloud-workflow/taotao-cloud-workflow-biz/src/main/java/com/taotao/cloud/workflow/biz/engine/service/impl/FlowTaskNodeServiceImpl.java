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


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.boot.common.utils.common.JsonUtils;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.nodejson.ChildNodeList;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskNodeEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowNodeEnum;
import com.taotao.cloud.workflow.biz.engine.mapper.FlowTaskNodeMapper;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskNodeService;
import com.taotao.cloud.workflow.biz.engine.util.FlowNature;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/** 流程节点 */
@Service
public class FlowTaskNodeServiceImpl extends ServiceImpl<FlowTaskNodeMapper, FlowTaskNodeEntity>
        implements FlowTaskNodeService {

    @Override
    public List<FlowTaskNodeEntity> getListAll() {
        QueryWrapper<FlowTaskNodeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .lambda()
                .orderByAsc(FlowTaskNodeEntity::getSortCode)
                .orderByDesc(FlowTaskNodeEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

    @Override
    public List<FlowTaskNodeEntity> getList(String taskId) {
        QueryWrapper<FlowTaskNodeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .lambda()
                .eq(FlowTaskNodeEntity::getTaskId, taskId)
                .orderByAsc(FlowTaskNodeEntity::getSortCode)
                .orderByDesc(FlowTaskNodeEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

    @Override
    public FlowTaskNodeEntity getInfo(String id) {
        QueryWrapper<FlowTaskNodeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowTaskNodeEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    public void deleteByTaskId(String taskId) {
        QueryWrapper<FlowTaskNodeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowTaskNodeEntity::getTaskId, taskId);
        this.remove(queryWrapper);
    }

    @Override
    public void create(List<FlowTaskNodeEntity> dataAll) {
        List<FlowTaskNodeEntity> startNodes = dataAll.stream()
                .filter(t -> FlowNature.NodeStart.equals(t.getNodeType()))
                .toList();
        if (startNodes.size() > 0) {
            String startNode = startNodes.get(0).getNodeCode();
            long num = 0L;
            long maxNum = 0L;
            List<Long> max = new ArrayList<>();
            List<FlowTaskNodeEntity> treeList = new ArrayList<>();
            nodeList(dataAll, startNode, treeList, num, max);
            List<Long> sortIdList = max.stream().sorted(Long::compareTo).toList();
            if (sortIdList.size() > 0) {
                maxNum = sortIdList.get(sortIdList.size() - 1);
            }
            String nodeNext = FlowNature.NodeEnd;
            for (FlowTaskNodeEntity entity : dataAll) {
                String type = entity.getNodeType();
                FlowTaskNodeEntity node = treeList.stream()
                        .filter(t -> t.getNodeCode().equals(entity.getNodeCode()))
                        .findFirst()
                        .orElse(null);
                // 判断结束节点是否多个
                List<FlowTaskNodeEntity> endCount = treeList.stream()
                        .filter(t -> StrUtil.isEmpty(t.getNodeNext()))
                        .toList();
                // 判断下一节点是否多个
                String next = entity.getNodeNext();
                List<FlowTaskNodeEntity> nextNum = treeList.stream()
                        .filter(t -> t.getNodeNext().equals(next))
                        .toList();
                if (StringUtil.isEmpty(next)) {
                    entity.setNodeNext(nodeNext);
                }
                if (node != null) {
                    entity.setSortCode(node.getSortCode());
                    entity.setState(FlowNodeEnum.Process.getCode());
                    if (StringUtil.isEmpty(next)) {
                        entity.setNodeNext(nodeNext);
                    }
                }
                // 判断下一节点是否相同
                if (!"empty".equals(type) && !"timer".equals(type)) {
                    // 至少2条下一节点一样,才有可能是分流
                    if (endCount.size() > 1) {
                        if (nodeNext.equals(entity.getNodeNext())) {
                            ChildNodeList modelList =
                                    JsonUtils.toObject(entity.getNodePropertyJson(), ChildNodeList.class);
                            // 添加指向下一节点的id
                            List<String> nextEndList =
                                    endCount.stream().map(t -> t.getNodeCode()).toList();
                            nextEndList.remove(entity.getNodeCode());
                            // 赋值合流id和分流的id
                            modelList.getCustom().setInterflow(true);
                            modelList.getCustom().setInterflowId(String.join(",", nextEndList));
                            modelList.getCustom().setInterflowNextId(nodeNext);
                            entity.setNodePropertyJson(JsonUtils.toJSONString(modelList));
                        }
                    }
                    // 至少2条下一节点一样,才有可能是分流
                    if (nextNum.size() > 1) {
                        ChildNodeList modelList = JsonUtils.toObject(entity.getNodePropertyJson(), ChildNodeList.class);
                        // 添加指向下一节点的id
                        List<String> nextEndList = nextNum.stream()
                                .map(FlowTaskNodeEntity::getNodeCode)
                                .toList();
                        nextEndList.remove(entity.getNodeCode());
                        // 赋值合流id和分流的id
                        modelList.getCustom().setInterflowId(String.join(",", nextEndList));
                        modelList.getCustom().setInterflowNextId(next);
                        modelList.getCustom().setInterflow(true);
                        entity.setNodePropertyJson(JsonUtils.toJSONString(modelList));
                    }
                    this.save(entity);
                }
            }
            FlowTaskNodeEntity endround = new FlowTaskNodeEntity();
            endround.setId(RandomUtil.uuId());
            endround.setNodeCode(nodeNext);
            endround.setNodeName("结束");
            endround.setCompletion(FlowNature.ProcessCompletion);
            endround.setCreatorTime(new Date());
            endround.setSortCode(maxNum + 1);
            endround.setTaskId(treeList.get(0).getTaskId());
            endround.setNodePropertyJson(startNodes.get(0).getNodePropertyJson());
            endround.setNodeType("endround");
            endround.setState(FlowNodeEnum.Process.getCode());
            this.save(endround);
        }
    }

    @Override
    public void create(FlowTaskNodeEntity entity) {
        this.save(entity);
    }

    @Override
    public void update(FlowTaskNodeEntity entity) {
        this.updateById(entity);
    }

    @Override
    public void update(String taskId) {
        UpdateWrapper<FlowTaskNodeEntity> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(FlowTaskNodeEntity::getTaskId, taskId);
        wrapper.lambda().set(FlowTaskNodeEntity::getCompletion, FlowNodeEnum.Futility.getCode());
        wrapper.lambda().set(FlowTaskNodeEntity::getState, FlowNodeEnum.Futility.getCode());
        this.update(wrapper);
    }

    @Override
    public void updateCompletion(List<String> id, int start) {
        if (id.size() > 0) {
            UpdateWrapper<FlowTaskNodeEntity> wrapper = new UpdateWrapper<>();
            wrapper.lambda().in(FlowTaskNodeEntity::getId, id);
            wrapper.lambda().set(FlowTaskNodeEntity::getCompletion, start);
            this.update(wrapper);
        }
    }

    private void nodeList(
            List<FlowTaskNodeEntity> dataAll,
            String nodeCode,
            List<FlowTaskNodeEntity> treeList,
            long num,
            List<Long> max) {
        num++;
        max.add(num);
        List<FlowTaskNodeEntity> thisEntity =
                dataAll.stream().filter(t -> t.getNodeCode().contains(nodeCode)).toList();
        for (int i = 0; i < thisEntity.size(); i++) {
            FlowTaskNodeEntity entity = thisEntity.get(i);
            entity.setSortCode(num);
            entity.setState(FlowNodeEnum.Process.getCode());
            treeList.add(entity);
            String[] nodeNext = entity.getNodeNext().split(",");
            if (nodeNext.length > 0) {
                for (int k = 0; k < nodeNext.length; k++) {
                    String next = nodeNext[k];
                    long nums = treeList.stream()
                            .filter(t -> t.getNodeCode().equals(next))
                            .count();
                    if (StrUtil.isNotEmpty(next) && nums == 0) {
                        nodeList(dataAll, next, treeList, num, max);
                    }
                }
            }
        }
    }
}
