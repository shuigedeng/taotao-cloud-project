package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.dcloud.service;

import net.xdclass.controller.request.LinkGroupAddRequest;
import net.xdclass.controller.request.LinkGroupUpdateRequest;
import net.xdclass.vo.LinkGroupVO;

import java.util.List;

/**
 * @author 刘森飚
 * @since 2023-01-18
 */

public interface LinkGroupService {

    /**
     * 新增分组
     * @param addRequest
     * @return
     */
    int add(LinkGroupAddRequest addRequest);

    /**
     * 删除分组
     * @param groupId
     * @return
     */
    int del(Long groupId);

    /**
     * 详情
     * @param groupId
     * @return
     */
    LinkGroupVO detail(Long groupId);

    /**
     * 列出用户全部分组
     * @return
     */
    List<LinkGroupVO> listAllGroup();

    /**
     * 更新组名
     * @param request
     * @return
     */
    int updateById(LinkGroupUpdateRequest request);
}
