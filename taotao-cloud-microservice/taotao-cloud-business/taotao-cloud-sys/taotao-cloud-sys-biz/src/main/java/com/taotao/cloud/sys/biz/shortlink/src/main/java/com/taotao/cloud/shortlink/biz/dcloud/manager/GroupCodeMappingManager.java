package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.dcloud.manager;

import net.xdclass.enums.ShortLinkStateEnum;
import net.xdclass.model.GroupCodeMappingDO;

import java.util.Map;

/**
 * @author 刘森飚
 * @since 2023-01-22
 */
public interface GroupCodeMappingManager {

    /**
     * 查找详情
     * @param mappingId
     * @param accountNo
     * @param groupId
     * @return
     */
    GroupCodeMappingDO findByGroupIdAndMappingId(Long mappingId, Long accountNo, Long groupId);


    /**
     * 新增
     * @param groupCodeMappingDO
     * @return
     */
    int add(GroupCodeMappingDO groupCodeMappingDO);


    /**
     * 冗余双写B端-删除操作
     * @param groupCodeMappingDO
     * @return
     */
    int del(GroupCodeMappingDO groupCodeMappingDO);


    /**
     * 分页查找
     * @param page
     * @param size
     * @param accountNo
     * @param groupId
     * @return
     */
    Map<String,Object> pageShortLinkByGroupId(Integer page, Integer size, Long accountNo, Long groupId);


    /**
     * 更新短链码状态
     * @param accountNo
     * @param groupId
     * @param shortLinkCode
     * @param shortLinkStateEnum
     * @return
     */
    int updateGroupCodeMappingState(Long accountNo, Long groupId, String shortLinkCode, ShortLinkStateEnum shortLinkStateEnum);


    /**
     * 查找是否存在
     * @param shortLinkCode
     * @param groupId
     * @param accountNo
     * @return
     */
    GroupCodeMappingDO findByCodeAndGroupId(String shortLinkCode, Long groupId, long accountNo);


    /**
     * 冗余双写B端-更新操作
     * @param groupCodeMappingDO
     * @return
     */
    int update(GroupCodeMappingDO groupCodeMappingDO);
}
