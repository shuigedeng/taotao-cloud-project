package com.taotao.cloud.standalone.system.modules.data.strategy;

import com.taotao.cloud.standalone.system.modules.data.enums.DataScopeTypeEnum;
import com.taotao.cloud.standalone.system.modules.sys.dto.RoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Classname DataScopeContext
 * @Description 创建环境角色Context:
 * @Author Created by Lihaodong (alias:小东啊) lihaodongmail@163.com
 * @since 2019-06-08 16:11
 * @Version 1.0
 */
@Service
public class DataScopeContext {

    @Autowired
    private final Map<String, AbstractDataScopeHandler> strategyMap = new ConcurrentHashMap<>();

    /**
     * Component里边的1是指定其名字，这个会作为key放到strategyMap里
     * @param strategyMap
     */
    public DataScopeContext(Map<String, AbstractDataScopeHandler> strategyMap) {
        strategyMap.forEach(this.strategyMap::put);
    }

    public List<Integer> getDeptIdsForDataScope(RoleDTO roleDto, Integer type) {
        return strategyMap.get(String.valueOf(type)).getDeptIds(roleDto, DataScopeTypeEnum.valueOf(type));
    }
}
