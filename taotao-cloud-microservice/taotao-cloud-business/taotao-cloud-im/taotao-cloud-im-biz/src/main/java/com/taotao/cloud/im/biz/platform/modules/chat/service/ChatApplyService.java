package com.taotao.cloud.im.biz.platform.modules.chat.service;

import com.github.pagehelper.PageInfo;
import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatApply;
import com.platform.modules.chat.enums.ApplySourceEnum;
import com.platform.modules.chat.vo.ApplyVo03;

/**
 * <p>
 * 好友申请表 服务层
 * q3z3
 * </p>
 */
public interface ChatApplyService extends BaseService<ChatApply> {

    /**
     * 申请好友
     */
    void applyFriend(Long acceptId, ApplySourceEnum source, String reason);

    /**
     * 申请加群
     */
    void applyGroup(Long acceptId);

    /**
     * 申请记录
     */
    PageInfo list();

    /**
     * 查询详情
     */
    ApplyVo03 getInfo(Long applyId);
}
