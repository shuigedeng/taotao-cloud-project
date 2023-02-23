package com.taotao.cloud.im.biz.platform.modules.chat.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.chat.domain.ChatGroup;
import com.platform.modules.push.vo.PushParamVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 群组 数据库访问层
 * q3z3
 * </p>
 */
@Repository
public interface ChatGroupDao extends BaseDao<ChatGroup> {

    /**
     * 查询列表
     *
     * @return
     */
    List<ChatGroup> queryList(ChatGroup chatGroup);

    /**
     * 查询用户
     */
    List<PushParamVo> queryFriendPushFrom(@Param("groupId") Long groupId, @Param("userId") Long userId);

}
