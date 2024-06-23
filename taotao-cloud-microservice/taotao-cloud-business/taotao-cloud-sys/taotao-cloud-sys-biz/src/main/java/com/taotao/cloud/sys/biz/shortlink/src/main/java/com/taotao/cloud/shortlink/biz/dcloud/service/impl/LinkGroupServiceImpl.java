package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.dcloud.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.controller.request.LinkGroupAddRequest;
import net.xdclass.controller.request.LinkGroupUpdateRequest;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.manager.LinkGroupManager;
import net.xdclass.model.LinkGroupDO;
import net.xdclass.service.LinkGroupService;
import net.xdclass.vo.LinkGroupVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 刘森飚
 * @since 2023-01-18
 */
@Service
@Slf4j
public class LinkGroupServiceImpl implements LinkGroupService {

    @Autowired
    private LinkGroupManager linkGroupManager;

    /**
     * 创建短链分组
     * @param addRequest
     * @return
     */
    @Override
    public int add(LinkGroupAddRequest addRequest) {
        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        LinkGroupDO linkGroupDO = new LinkGroupDO();
        linkGroupDO.setTitle(addRequest.getTitle());
        linkGroupDO.setAccountNo(accountNo);
        int rows = linkGroupManager.add(linkGroupDO);
        return rows;
    }


    /**
     * 删除短链分组
     * @param groupId
     * @return
     */
    @Override
    public int del(Long groupId) {
        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        return linkGroupManager.del(groupId,accountNo);
    }


    /**
     * 详情
     * @param groupId
     * @return
     */
    @Override
    public LinkGroupVO detail(Long groupId) {
        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        LinkGroupDO linkGroupDO = linkGroupManager.detail(groupId,accountNo);
        LinkGroupVO linkGroupVO = new LinkGroupVO();
        BeanUtils.copyProperties(linkGroupDO,linkGroupVO);
        return linkGroupVO;
    }


    /**
     * 列出用户全部分组
     * @return
     */
    @Override
    public List<LinkGroupVO> listAllGroup() {
        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        List<LinkGroupDO> linkGroupDOList = linkGroupManager.listAllGroup(accountNo);
        List<LinkGroupVO> groupVOList = linkGroupDOList.stream().map(obj -> {
            LinkGroupVO linkGroupVO = new LinkGroupVO();
            BeanUtils.copyProperties(obj, linkGroupVO);
            return linkGroupVO;
        }).toList();
        return groupVOList;
    }


    /**
     * 更新组名
     * @param request
     * @return
     */
    @Override
    public int updateById(LinkGroupUpdateRequest request) {
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        LinkGroupDO linkGroupDO = new LinkGroupDO();
        linkGroupDO.setTitle(request.getTitle());
        linkGroupDO.setId(request.getId());
        linkGroupDO.setAccountNo(accountNo);
        int rows = linkGroupManager.updateById(linkGroupDO);
        return rows;
    }
}
