package com.taotao.cloud.im.biz.platform.modules.chat.service;

import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatGroupInfo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务层
 * q3z3
 * </p>
 */
public interface ChatGroupInfoService extends BaseService<ChatGroupInfo> {

    /**
     * 查询详情
     */
    ChatGroupInfo getGroupInfo(Long groupId, Long userId, YesOrNoEnum verify);

    /**
     * 删除缓存
     */
    void delGroupInfoCache(Long groupId, List<Long> userList);

    /**
     * 查询数量
     */
    Long countByGroup(Long groupId);

    /**
     * 查询用户id
     */
    List<Long> queryUserList(Long groupId);

    /**
     * 查询用户id
     */
    List<ChatGroupInfo> queryUserList(Long groupId, List<Long> userList);

    /**
     * 查询用户id
     */
    Map<Long, ChatGroupInfo> queryUserMap(Long groupId);

    /**
     * 通过群组删除
     */
    void delByGroup(Long groupId);

}
