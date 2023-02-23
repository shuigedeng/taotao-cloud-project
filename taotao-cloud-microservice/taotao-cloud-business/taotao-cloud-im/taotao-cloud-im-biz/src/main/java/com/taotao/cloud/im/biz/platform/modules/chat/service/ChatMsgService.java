package com.taotao.cloud.im.biz.platform.modules.chat.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatMsg;
import com.platform.modules.chat.vo.ChatVo01;
import com.platform.modules.chat.vo.ChatVo02;
import com.platform.modules.chat.vo.ChatVo03;
import com.platform.modules.push.vo.PushBodyVo;

/**
 * <p>
 * 聊天消息 服务层
 * q3z3
 * </p>
 */
public interface ChatMsgService extends BaseService<ChatMsg> {

    /**
     * 发送消息
     */
    ChatVo03 sendFriendMsg(ChatVo01 chatVo);

    /**
     * 发送群消息
     */
    ChatVo03 sendGroupMsg(ChatVo02 chatVo);

    /**
     * 获取大消息
     */
    PushBodyVo getBigMsg(String msgId);

}
