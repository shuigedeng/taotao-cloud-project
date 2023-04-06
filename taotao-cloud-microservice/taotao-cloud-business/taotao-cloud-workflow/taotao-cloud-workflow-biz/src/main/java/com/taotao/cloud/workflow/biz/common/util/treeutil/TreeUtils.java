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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** 操作“树”的工具 */
public class TreeUtils {

    // --------------------扩展-------------------------------
    /**
     * 将List转换为Tree
     *
     * @param tList @MethosName convertListToTreevo
     */
    public static <T extends TreeModel> List<TreeModel<T>> listToTreeVO(List<T> tList) {
        List<TreeModel<T>> treeModelList = new ArrayList<>();
        if (tList != null && tList.size() > 0) {
            for (T t : tList) {
                if (!isTreevoExist(tList, t.getParentId())) {
                    // 不存在以父ID为ID的点，说明是当前点是顶级节点
                    TreeModel<T> tTreeModel = getTreevoByT(t, tList);
                    treeModelList.add(tTreeModel);
                }
            }
        }
        return treeModelList;
    }

    /**
     * 根据ID判断该点是否存在
     *
     * @param tList
     * @param id 点ID
     * @return java.lang.Boolean @MethosName isTreevoExist
     */
    private static <T extends TreeModel> Boolean isTreevoExist(List<T> tList, String id) {
        for (T t : tList) {
            if (t.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取指定父点的子树
     *
     * @param parentTreeModel 父点
     * @param tList @MethosName getChildTreeList
     */
    private static <T extends TreeModel> List<TreeModel<T>> getChildTreevoList(
            TreeModel<T> parentTreeModel, List<T> tList) {
        List<TreeModel<T>> childTreeModelList = new ArrayList<>();
        for (T t : tList) {
            if (parentTreeModel.getId().equals(t.getParentId())) {
                // 如果父ID是传递树点的ID，那么就是传递树点的子点
                TreeModel<T> tTreeModel = getTreevoByT(t, tList);
                childTreeModelList.add(tTreeModel);
            }
        }
        return childTreeModelList;
    }

    /**
     * 根据实体获取Treevo
     *
     * @param t
     * @param tList
     * @return pri.xiaowd.layui.pojo.Treevo<T> @MethosName getTreevoByT
     */
    private static <T extends TreeModel> TreeModel<T> getTreevoByT(T t, List<T> tList) {
        TreeModel<T> tTreeModel = new TreeModel<>();
        tTreeModel.setId(t.getId());
        tTreeModel.setParentId(t.getParentId());
        tTreeModel.setFullName(t.getFullName());
        tTreeModel.setIcon(t.getIcon());
        List<TreeModel<T>> children = getChildTreevoList(tTreeModel, tList);
        tTreeModel.setHasChildren(children.size() == 0 ? false : true);
        tTreeModel.setChildren(children);
        return tTreeModel;
    }

    /**
     * 将TreeList的所有点转换为ID的Set集合
     *
     * @param treeModelList
     * @param kClass ID的类型 @MethosName convertTreevoToIdSet
     */
    public static <T extends TreeModel, K> Set<K> convertTreevoToIdSet(
            List<TreeModel<T>> treeModelList, Class<K> kClass) {
        Set<K> idSet = new HashSet<>(16);
        if (treeModelList != null && treeModelList.size() > 0) {
            for (TreeModel<T> treeModel : treeModelList) {
                idSet.add((K) treeModel.getId());
                if (treeModel.getChildren() != null && treeModel.getChildren().size() > 0) {
                    idSet.addAll(convertTreevoToIdSet(treeModel.getChildren(), kClass));
                }
            }
        }
        return idSet;
    }

    // -------------不扩展-------------------------------
    /**
     * 将List转换为Tree
     *
     * @param tList @MethosName convertListToTreevo
     */
    public static List<TreeModel> listToTree(List<TreeModel> tList) {
        List<TreeModel> treeModelList = new ArrayList<>();
        if (tList != null && tList.size() > 0) {
            for (TreeModel t : tList) {
                if (!isTreevoExist(tList, t.getParentId())) {
                    // 不存在以父ID为ID的点，说明是当前点是顶级节点
                    TreeModel tTreeModel = getTreeByT(t, tList);
                    treeModelList.add(tTreeModel);
                }
            }
        }
        return treeModelList;
    }

    /**
     * 获取指定父点的子树
     *
     * @param parentTreeModel 父点
     * @param tList @MethosName getChildTreeList
     */
    private static List<TreeModel> getChildTreeList(TreeModel parentTreeModel, List<TreeModel> tList) {
        List<TreeModel> childTreeModelList = new ArrayList<>();
        for (TreeModel t : tList) {
            if (parentTreeModel.getId().equals(t.getParentId())) {
                // 如果父ID是传递树点的ID，那么就是传递树点的子点
                TreeModel tTreeModel = getTreevoByT(t, tList);
                childTreeModelList.add(tTreeModel);
            }
        }
        return childTreeModelList;
    }

    /**
     * 根据实体获取Treevo
     *
     * @param t
     * @param tList
     * @return pri.xiaowd.layui.pojo.Treevo<T> @MethosName getTreevoByT
     */
    private static TreeModel getTreeByT(TreeModel t, List<TreeModel> tList) {
        TreeModel tTreeModel = new TreeModel<>();
        tTreeModel.setId(t.getId());
        tTreeModel.setParentId(t.getParentId());
        tTreeModel.setFullName(t.getFullName());
        tTreeModel.setIcon(t.getIcon());
        List<TreeModel> children = getChildTreeList(tTreeModel, tList);
        tTreeModel.setHasChildren(children.size() == 0 ? false : true);
        tTreeModel.setChildren(children);
        return tTreeModel;
    }
}
