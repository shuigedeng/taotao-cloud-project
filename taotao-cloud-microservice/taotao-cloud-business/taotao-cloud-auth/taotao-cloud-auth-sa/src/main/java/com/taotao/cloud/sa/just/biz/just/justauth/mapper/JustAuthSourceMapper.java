package com.taotao.cloud.sa.just.biz.just.justauth.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.gitegg.service.extension.justauth.entity.JustAuthSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.gitegg.service.extension.justauth.dto.JustAuthSourceDTO;
import com.gitegg.service.extension.justauth.dto.QueryJustAuthSourceDTO;

import java.util.List;

/**
 * <p>
 * 租户第三方登录信息配置表 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2022-05-19
 */
public interface JustAuthSourceMapper extends BaseMapper<JustAuthSource> {

    /**
    * 分页查询租户第三方登录信息配置表列表
    * @param page
    * @param justAuthSourceDTO
    * @return
    */
    Page<JustAuthSourceDTO> queryJustAuthSourceList(Page<JustAuthSourceDTO> page, @Param("justAuthSource") QueryJustAuthSourceDTO justAuthSourceDTO);

    /**
    * 查询租户第三方登录信息配置表列表
    * @param justAuthSourceDTO
    * @return
    */
    List<JustAuthSourceDTO> queryJustAuthSourceList(@Param("justAuthSource") QueryJustAuthSourceDTO justAuthSourceDTO);

    /**
    * 查询租户第三方登录信息配置表信息
    * @param justAuthSourceDTO
    * @return
    */
    JustAuthSourceDTO queryJustAuthSource(@Param("justAuthSource") QueryJustAuthSourceDTO justAuthSourceDTO);
    
    /**
     * 排除多租户插件查询租户第三方登录信息配置表列表
     * @param justAuthSourceDTO
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    List<JustAuthSourceDTO> initJustAuthSourceList(@Param("justAuthSource") QueryJustAuthSourceDTO justAuthSourceDTO);
}
