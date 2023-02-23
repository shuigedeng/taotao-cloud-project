package com.taotao.cloud.im.biz.platform.modules.push.service;

import com.platform.modules.push.enums.PushMsgTypeEnum;
import com.platform.modules.push.enums.PushNoticeTypeEnum;
import com.platform.modules.push.vo.PushParamVo;

import java.util.List;

/**
 * <p>
 * 用户推送 服务层
 * q3z3
 * </p>
 */
public interface ChatPushService {

    /**
     * 注册别名
     */
    void setAlias(Long userId, String cid);

    /**
     * 解除别名
     */
    void delAlias(Long userId, String cid);

    /**
     * 发送消息
     */
    void pushMsg(PushParamVo from, PushMsgTypeEnum msgType);

    /**
     * 发送消息
     */
    void pushMsg(List<PushParamVo> userList, PushMsgTypeEnum msgType);

    /**
     * 发送消息
     */
    void pushMsg(List<PushParamVo> userList, PushParamVo group, PushMsgTypeEnum msgType);

    /**
     * 拉取离线消息
     */
    void pullOffLine(Long userId);

    /**
     * 发送通知
     */
    void pushNotice(PushParamVo paramVo, PushNoticeTypeEnum pushNoticeType);

    /**
     * 发送通知
     */
    void pushNotice(List<PushParamVo> userList, PushNoticeTypeEnum pushNoticeType);

}
