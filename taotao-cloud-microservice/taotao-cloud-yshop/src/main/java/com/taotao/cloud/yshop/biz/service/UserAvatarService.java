/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.system.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.system.api.dto.UserAvatarDto;
import com.taotao.cloud.system.api.dto.UserAvatarQueryCriteria;
import com.taotao.cloud.system.biz.entity.UserAvatar;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UserAvatarService extends IService<UserAvatar> {

    /**
     * 查询数据分页
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(UserAvatarQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     * @param criteria 条件参数
     * @return List<UserAvatarDto>
     */
    List<UserAvatar> queryAll(UserAvatarQueryCriteria criteria);

    /**
     * 导出数据
     * @param all 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<UserAvatarDto> all, HttpServletResponse response) throws IOException;

    UserAvatar saveFile(UserAvatar userAvatar);
}
