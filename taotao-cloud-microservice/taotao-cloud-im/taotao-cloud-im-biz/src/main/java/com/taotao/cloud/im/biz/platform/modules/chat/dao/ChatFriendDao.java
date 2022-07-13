package com.taotao.cloud.im.biz.platform.modules.chat.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.chat.domain.ChatFriend;
import com.platform.modules.chat.vo.FriendVo06;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 好友表 数据库访问层
 * q3z3
 * </p>
 */
@Repository
public interface ChatFriendDao extends BaseDao<ChatFriend> {

    /**
     * 查询列表
     *
     * @return
     */
    List<ChatFriend> queryList(ChatFriend chatFriend);

    /**
     * 查询好友列表
     */
    List<FriendVo06> friendList(Long userId);

    /**
     * 查询好友id
     */
    List<Long> queryFriendId(Long userId);

}
