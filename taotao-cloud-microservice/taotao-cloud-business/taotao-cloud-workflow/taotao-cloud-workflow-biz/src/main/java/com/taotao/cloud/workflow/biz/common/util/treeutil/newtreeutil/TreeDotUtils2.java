package com.taotao.cloud.workflow.biz.common.util.treeutil.newtreeutil;


import com.taotao.cloud.workflow.biz.common.util.treeutil.SumTree2;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;

/**
 * 树工具
 *
 */
public class TreeDotUtils2 {


    /**
     * 将List转换为指定对象的Tree
     */
    public static <T extends SumTree2<T>> List<T> convertListToBeanTreeDot(List<T> tList, Class<T> clz) {
        List<T> SumTree2s = new ArrayList<>();

        if (tList != null && tList.size() > 0) {
            for (int i = 0; i < tList.size(); i++) {
                T t = tList.get(i);
                if (!isTreeDotExist(tList, t.getParentId())) {
                    //不存在以父ID为ID的点，说明是当前点是顶级节点
                    T tSumTree2 = getBeanTreeDotByT(t, tList, clz);
                    SumTree2s.add(tSumTree2);
                }
            }
        }
        return SumTree2s;
    }

    private static <T extends SumTree2<T>> T getBeanTreeDotByT(T t, List<T> tList, Class<T> clz) {
        List<T> children = getChildTreeDotList(t, tList, clz);
        if(children.size() != 0){
            t.setHasChildren(true);
            t.setChildren(children);
        }else {
            t.setHasChildren(false);
        }
        try {
            T s = clz.newInstance();
            BeanUtils.copyProperties(t, s);
            return s;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static <T extends SumTree2<T>> List<T> getChildTreeDotList(SumTree2<T> parentTreeDot, List<T> tList, Class<T> clz) {
        List<T> childTreeDotList = new ArrayList<>();
        List<T> data = tList.stream().filter(t -> parentTreeDot.getId().equals(t.getParentId())).collect(Collectors.toList());
        for (T t : data) {
            if (parentTreeDot.getId().equals(t.getParentId())) {
                //如果父ID是传递树点的ID，那么就是传递树点的子点
                T tSumTree2 = getBeanTreeDotByT(t, tList, clz);
                childTreeDotList.add(tSumTree2);
            }
        }
        return childTreeDotList;
    }

    private static <T extends SumTree2<T>> Boolean isTreeDotExist(List<T> tList, String id) {
        for (T t : tList) {
            if (t.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
}
