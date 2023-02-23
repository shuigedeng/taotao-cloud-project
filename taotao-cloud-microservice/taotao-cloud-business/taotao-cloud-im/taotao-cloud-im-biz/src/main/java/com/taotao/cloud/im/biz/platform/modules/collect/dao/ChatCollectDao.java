package com.taotao.cloud.im.biz.platform.modules.collect.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.collect.domain.ChatCollect;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 收藏表 数据库访问层
 * q3z3
 * </p>
 */
@Repository
public interface ChatCollectDao extends BaseDao<ChatCollect> {

    /**
     * 查询列表
     *
     * @return
     */
    List<ChatCollect> queryList(ChatCollect chatCollect);

}
