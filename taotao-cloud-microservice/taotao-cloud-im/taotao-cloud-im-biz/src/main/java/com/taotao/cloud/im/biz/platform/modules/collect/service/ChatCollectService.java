package com.taotao.cloud.im.biz.platform.modules.collect.service;

import com.github.pagehelper.PageInfo;
import com.platform.common.web.service.BaseService;
import com.platform.modules.collect.domain.ChatCollect;
import com.platform.modules.collect.vo.CollectVo01;

/**
 * <p>
 * 收藏表 服务层
 * q3z3
 * </p>
 */
public interface ChatCollectService extends BaseService<ChatCollect> {

    /**
     * 新增收藏
     */
    void addCollect(CollectVo01 collectVo);

    /**
     * 删除收藏
     */
    void deleteCollect(Long collectId);

    /**
     * 列表
     */
    PageInfo collectList(ChatCollect collect);

}
