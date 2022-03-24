package com.taotao.cloud.stock.biz.application.service;

import com.xtoon.cloud.common.mybatis.util.Page;
import com.xtoon.cloud.sys.application.dto.RoleDTO;

import java.util.List;
import java.util.Map;

/**
 * 角色查询服务接口
 *
 * @author shuigedeng
 * @date 2021-05-10
 **/
public interface RoleQueryService {

    /**
     * 分页查询
     *
     * @param params
     * @return
     */
    Page queryPage(Map<String, Object> params);

    /**
     * 查询列表
     *
     * @return
     */
    List<RoleDTO> listAll();

    /**
     * 通过ID获取
     *
     * @param id
     * @return
     */
    RoleDTO getById(String id);
}
