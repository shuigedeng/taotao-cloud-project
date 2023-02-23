package com.taotao.cloud.im.biz.platform.modules.chat.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.chat.domain.ChatUser;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 用户表 数据库访问层
 * q3z3
 * </p>
 */
@Repository
public interface ChatUserDao extends BaseDao<ChatUser> {

    /**
     * 查询列表
     *
     * @return
     */
    List<ChatUser> queryList(ChatUser chatUser);

}
