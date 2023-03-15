package com.taotao.cloud.sa.just.biz.just.justauth.mapper;

import com.gitegg.service.extension.justauth.entity.JustAuthSocialUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.gitegg.service.extension.justauth.dto.JustAuthSocialUserDTO;
import com.gitegg.service.extension.justauth.dto.QueryJustAuthSocialUserDTO;

import java.util.List;

/**
 * <p>
 * 租户第三方用户绑定 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2022-05-19
 */
public interface JustAuthSocialUserMapper extends BaseMapper<JustAuthSocialUser> {

    /**
    * 分页查询租户第三方用户绑定列表
    * @param page
    * @param justAuthSocialUserDTO
    * @return
    */
    Page<JustAuthSocialUserDTO> queryJustAuthSocialUserList(Page<JustAuthSocialUserDTO> page, @Param("justAuthSocialUser") QueryJustAuthSocialUserDTO justAuthSocialUserDTO);

    /**
    * 查询租户第三方用户绑定列表
    * @param justAuthSocialUserDTO
    * @return
    */
    List<JustAuthSocialUserDTO> queryJustAuthSocialUserList(@Param("justAuthSocialUser") QueryJustAuthSocialUserDTO justAuthSocialUserDTO);

    /**
    * 查询租户第三方用户绑定信息
    * @param justAuthSocialUserDTO
    * @return
    */
    JustAuthSocialUserDTO queryJustAuthSocialUser(@Param("justAuthSocialUser") QueryJustAuthSocialUserDTO justAuthSocialUserDTO);
}
