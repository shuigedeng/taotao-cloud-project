package com.taotao.cloud.workflow.api.common.util;

import java.util.List;

/**
 *
 */
public class PageUtil {
    /**
     * 自定义分页
     */
    public static List getListPage(int page, int pageSize, List list) {
        if (list == null || list.size() == 0) {
            return list;
        }
        int totalCount = list.size();
        page = page - 1;
        int fromIndex = page * pageSize;
        if (fromIndex >= totalCount) {
            return list;
        }
        int toIndex = ((page + 1) * pageSize);
        if (toIndex > totalCount) {
            toIndex = totalCount;
        }
        return list.subList(fromIndex, toIndex);
    }
}
