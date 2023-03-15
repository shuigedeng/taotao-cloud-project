package com.taotao.cloud.sa.just.biz.just.justauth.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.gitegg.service.extension.justauth.entity.JustAuthConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.gitegg.service.extension.justauth.dto.JustAuthConfigDTO;
import com.gitegg.service.extension.justauth.dto.QueryJustAuthConfigDTO;

import java.util.List;

/**
 * <p>
 * 租户第三方登录功能配置表 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2022-05-16
 */
public interface JustAuthConfigMapper extends BaseMapper<JustAuthConfig> {

    /**
    * 分页查询租户第三方登录功能配置表列表
    * @param page
    * @param justAuthConfigDTO
    * @return
    */
    Page<JustAuthConfigDTO> queryJustAuthConfigList(Page<JustAuthConfigDTO> page, @Param("justAuthConfig") QueryJustAuthConfigDTO justAuthConfigDTO);

    /**
    * 查询租户第三方登录功能配置表列表
    * @param justAuthConfigDTO
    * @return
    */
    List<JustAuthConfigDTO> queryJustAuthConfigList(@Param("justAuthConfig") QueryJustAuthConfigDTO justAuthConfigDTO);

    /**
    * 查询租户第三方登录功能配置表信息
    * @param justAuthConfigDTO
    * @return
    */
    JustAuthConfigDTO queryJustAuthConfig(@Param("justAuthConfig") QueryJustAuthConfigDTO justAuthConfigDTO);
    
    /**
     * 查询租户第三方登录功能配置表列表
     * @param justAuthConfigDTO
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    List<JustAuthConfigDTO> initJustAuthConfigList(@Param("justAuthConfig") QueryJustAuthConfigDTO justAuthConfigDTO);
}
