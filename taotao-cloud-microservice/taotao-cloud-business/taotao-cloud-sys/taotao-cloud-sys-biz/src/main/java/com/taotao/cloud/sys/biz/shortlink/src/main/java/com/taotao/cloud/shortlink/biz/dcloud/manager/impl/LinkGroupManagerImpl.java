package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.dcloud.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.xdclass.manager.LinkGroupManager;
import net.xdclass.mapper.LinkGroupMapper;
import net.xdclass.model.LinkGroupDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description
 * @Author:刘森飚
 **/

@Component
public class LinkGroupManagerImpl implements LinkGroupManager {

    @Autowired
    private LinkGroupMapper linkGroupMapper;


    /**
     * 新增
     * @param linkGroupDO
     * @return
     */
    @Override
    public int add(LinkGroupDO linkGroupDO) {
        return linkGroupMapper.insert(linkGroupDO);
    }


    /**
     * 删除
     * @param groupId
     * @param accountNo
     * @return
     */
    @Override
    public int del(Long groupId, Long accountNo) {
        return linkGroupMapper.delete(new QueryWrapper<LinkGroupDO>().
                eq("id",groupId)
                .eq("account_no",accountNo));
    }



    /**
     * 获取详情
     * @param groupId
     * @param accountNo
     * @return
     */
    @Override
    public LinkGroupDO detail(Long groupId, long accountNo) {
        return linkGroupMapper.selectOne(new QueryWrapper<LinkGroupDO>()
        .eq("id",groupId)
        .eq("account_no",accountNo));
    }


    /**
     * 获取所有组列表
     * @param accountNo
     * @return
     */
    @Override
    public List<LinkGroupDO> listAllGroup(long accountNo) {

        return linkGroupMapper.selectList(new QueryWrapper<LinkGroupDO>()
                .eq("account_no",accountNo));
    }


    /**
     * 更新
     * @param linkGroupDO
     * @return
     */
    @Override
    public int updateById(LinkGroupDO linkGroupDO) {

        return linkGroupMapper.update(linkGroupDO,new QueryWrapper<LinkGroupDO>()
                .eq("id",linkGroupDO.getId())
                .eq("account_no",linkGroupDO.getAccountNo()));
    }
}
