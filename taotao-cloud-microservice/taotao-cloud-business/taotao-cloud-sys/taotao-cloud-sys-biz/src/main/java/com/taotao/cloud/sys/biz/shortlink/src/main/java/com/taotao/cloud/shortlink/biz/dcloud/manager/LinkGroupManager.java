package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.dcloud.manager;

import net.xdclass.model.LinkGroupDO;

import java.util.List;

public interface LinkGroupManager {

    /**
     * 新增
     * @param linkGroupDO
     * @return
     */
    int add(LinkGroupDO linkGroupDO);

    /**
     * 删除
     * @param groupId
     * @param accountNo
     * @return
     */
    int del(Long groupId, Long accountNo);

    /**
     * 获取详情
     * @param groupId
     * @param accountNo
     * @return
     */
    LinkGroupDO detail(Long groupId, long accountNo);

    /**
     * 获取所有组列表
     * @param accountNo
     * @return
     */
    List<LinkGroupDO> listAllGroup(long accountNo);

    /**
     * 更新
     * @param linkGroupDO
     * @return
     */
    int updateById(LinkGroupDO linkGroupDO);
}
