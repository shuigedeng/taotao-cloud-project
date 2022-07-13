package com.taotao.cloud.im.biz.platform.modules.chat.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.chat.domain.ChatGroupInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 数据库访问层
 * q3z3
 * </p>
 */
@Repository
public interface ChatGroupInfoDao extends BaseDao<ChatGroupInfo> {

    /**
     * 查询列表
     *
     * @return
     */
    List<ChatGroupInfo> queryList(ChatGroupInfo chatGroupInfo);

}
