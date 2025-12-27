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

package com.taotao.cloud.workflow.biz.engine.util;

import com.taotao.boot.common.utils.json.JacksonUtils;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.childnode.ChildNode;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.childnode.ProperCond;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.childnode.Properties;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.nodejson.ChildNodeList;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.nodejson.ConditionList;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.nodejson.Custom;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.nodejson.DateProperties;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskNodeEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import lombok.Data;
import com.taotao.boot.common.utils.lang.StringUtils;

/** 在线工作流开发 */
@Data
public class FlowJsonUtil {

    /** 外层节点 */
    private static String cusNum = "0";

    /** 获取下一节点 */
    public static String getNextNode(
            String nodeId, String data, List<ChildNodeList> childNodeListAll, List<ConditionList> conditionListAll) {
        return nextNodeId(data, nodeId, childNodeListAll, conditionListAll);
    }

    /** 下一节点id */
    private static String nextNodeId(
            String data, String nodeId, List<ChildNodeList> childNodeListAll, List<ConditionList> conditionListAll) {
        String nextId = "";
        boolean flag = false;
        ChildNodeList childNode = childNodeListAll.stream()
                .filter(t -> t.getCustom().getNodeId().equals(nodeId))
                .findFirst()
                .orElse(null);
        String contextType = childNode.getConditionType();
        // 条件、分流的判断
        if (StringUtils.isNotEmpty(contextType)) {
            if (FlowCondition.CONDITION.equals(contextType)) {
                List<String> nextNodeId = new ArrayList<>();
                getContionNextNode(data, conditionListAll, nodeId, nextNodeId);
                nextId = String.join(",", nextNodeId);
                if (StringUtils.isNotEmpty(nextId)) {
                    flag = true;
                }
            } else if (FlowCondition.INTERFLOW.equals(contextType)) {
                nextId = childNode.getCustom().getFlowId();
                flag = true;
            }
        }
        // 子节点
        if (!flag) {
            if (childNode.getCustom().getFlow()) {
                nextId = childNode.getCustom().getFlowId();
            } else {
                // 不是外层的下一节点
                if (!cusNum.equals(childNode.getCustom().getNum())) {
                    nextId = childNode.getCustom().getFirstId();
                    if (childNode.getCustom().getChild()) {
                        nextId = childNode.getCustom().getChildNode();
                    }
                } else {
                    // 外层的子节点
                    if (childNode.getCustom().getChild()) {
                        nextId = childNode.getCustom().getChildNode();
                    }
                }
            }
        }
        return nextId;
    }

    // ---------------------------------------------------递归获取当前的上节点和下节点----------------------------------------------

    /** 获取当前已完成节点 */
    private static void upList(
            List<FlowTaskNodeEntity> flowTaskNodeList, String node, Set<String> upList, String[] tepId) {
        FlowTaskNodeEntity entity = flowTaskNodeList.stream()
                .filter(t -> t.getNodeCode().equals(node))
                .findFirst()
                .orElse(null);
        if (entity != null) {
            List<String> list = flowTaskNodeList.stream()
                    .filter(t -> t.getSortCode() != null && t.getSortCode() < entity.getSortCode())
                    .map(FlowTaskNodeEntity::getNodeCode)
                    .toList();
            list.removeAll(Arrays.asList(tepId));
            upList.addAll(list);
        }
    }

    /** 获取当前未完成节点 */
    private static void nextList(
            List<FlowTaskNodeEntity> flowTaskNodeList, String node, Set<String> nextList, String[] tepId) {
        FlowTaskNodeEntity entity = flowTaskNodeList.stream()
                .filter(t -> t.getNodeCode().equals(node))
                .findFirst()
                .orElse(null);
        if (entity != null) {
            List<String> list = flowTaskNodeList.stream()
                    .filter(t -> t.getSortCode() != null && t.getSortCode() > entity.getSortCode())
                    .map(FlowTaskNodeEntity::getNodeCode)
                    .toList();
            list.removeAll(Arrays.asList(tepId));
            nextList.addAll(list);
        }
    }

    // ---------------------------------------------------条件----------------------------------------------

    /** 递归条件 */
    private static void getContionNextNode(
            String data, List<ConditionList> conditionListAll, String nodeId, List<String> nextNodeId) {
        List<ConditionList> conditionAll = conditionListAll.stream()
                .filter(t -> t.getPrevId().equals(nodeId))
                .toList();
        for (ConditionList condition : conditionAll) {
            List<ProperCond> conditions = condition.getConditions();
            boolean flag = nodeConditionDecide(data, conditions, new HashMap<>(100), new HashMap<>(100));
            // 判断条件是否成立或者其他情况条件
            if (flag || condition.getIsDefault()) {
                String conditionId = condition.getNodeId();
                List<ConditionList> collect = conditionListAll.stream()
                        .filter(t -> t.getPrevId().equals(conditionId))
                        .toList();
                if (collect.size() > 0) {
                    getContionNextNode(data, conditionListAll, conditionId, nextNodeId);
                } else {
                    if (nextNodeId.size() == 0) {
                        // 先获取条件下的分流节点
                        if (condition.getFlow()) {
                            nextNodeId.add(condition.getFlowId());
                        } else {
                            // 条件的子节点
                            if (condition.getChild()) {
                                nextNodeId.add(condition.getChildNodeId());
                            } else {
                                nextNodeId.add(condition.getFirstId());
                            }
                        }
                    }
                }
            }
        }
    }

    /** 节点条件判断 */
    private static boolean nodeConditionDecide(
            String formDataJson,
            List<ProperCond> conditionList,
            Map<String, String> flowKey,
            Map<String, Object> keyList) {
        boolean flag = false;
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("js");
        Map<String, Object> map = JacksonUtils.toMap(formDataJson);
        StringBuilder expression = new StringBuilder();
        for (int i = 0; i < conditionList.size(); i++) {
            String logic = conditionList.get(i).getLogic();
            String field = conditionList.get(i).getField();
            Object form = map.get(field);
            String formValue = "'" + form + "'";
            String symbol = conditionList.get(i).getSymbol();
            if ("<>".equals(symbol)) {
                symbol = "!=";
            }
            String value = conditionList.get(i).getFiledValue();
            String filedValue = "'" + value + "'";
            if (">".equals(symbol) || ">=".equals(symbol) || "<=".equals(symbol) || "<".equals(symbol)) {
                formValue = "parseFloat(" + formValue + ")";
                filedValue = "parseFloat(" + filedValue + ")";
            }
            String pression = formValue + symbol + filedValue;
            expression.append(pression);
            if (!StringUtils.isEmpty(logic)) {
                if (i != conditionList.size() - 1) {
                    expression.append(" ").append(logic).append(" ");
                }
            }
        }
        try {
            flag = (Boolean) scriptEngine.eval(expression.toString());
        } catch (Exception e) {
            LogUtils.info(e.getMessage());
        }
        return flag;
    }

    // ---------------------------------------------------------------解析--------------------------------------------------------------------------

    /** 递归外层的节点 */
    private static void childListAll(ChildNode childNode, List<ChildNode> chilNodeList) {
        if (childNode != null) {
            chilNodeList.add(childNode);
            boolean haschildNode = childNode.getChildNode() != null;
            if (haschildNode) {
                ChildNode nextNode = childNode.getChildNode();
                childListAll(nextNode, chilNodeList);
            }
        }
    }

    /** 最外层的json */
    public static void getTemplateAll(
            ChildNode childNode, List<ChildNodeList> childNodeListAll, List<ConditionList> conditionListAll) {
        List<ChildNode> chilNodeList = new ArrayList<>();
        childListAll(childNode, chilNodeList);
        if (childNode != null) {
            String nodeId = childNode.getNodeId();
            String prevId = childNode.getPrevId();
            boolean haschildNode = childNode.getChildNode() != null;
            boolean hasconditionNodes = childNode.getConditionNodes() != null;
            Properties properties = childNode.getProperties();
            // 属性赋值
            assignment(properties);
            ChildNodeList childNodeList = new ChildNodeList();
            childNodeList.setProperties(properties);
            // 定时器
            DateProperties model = JacksonUtils.toObject(properties, DateProperties.class);
            childNodeList.setTimer(model);
            // 自定义属性
            Custom customModel = new Custom();
            customModel.setType(childNode.getType());
            customModel.setNum("0");
            customModel.setFirstId("");
            customModel.setChild(haschildNode);
            customModel.setNodeId(nodeId);
            customModel.setPrevId(prevId);
            customModel.setChildNode(haschildNode ? childNode.getChildNode().getNodeId() : "");
            // 判断子节点数据是否还有分流节点,有的话保存分流节点id
            if (hasconditionNodes) {
                childNodeList.setConditionType(FlowCondition.CONDITION);
                List<ChildNode> conditionNodes = childNode.getConditionNodes().stream()
                        .filter(t -> t.getIsInterflow() != null)
                        .toList();
                boolean isFlow = conditionNodes.size() > 0;
                if (isFlow) {
                    customModel.setFlow(isFlow);
                    childNodeList.setConditionType(FlowCondition.INTERFLOW);
                    List<String> flowIdAll =
                            conditionNodes.stream().map(ChildNode::getNodeId).toList();
                    customModel.setFlowId(String.join(",", flowIdAll));
                }
            }
            childNodeList.setCustom(customModel);
            childNodeListAll.add(childNodeList);
            String firstId = "";
            if (haschildNode) {
                firstId = childNode.getChildNode().getNodeId();
            }
            if (hasconditionNodes) {
                conditionList(childNode, firstId, childNodeListAll, conditionListAll, chilNodeList);
            }
            if (haschildNode) {
                getchildNode(childNode, firstId, childNodeListAll, conditionListAll, chilNodeList);
            }
        }
    }

    /** 递归子节点的子节点 */
    private static void getchildNode(
            ChildNode parentChildNodeTest,
            String firstId,
            List<ChildNodeList> childNodeListAll,
            List<ConditionList> conditionListAll,
            List<ChildNode> chilNodeList) {
        ChildNode childNode = parentChildNodeTest.getChildNode();
        if (childNode != null) {
            String nodeId = childNode.getNodeId();
            String prevId = childNode.getPrevId();
            boolean haschildNode = childNode.getChildNode() != null;
            boolean hasconditionNodes = childNode.getConditionNodes() != null;
            Properties properModel = childNode.getProperties();
            // 属性赋值
            assignment(properModel);
            ChildNodeList childNodeList = new ChildNodeList();
            childNodeList.setProperties(properModel);
            // 定时器
            DateProperties model = JacksonUtils.toObject(properModel, DateProperties.class);
            childNodeList.setTimer(model);
            // 自定义属性
            Custom customModel = new Custom();
            customModel.setType(childNode.getType());
            boolean isFirst = chilNodeList.stream().anyMatch(t -> t.getNodeId().equals(nodeId));
            customModel.setNum(isFirst ? "0" : "1");
            customModel.setFirstId(firstId);
            if (isFirst) {
                customModel.setFirstId(haschildNode ? childNode.getChildNode().getNodeId() : "");
            }
            customModel.setChild(haschildNode);
            customModel.setNodeId(nodeId);
            customModel.setPrevId(prevId);
            customModel.setChildNode(haschildNode ? childNode.getChildNode().getNodeId() : "");
            // 判断子节点数据是否还有分流节点,有的话保存分流节点id
            if (hasconditionNodes) {
                childNodeList.setConditionType(FlowCondition.CONDITION);
                List<ChildNode> conditionNodes = childNode.getConditionNodes().stream()
                        .filter(t -> t.getIsInterflow() != null)
                        .toList();
                boolean isFlow = conditionNodes.size() > 0;
                if (isFlow) {
                    customModel.setFlow(isFlow);
                    childNodeList.setConditionType(FlowCondition.INTERFLOW);
                    List<String> flowIdAll =
                            conditionNodes.stream().map(ChildNode::getNodeId).toList();
                    customModel.setFlowId(String.join(",", flowIdAll));
                }
            }
            childNodeList.setCustom(customModel);
            childNodeListAll.add(childNodeList);
            // 条件或者分流递归
            if (hasconditionNodes) {
                conditionList(childNode, firstId, childNodeListAll, conditionListAll, chilNodeList);
            }
            // 子节点递归
            if (haschildNode) {
                getchildNode(childNode, firstId, childNodeListAll, conditionListAll, chilNodeList);
            }
        }
    }

    /** 条件、分流递归 */
    private static void conditionList(
            ChildNode childNode,
            String firstId,
            List<ChildNodeList> childNodeListAll,
            List<ConditionList> conditionListAll,
            List<ChildNode> chilNodeList) {
        List<ChildNode> conditionNodes = childNode.getConditionNodes();
        if (conditionNodes.size() > 0) {
            // 判断是条件还是分流
            // 判断父节点是否还有子节点,有的话替换子节点数据
            ChildNode childNodeModel = childNode.getChildNode();
            if (childNodeModel != null) {
                firstId = childNodeModel.getNodeId();
            } else {
                ChildNode nodes = chilNodeList.stream()
                        .filter(t -> t.getNodeId().equals(childNode.getNodeId()))
                        .findFirst()
                        .orElse(null);
                if (nodes != null) {
                    if (nodes.getChildNode() != null) {
                        firstId = childNode.getChildNode().getNodeId();
                    } else {
                        firstId = "";
                    }
                }
            }
            for (ChildNode node : conditionNodes) {
                boolean conditionType = node.getIsInterflow() == null;
                if (conditionType) {
                    getCondition(node, firstId, childNodeListAll, conditionListAll, chilNodeList);
                } else {
                    getConditonFlow(node, firstId, childNodeListAll, conditionListAll, chilNodeList);
                }
            }
        }
    }

    /** 条件递归 */
    private static void getCondition(
            ChildNode childNode,
            String firstId,
            List<ChildNodeList> childNodeListAll,
            List<ConditionList> conditionListAll,
            List<ChildNode> chilNodeList) {
        if (childNode != null) {
            String nodeId = childNode.getNodeId();
            String prevId = childNode.getPrevId();
            boolean haschildNode = childNode.getChildNode() != null;
            boolean hasconditionNodes = childNode.getConditionNodes() != null;
            boolean isDefault = childNode.getProperties().getIsDefault() != null
                    ? childNode.getProperties().getIsDefault()
                    : false;
            ConditionList conditionList = new ConditionList();
            conditionList.setNodeId(nodeId);
            conditionList.setPrevId(prevId);
            conditionList.setChild(haschildNode);
            conditionList.setTitle(childNode.getProperties().getTitle());
            conditionList.setConditions(childNode.getProperties().getConditions());
            conditionList.setChildNodeId(haschildNode ? childNode.getChildNode().getNodeId() : "");
            conditionList.setIsDefault(isDefault);
            conditionList.setFirstId(firstId);
            // 判断子节点数据是否还有分流节点,有的话保存分流节点id
            if (hasconditionNodes) {
                List<ChildNode> conditionNodes = childNode.getConditionNodes().stream()
                        .filter(t -> t.getIsInterflow() != null)
                        .toList();
                boolean isFlow = conditionNodes.size() > 0;
                if (isFlow) {
                    conditionList.setFlow(isFlow);
                    List<String> flowIdAll =
                            conditionNodes.stream().map(ChildNode::getNodeId).toList();
                    conditionList.setFlowId(String.join(",", flowIdAll));
                }
            }
            conditionListAll.add(conditionList);
            // 递归条件、分流
            if (hasconditionNodes) {
                conditionList(childNode, firstId, childNodeListAll, conditionListAll, chilNodeList);
            }
            // 递归子节点
            if (haschildNode) {
                getchildNode(childNode, firstId, childNodeListAll, conditionListAll, chilNodeList);
            }
        }
    }

    /** 条件递归 */
    private static void getConditonFlow(
            ChildNode childNode,
            String firstId,
            List<ChildNodeList> childNodeListAll,
            List<ConditionList> conditionListAll,
            List<ChildNode> chilNodeList) {
        if (childNode != null) {
            String nodeId = childNode.getNodeId();
            String prevId = childNode.getPrevId();
            boolean haschildNode = childNode.getChildNode() != null;
            boolean hasconditionNodes = childNode.getConditionNodes() != null;
            Properties properties = childNode.getProperties();
            // 属性赋值
            assignment(properties);
            ChildNodeList childNodeList = new ChildNodeList();
            childNodeList.setProperties(properties);
            // 定时器
            DateProperties model = JacksonUtils.toObject(properties, DateProperties.class);
            childNodeList.setTimer(model);
            // 自定义属性
            Custom customModel = new Custom();
            customModel.setType(childNode.getType());
            customModel.setNum("1");
            customModel.setFirstId(firstId);
            customModel.setChild(haschildNode);
            customModel.setChildNode(haschildNode ? childNode.getChildNode().getNodeId() : "");
            customModel.setNodeId(nodeId);
            customModel.setPrevId(prevId);
            // 判断子节点数据是否还有分流节点,有的话保存分流节点id
            if (hasconditionNodes) {
                childNodeList.setConditionType(FlowCondition.CONDITION);
                List<ChildNode> conditionNodes = childNode.getConditionNodes().stream()
                        .filter(t -> t.getIsInterflow() != null)
                        .toList();
                boolean isFlow = conditionNodes.size() > 0;
                if (isFlow) {
                    customModel.setFlow(isFlow);
                    childNodeList.setConditionType(FlowCondition.INTERFLOW);
                    List<String> flowIdAll =
                            conditionNodes.stream().map(ChildNode::getNodeId).toList();
                    customModel.setFlowId(String.join(",", flowIdAll));
                }
            }
            childNodeList.setCustom(customModel);
            childNodeListAll.add(childNodeList);
            if (hasconditionNodes) {
                conditionList(childNode, firstId, childNodeListAll, conditionListAll, chilNodeList);
            }
            if (haschildNode) {
                getchildNode(childNode, firstId, childNodeListAll, conditionListAll, chilNodeList);
            }
        }
    }

    /**
     * 属性赋值
     *
     * @param properties
     */
    public static void assignment(Properties properties) {}
}
