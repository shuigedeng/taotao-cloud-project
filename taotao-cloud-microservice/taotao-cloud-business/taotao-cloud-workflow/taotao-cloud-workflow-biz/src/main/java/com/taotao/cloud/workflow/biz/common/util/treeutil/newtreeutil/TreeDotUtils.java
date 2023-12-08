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

package com.taotao.cloud.workflow.biz.common.util.treeutil.newtreeutil;

import com.taotao.cloud.workflow.biz.common.util.treeutil.SumTree;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;

/***
 * 树工具
 */
public class TreeDotUtils {

    /** 将List转换为Tree */
    public static <T extends SumTree> List<SumTree<T>> convertListToTreeDot(List<T> tList, String parentId) {
        List<SumTree<T>> sumTrees = new ArrayList<>();
        List<T> list = new ArrayList<>();
        CollectionUtils.addAll(list, tList);
        if (StringUtil.isNotEmpty(parentId)) {
            List<T> data =
                    list.stream().filter(t -> parentId.equals(t.getParentId())).toList();
            list.removeAll(data);
            for (int i = 0; i < data.size(); i++) {
                T t = data.get(i);
                if (!isTreeDotExist(list, t.getParentId())) {
                    SumTree<T> tSumTree = getTreeDotByT(t, list);
                    sumTrees.add(tSumTree);
                }
            }
        }
        return sumTrees;
    }

    /** 将List转换为Tree */
    public static <T extends SumTree> List<SumTree<T>> convertListToTreeDot(List<T> tList) {
        List<SumTree<T>> sumTrees = new ArrayList<>();
        if (tList != null && tList.size() > 0) {
            for (int i = 0; i < tList.size(); i++) {
                T t = tList.get(i);
                if (!isTreeDotExist(tList, t.getParentId())) {
                    // 不存在以父ID为ID的点，说明是当前点是顶级节点
                    SumTree<T> tSumTree = getTreeDotByT(t, tList);
                    sumTrees.add(tSumTree);
                }
            }
        }
        return sumTrees;
    }

    /** 将List转换为Tree（个别过滤子集） */
    public static <T extends SumTree> List<SumTree<T>> convertListToTreeDotFilter(List<T> tList) {
        List<SumTree<T>> sumTrees = new ArrayList<>();
        if (tList != null && tList.size() > 0) {
            for (int i = 0; i < tList.size(); i++) {
                T t = tList.get(i);
                if (!isTreeDotExist(tList, t.getParentId())) {
                    // 不存在以父ID为ID的点，说明是当前点是顶级节点
                    SumTree<T> tSumTree = getTreeDotByT(t, tList);
                    if ("-1".equals(tSumTree.getParentId()) || "0".equals(tSumTree.getParentId())) {
                        sumTrees.add(tSumTree);
                    }
                }
            }
        }
        return sumTrees;
    }

    /**
     * 根据ID判断该点是否存在
     *
     * @param tList
     * @param id 点ID
     */
    private static <T extends SumTree> Boolean isTreeDotExist(List<T> tList, String id) {
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
     * @param parentTreeDot 父点
     * @param tList
     */
    private static <T extends SumTree> List<SumTree<T>> getChildTreeDotList(SumTree<T> parentTreeDot, List<T> tList) {
        List<SumTree<T>> childTreeDotList = new ArrayList<>();
        List<T> data = tList.stream()
                .filter(t -> parentTreeDot.getId().equals(t.getParentId()))
                .toList();
        for (T t : data) {
            if (parentTreeDot.getId().equals(t.getParentId())) {
                // 如果父ID是传递树点的ID，那么就是传递树点的子点
                SumTree<T> tSumTree = getTreeDotByT(t, tList);
                childTreeDotList.add(tSumTree);
            }
        }
        return childTreeDotList;
    }

    /**
     * 根据实体获取TreeDot
     *
     * @param t
     * @param tList
     *     2020/5/4 22:17
     */
    private static <T extends SumTree> SumTree<T> getTreeDotByT(T t, List<T> tList) {
        SumTree<T> sumTree = t;
        List<SumTree<T>> children = getChildTreeDotList(sumTree, tList);
        sumTree.setHasChildren(children.size() == 0 ? false : true);
        if (children.size() == 0) {
            children = null;
        }
        sumTree.setChildren(children);
        return sumTree;
    }
}
