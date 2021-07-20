package com.taotao.cloud.standalone.common.utils;

import cn.hutool.core.util.PageUtil;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname PageUtil
 * @Description TODO
 * @Author shuigedeng
 * @since 2019-07-18 16:23
 * @Version 1.0
 */
@UtilityClass
public class PrePageUtil extends PageUtil {

    /**
     * List 分页
     * @param page
     * @param size
     * @param list
     * @return
     */
    public List toPage(int page, int size , List list) {
        int fromIndex = page * size;
        int toIndex = page * size + size;

        if(fromIndex > list.size()){
            return new ArrayList();
        } else if(toIndex >= list.size()) {
            return list.subList(fromIndex,list.size());
        } else {
            return list.subList(fromIndex,toIndex);
        }
    }

}
