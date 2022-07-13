package com.taotao.cloud.im.biz.platform.modules.chat.dao;

import com.platform.modules.chat.domain.ChatFeedback;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

/**
 * <p>
 * 建议反馈 数据库访问层
 * q3z3
 * </p>
 */
@Repository
public interface ChatFeedbackDao extends BaseDao<ChatFeedback> {

    /**
     * 查询列表
     *
     * @return
     */
    List<ChatFeedback> queryList(ChatFeedback chatFeedback);

}
