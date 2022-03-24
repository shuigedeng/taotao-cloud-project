package com.taotao.cloud.stock.biz.application.service;

import com.xtoon.cloud.sys.application.command.RoleCommand;

import java.util.List;

/**
 * 角色应用服务接口
 *
 * @author shuigedeng
 * @date 2021-02-17
 **/
public interface RoleApplicationService {

    /**
     * 保存或更新
     *
     * @param roleCommand
     */
    void saveOrUpdate(RoleCommand roleCommand);

    /**
     * 批量删除
     *
     * @param ids
     */
    void deleteBatch(List<String> ids);

    /**
     * 禁用
     *
     * @param id
     */
    void disable(String id);
}
