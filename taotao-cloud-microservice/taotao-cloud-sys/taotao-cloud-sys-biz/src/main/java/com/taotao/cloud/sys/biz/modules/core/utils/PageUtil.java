package com.taotao.cloud.sys.biz.modules.core.utils;


import java.util.List;
import java.util.stream.Collectors;

public class PageUtil {

    /**
     * 对数据进行分页
     * @param datas 数据列表
     * @param pageParam 分页参数
     * @param <T>
     * @return
     */
    public static <T> List<T> splitListByPageParam(List<T> datas, PageParam pageParam){
        if (pageParam.getPageNo() < 1){
            pageParam.setPageNo("1");
        }
        final int totalPage = (datas.size() - 1) / pageParam.getPageSize() + 1;
        if (pageParam.getPageNo() > totalPage){
            pageParam.setPageNo(totalPage+"");
        }
        long startIndex = (pageParam.getPageNo() - 1) * pageParam.getPageSize();
        return datas.stream().skip(startIndex).limit(pageParam.getPageSize()).collect(Collectors.toList());
    }
}
