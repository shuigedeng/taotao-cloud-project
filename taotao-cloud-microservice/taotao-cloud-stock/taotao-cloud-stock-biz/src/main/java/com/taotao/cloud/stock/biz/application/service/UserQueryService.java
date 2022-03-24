package com.taotao.cloud.stock.biz.application.service;

import com.xtoon.cloud.common.mybatis.util.Page;
import com.xtoon.cloud.sys.application.dto.UserDTO;

import java.util.Map;

/**
 * 用户查询服务接口
 *
 * @author shuigedeng
 * @date 2021-05-10
 **/
public interface UserQueryService {

    /**
     * 分页查询
     *
     * @param params
     * @return
     */
    Page queryPage(Map<String, Object> params);

    /**
     * 通过用户ID获取用户
     *
     * @param userId
     * @return
     */
    UserDTO find(String userId);
}
