package com.taotao.cloud.im.biz.platform.modules.chat.dao;

import com.platform.modules.chat.domain.ChatApply;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

/**
 * <p>
 * 好友申请表 数据库访问层
 * q3z3
 * </p>
 */
@Repository
public interface ChatApplyDao extends BaseDao<ChatApply> {

    /**
     * 查询列表
     *
     * @return
     */
    List<ChatApply> queryList(ChatApply chatApply);

}
