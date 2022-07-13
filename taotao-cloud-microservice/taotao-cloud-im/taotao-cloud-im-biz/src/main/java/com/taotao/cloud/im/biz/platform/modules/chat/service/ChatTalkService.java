package com.taotao.cloud.im.biz.platform.modules.chat.service;

import com.platform.modules.chat.vo.FriendVo06;
import com.platform.modules.chat.vo.FriendVo07;
import com.platform.modules.push.vo.PushParamVo;

import java.util.List;

/**
 * <p>
 * 系统聊天 服务层
 * q3z3
 * </p>
 */
public interface ChatTalkService {

    /**
     * 查询好友列表
     */
    List<FriendVo06> queryFriendList();

    /**
     * 查询好友详情
     */
    FriendVo07 queryFriendInfo(Long userId);

    /**
     * 发送聊天
     */
    PushParamVo talk(Long userId, String content);

}
