package com.taotao.cloud.im.biz.platform.modules.chat.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatFeedback;
import com.platform.modules.chat.vo.MyVo04;

/**
 * <p>
 * 建议反馈 服务层
 * q3z3
 * </p>
 */
public interface ChatFeedbackService extends BaseService<ChatFeedback> {

    /**
     * 添加建议反馈
     */
    void addFeedback(MyVo04 myVo);

}
