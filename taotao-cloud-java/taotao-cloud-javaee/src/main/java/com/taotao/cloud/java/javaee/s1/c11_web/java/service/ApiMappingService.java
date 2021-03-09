package com.taotao.cloud.java.javaee.s1.c11_web.java.service;

import com.github.pagehelper.PageInfo;
import com.qianfeng.openapi.web.master.pojo.ApiMapping;

public interface ApiMappingService {
    void addApiMapping(ApiMapping mapping);

    void updateApiMapping(ApiMapping mapping);

    PageInfo<ApiMapping> getMappingList(ApiMapping criteria, int page, int limit);

    ApiMapping getMappingById(int id);

    void deleteMapping(int[] ids);
}
