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

package com.taotao.cloud.workflow.biz.common.util.treeutil;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;

/** */
public class ListToTreeUtil {

    /**
     * 转换TreeView
     *
     * @param data
     * @return
     */
    public static List<TreeViewModel> toTreeView(List<TreeViewModel> data) {
        List<TreeViewModel> treeList = getChildNodeList(data, "0");
        return treeList;
    }

    /**
     * 递归
     *
     * @param data
     * @param parentId
     */
    private static List<TreeViewModel> getChildNodeList(List<TreeViewModel> data, String parentId) {
        List<TreeViewModel> treeList = new ArrayList<>();
        List<TreeViewModel> childNodeList = data.stream()
                .filter(t -> String.valueOf(t.getParentId()).equals(parentId))
                .toList();
        for (TreeViewModel entity : childNodeList) {
            TreeViewModel model = new TreeViewModel();
            model.setId(entity.getId());
            model.setText(entity.getText());
            model.setParentId(entity.getParentId());
            model.setIsexpand(entity.getIsexpand());
            model.setComplete(entity.getComplete());
            model.setHasChildren(
                    entity.getHasChildren() == null
                            ? data.stream()
                                                    .filter(t -> String.valueOf(t.getParentId())
                                                            .equals(String.valueOf(entity.getId())))
                                                    .count()
                                            == 0
                                    ? false
                                    : true
                            : false);
            if (entity.getShowcheck()) {
                model.setCheckstate(entity.getCheckstate());
                model.setShowcheck(true);
            }
            if (entity.getImg() != null) {
                model.setImg(entity.getImg());
            }
            if (entity.getCssClass() != null) {
                model.setCssClass(entity.getCssClass());
            }
            if (entity.getClick() != null) {
                model.setClick(entity.getClick());
            }
            if (entity.getCode() != null) {
                model.setCode(entity.getCode());
            }
            if (entity.getTitle() != null) {
                model.setTitle(entity.getTitle());
            }
            if (entity.getHt() != null) {
                model.setHt(entity.getHt());
            }
            model.setChildNodes(getChildNodeList(data, entity.getId()));
            treeList.add(model);
        }
        return treeList;
    }

    /**
     * 转换树表
     *
     * @param data 数据
     * @return
     */
    public static Object listTree(List<TreeListModel> data) {
        List<Object> treeGridList = new ArrayList<>();
        getChildNodeList(data, -1, "0", treeGridList);
        Map map = new HashMap(16);
        map.put("rows", treeGridList);
        return map;
    }

    /**
     * 转换树表
     *
     * @param data 数据
     * @param parentId 父节点
     * @param level 层级
     * @return
     */
    public static Object listTree(List<TreeListModel> data, String parentId, int level) {
        List<Object> treeGridList = new ArrayList<>();
        getChildNodeList(data, level, parentId, treeGridList);
        Map map = new HashMap(16);
        map.put("rows", treeGridList);
        return map;
    }

    /**
     * 递归
     *
     * @param data 数据
     * @param level 层级
     * @param parentId 父节点
     * @param treeGridList 返回数据
     */
    private static void getChildNodeList(
            List<TreeListModel> data, int level, String parentId, List<Object> treeGridList) {
        List<TreeListModel> childNodeList =
                data.stream().filter(t -> t.getParentId().equals(parentId)).toList();
        if (childNodeList.size() > 0) {
            level++;
        }
        for (TreeListModel entity : childNodeList) {
            Map ht = new HashMap(16);
            if (entity.getHt() != null) {
                ht = entity.getHt();
            }
            ht.put("level", entity.getLevel() == null ? level : entity.getLevel());
            if (entity.getIsLeaf() != null) {
                ht.put("isLeaf", entity.getIsLeaf());
            } else {
                ht.put(
                        "isLeaf",
                        data.stream()
                                                .filter(t -> t.getParentId().equals(entity.getId()))
                                                .count()
                                        == 0
                                ? true
                                : false);
            }
            ht.put("parent", parentId);
            ht.put("expanded", entity.getExpanded());
            ht.put("loaded", entity.getLoaded());
            treeGridList.add(ht);
            getChildNodeList(data, level, entity.getId(), treeGridList);
        }
    }

    /**
     * 递归查询父节点
     *
     * @param data 条件的的数据
     * @param dataAll 所有的数据
     * @param id id
     * @param parentId parentId
     * @param <T>
     * @return
     */
    public static <T> JSONArray treeWhere(List<T> data, List<T> dataAll, String id, String parentId) {
        JSONArray resultData = new JSONArray();
        if (data.size() == dataAll.size()) {
            resultData.addAll(data);
            return resultData;
        }
        List<T> dataListAll = new ArrayList<>();
        CollectionUtils.addAll(dataListAll, dataAll);
        dataListAll.removeAll(data);
        for (int i = 0; i < data.size(); i++) {
            T entity = data.get(i);
            JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(entity));
            String firstParentId = json.getString(parentId);
            if (resultData.stream().filter(t -> t.equals(json)).count() == 0) {
                resultData.add(entity);
            }
            if (!"-1".equals(firstParentId)) {
                ParentData(dataListAll, json, resultData, id, parentId);
            }
        }
        return resultData;
    }

    /**
     * 递归查询父节点
     *
     * @param data 条件的的数据
     * @param dataAll 所有的数据
     * @param <T>
     * @return
     */
    public static <T> JSONArray treeWhere(List<T> data, List<T> dataAll) {
        String id = "id";
        String parentId = "parentId";
        return treeWhere(data, dataAll, id, parentId);
    }

    /**
     * 递归查询父节点
     *
     * @param dataAll 所有数据
     * @param json 当前对象
     * @param resultData 结果数据
     * @param id id
     * @param parentId parentId
     * @param <T>
     * @return
     */
    private static <T> JSONArray ParentData(
            List<T> dataAll, JSONObject json, JSONArray resultData, String id, String parentId) {
        List<T> data = dataAll.stream()
                .filter(t -> JSONObject.parseObject(JSONObject.toJSONString(t))
                        .get(id)
                        .equals(json.getString(parentId)))
                .toList();
        dataAll.removeAll(data);
        for (int i = 0; i < data.size(); i++) {
            T entity = data.get(i);
            JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(entity));
            String parentIds = object.getString(parentId);
            if (resultData.stream().filter(t -> t.equals(object)).count() == 0) {
                resultData.add(entity);
            }
            if ("-1".equals(parentIds)) {
                break;
            }
            ParentData(dataAll, object, resultData, id, parentId);
        }
        return resultData;
    }

    /**
     * 递归查询子节点
     *
     * @param dataAll 所有的数据
     * @param id id
     * @param parentId parentId
     * @param fid 查询的父亲节点
     * @param <T>
     * @return
     */
    public static <T> JSONArray treeWhere(String fid, List<T> dataAll, String id, String parentId) {
        JSONArray resultData = new JSONArray();
        List<T> data = dataAll.stream()
                .filter(t -> JSONObject.parseObject(JSONObject.toJSONString(t))
                        .get(parentId)
                        .equals(fid))
                .toList();
        List<T> dataListAll = new ArrayList<>();
        CollectionUtils.addAll(dataListAll, dataAll);
        dataListAll.removeAll(data);
        for (int i = 0; i < data.size(); i++) {
            T entity = data.get(i);
            JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(entity));
            String fId = json.getString(id);
            String fParentId = json.getString(parentId);
            if (fid.equals(fParentId)) {
                resultData.add(entity);
                ChildData(fId, dataListAll, resultData, id, parentId);
            }
        }
        return resultData;
    }

    /**
     * 递归查询子节点
     *
     * @param data 所有的数据
     * @param fid 查询的父亲节点
     * @param <T>
     * @return
     */
    public static <T> JSONArray treeWhere(String fid, List<T> data) {
        String id = "id";
        String parentId = "parentId";
        return treeWhere(fid, data, id, parentId);
    }

    /**
     * 递归查询子节点
     *
     * @param dataAll 所有的数据
     * @param id F_Id
     * @param parentId F_ParentId
     * @param fid 查询的父亲节点
     * @param <T>
     * @return
     */
    public static <T> JSONArray ChildData(
            String fid, List<T> dataAll, JSONArray resultData, String id, String parentId) {
        List<T> data = dataAll.stream()
                .filter(t -> JSONObject.parseObject(JSONObject.toJSONString(t))
                        .get(parentId)
                        .equals(fid))
                .toList();
        dataAll.removeAll(data);
        for (int i = 0; i < data.size(); i++) {
            T entity = data.get(i);
            JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(entity));
            String fId = json.getString(id);
            String fParentId = json.getString(parentId);
            if (fid.equals(fParentId)) {
                resultData.add(entity);
                ChildData(fId, dataAll, resultData, id, parentId);
            }
        }
        return resultData;
    }
}
