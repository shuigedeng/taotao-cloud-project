package com.taotao.cloud.sa.just.biz.just.justauth.service;

import java.util.List;

import com.gitegg.service.extension.justauth.entity.JustAuthSocialUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.gitegg.service.extension.justauth.dto.JustAuthSocialUserDTO;
import com.gitegg.service.extension.justauth.dto.CreateJustAuthSocialUserDTO;
import com.gitegg.service.extension.justauth.dto.UpdateJustAuthSocialUserDTO;
import com.gitegg.service.extension.justauth.dto.QueryJustAuthSocialUserDTO;

/**
 * <p>
 * 租户第三方登录功能配置表 服务类
 * </p>
 *
 * @author GitEgg
 * @since 2022-05-19
 */
public interface IJustAuthSocialUserService extends IService<JustAuthSocialUser> {

    /**
    * 分页查询租户第三方登录功能配置表列表
    * @param page
    * @param queryJustAuthSocialUserDTO
    * @return
    */
    Page<JustAuthSocialUserDTO> queryJustAuthSocialUserList(Page<JustAuthSocialUserDTO> page, QueryJustAuthSocialUserDTO queryJustAuthSocialUserDTO);

    /**
    * 查询租户第三方登录功能配置表列表
    * @param queryJustAuthSocialUserDTO
    * @return
    */
    List<JustAuthSocialUserDTO> queryJustAuthSocialUserList(QueryJustAuthSocialUserDTO queryJustAuthSocialUserDTO);

    /**
    * 查询租户第三方登录功能配置表详情
    * @param queryJustAuthSocialUserDTO
    * @return
    */
    JustAuthSocialUserDTO queryJustAuthSocialUser(QueryJustAuthSocialUserDTO queryJustAuthSocialUserDTO);

    /**
    * 创建租户第三方登录功能配置表
    * @param justAuthSocialUser
    * @return
    */
    JustAuthSocialUser createJustAuthSocialUser(CreateJustAuthSocialUserDTO justAuthSocialUser);

    /**
    * 更新租户第三方登录功能配置表
    * @param justAuthSocialUser
    * @return
    */
    boolean updateJustAuthSocialUser(UpdateJustAuthSocialUserDTO justAuthSocialUser);

    /**
    * 删除租户第三方登录功能配置表
    * @param justAuthSocialUserId
    * @return
    */
    boolean deleteJustAuthSocialUser(Long justAuthSocialUserId);

    /**
    * 批量删除租户第三方登录功能配置表
    * @param justAuthSocialUserIds
    * @return
    */
    boolean batchDeleteJustAuthSocialUser(List<Long> justAuthSocialUserIds);
}
