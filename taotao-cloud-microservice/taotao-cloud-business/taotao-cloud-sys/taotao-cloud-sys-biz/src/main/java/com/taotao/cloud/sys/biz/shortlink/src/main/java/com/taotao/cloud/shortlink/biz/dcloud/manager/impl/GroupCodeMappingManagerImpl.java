package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.dcloud.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.ShortLinkStateEnum;
import net.xdclass.manager.GroupCodeMappingManager;
import net.xdclass.mapper.GroupCodeMappingMapper;
import net.xdclass.model.GroupCodeMappingDO;
import net.xdclass.vo.GroupCodeMappingVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author:刘森飚
 **/

@Component
@Slf4j
public class GroupCodeMappingManagerImpl implements GroupCodeMappingManager {

    @Autowired
    private GroupCodeMappingMapper groupCodeMappingMapper;


    /**
     * 查找详情
     * @param mappingId
     * @param accountNo
     * @param groupId
     * @return
     */
    @Override
    public GroupCodeMappingDO findByGroupIdAndMappingId(Long mappingId, Long accountNo, Long groupId) {

        GroupCodeMappingDO groupCodeMappingDO = groupCodeMappingMapper.selectOne(
                new QueryWrapper<GroupCodeMappingDO>()
                .eq("id", mappingId)
                .eq("account_no", accountNo)
                .eq("group_id", groupId));

        return groupCodeMappingDO;
    }


    /**
     * 新增
     * @param groupCodeMappingDO
     * @return
     */
    @Override
    public int add(GroupCodeMappingDO groupCodeMappingDO) {
        return groupCodeMappingMapper.insert(groupCodeMappingDO);
    }


    /**
     * 冗余双写B端-删除操作
     * @param groupCodeMappingDO
     * @return
     */
    @Override
    public int del(GroupCodeMappingDO groupCodeMappingDO) {

        int rows = groupCodeMappingMapper.update(null, new UpdateWrapper<GroupCodeMappingDO>()
                .eq("id", groupCodeMappingDO.getId())
                .eq("account_no", groupCodeMappingDO.getAccountNo())
                .eq("group_id", groupCodeMappingDO.getGroupId())
                .set("del", 1)
        );
        return rows;
    }


    /**
     * 分页查找
     * @param page
     * @param size
     * @param accountNo
     * @param groupId
     * @return
     */
    @Override
    public Map<String, Object> pageShortLinkByGroupId(Integer page, Integer size, Long accountNo, Long groupId) {

        Page<GroupCodeMappingDO> pageInfo = new Page<>(page, size);

        Page<GroupCodeMappingDO> groupCodeMappingDOPage = groupCodeMappingMapper.
                selectPage(pageInfo, new QueryWrapper<GroupCodeMappingDO>()
                        .eq("account_no", accountNo)
                .eq("group_id", groupId)
                .eq("del",0)
                        .orderByDesc("gmt_create"));

        Map<String, Object> pageMap = new HashMap<>(3);

        pageMap.put("total_record", groupCodeMappingDOPage.getTotal());
        pageMap.put("total_page", groupCodeMappingDOPage.getPages());
        pageMap.put("current_data", groupCodeMappingDOPage.getRecords()
                .stream().map(obj -> beanProcess(obj)).toList());

        return pageMap;
    }


    /**
     * 更新短链码状态
     * @param accountNo
     * @param groupId
     * @param shortLinkCode
     * @param shortLinkStateEnum
     * @return
     */
    @Override
    public int updateGroupCodeMappingState(Long accountNo, Long groupId, String shortLinkCode, ShortLinkStateEnum shortLinkStateEnum) {

        int rows = groupCodeMappingMapper.update(null,
                new UpdateWrapper<GroupCodeMappingDO>()
                .eq("code", shortLinkCode)
                .eq("account_no", accountNo)
                .eq("group_id", groupId).eq("del",0)
                .set("state", shortLinkStateEnum.name()));

        return rows;
    }


    /**
     * 查找是否存在
     * @param shortLinkCode
     * @param groupId
     * @param accountNo
     * @return
     */
    @Override
    public GroupCodeMappingDO findByCodeAndGroupId(String shortLinkCode, Long groupId, long accountNo) {
        GroupCodeMappingDO groupCodeMappingDO = groupCodeMappingMapper.selectOne(new QueryWrapper<GroupCodeMappingDO>()
                .eq("code", shortLinkCode)
                .eq("account_no", accountNo)
                .eq("del",0)
                .eq("group_id", groupId));
        return groupCodeMappingDO;
    }



    /**
     * 冗余双写-更新操作
     * @param groupCodeMappingDO
     * @return
     */
    @Override
    public int update(GroupCodeMappingDO groupCodeMappingDO) {
        int rows = groupCodeMappingMapper.update(null, new UpdateWrapper<GroupCodeMappingDO>()
                .eq("id", groupCodeMappingDO.getId())
                .eq("account_no", groupCodeMappingDO.getAccountNo())
                .eq("group_id", groupCodeMappingDO.getGroupId())
                .eq("del", 0)
                .set("title", groupCodeMappingDO.getTitle())
                .set("domain", groupCodeMappingDO.getDomain())
        );
        return rows;
    }


    private GroupCodeMappingVO beanProcess(GroupCodeMappingDO groupCodeMappingDO) {
        GroupCodeMappingVO groupCodeMappingVO = new GroupCodeMappingVO();
        BeanUtils.copyProperties(groupCodeMappingDO, groupCodeMappingVO);

        return groupCodeMappingVO;
    }
}
