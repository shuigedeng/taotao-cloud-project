package com.taotao.cloud.standalone.system.modules.data.strategy;


import com.taotao.cloud.standalone.system.modules.data.enums.DataScopeTypeEnum;
import com.taotao.cloud.standalone.system.modules.sys.dto.RoleDTO;

import java.util.List;

/**
 * @Classname AbstractDataScopeHandler
 * @Description 创建抽象策略角色 主要作用 数据权限范围使用
 * @Author Created by Lihaodong (alias:小东啊) lihaodongmail@163.com
 * @since 2019-06-08 15:45
 * @Version 1.0
 */

public interface AbstractDataScopeHandler {

    /**
     * @param roleDto
     * @param dataScopeTypeEnum
     * @return
     */
    List<Integer> getDeptIds(RoleDTO roleDto, DataScopeTypeEnum dataScopeTypeEnum);
}
