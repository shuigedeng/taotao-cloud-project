package com.taotao.cloud.standalone.system.modules.data.strategy;

import com.taotao.cloud.standalone.security.util.SecurityUtil;
import com.taotao.cloud.standalone.system.modules.data.enums.DataScopeTypeEnum;
import com.taotao.cloud.standalone.system.modules.sys.dto.RoleDTO;
import com.taotao.cloud.standalone.system.modules.sys.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname ThisLevelHandler
 * @Description 本级
 * @Author Created by Lihaodong (alias:小东啊) lihaodongmail@163.com
 * @since 2019-06-08 15:44
 * @Version 1.0
 */
@Component("2")
public class ThisLevelDataScope implements AbstractDataScopeHandler {

    @Autowired
    private ISysUserService userService;

    @Override
    public List<Integer> getDeptIds(RoleDTO roleDto, DataScopeTypeEnum dataScopeTypeEnum) {
        // 用于存储部门id
        List<Integer> deptIds = new ArrayList<>();
        deptIds.add(userService.findByUserInfoName(SecurityUtil.getUser().getUsername()).getDeptId());
        return deptIds;
    }
}
