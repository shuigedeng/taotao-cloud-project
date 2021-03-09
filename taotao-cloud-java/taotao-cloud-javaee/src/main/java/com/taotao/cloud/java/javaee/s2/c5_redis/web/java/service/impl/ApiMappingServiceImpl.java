package com.taotao.cloud.java.javaee.s2.c5_redis.web.java.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qianfeng.openapi.web.master.mapper.ApiMappingMapper;
import com.qianfeng.openapi.web.master.pojo.ApiMapping;
import com.qianfeng.openapi.web.master.service.ApiMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ApiMappingServiceImpl implements ApiMappingService {
    @Autowired
    private ApiMappingMapper apiMappingMapper;

    private final String GATEWAY_REDIS_KEY = "APINAME:";

    @Override
    public void addApiMapping(ApiMapping mapping) {
        apiMappingMapper.addApiMapping(mapping);
        // cacheService.hmset(GATEWAY_REDIS_KEY + mapping.get("gatewayApiName"), mapping);
    }

    @Override
    public void updateApiMapping(ApiMapping mapping) {
        apiMappingMapper.updateApiMapping(mapping);
        //cacheService.hmset(GATEWAY_REDIS_KEY + mapping.get("gatewayApiName"), mapping);
    }

    @Override
    public PageInfo<ApiMapping> getMappingList(ApiMapping criteria, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return new PageInfo<>(apiMappingMapper.getMappingList(criteria));
    }

    @Override
    public ApiMapping getMappingById(int id) {
        return apiMappingMapper.getMappingById(id);
    }

    @Override
    public void deleteMapping(int[] ids) {
        if (ids == null || ids.length == 0) {
            return;
        }
        for (int id : ids) {
            ApiMapping mapping = apiMappingMapper.getMappingById(id);
            if (mapping != null) {
                mapping.setState(0);
                apiMappingMapper.updateApiMapping(mapping);
                //cacheService.del(GATEWAY_REDIS_KEY + mapping.getGatewayApiName());
            }
        }

    }
}
