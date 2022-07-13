package com.taotao.cloud.im.biz.platform.modules.chat.dao;

import com.platform.modules.chat.domain.ChatVersion;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

/**
 * <p>
 * 版本 数据库访问层
 * q3z3
 * </p>
 */
@Repository
public interface ChatVersionDao extends BaseDao<ChatVersion> {

    /**
     * 查询列表
     *
     * @return
     */
    List<ChatVersion> queryList(ChatVersion chatVersion);

}
