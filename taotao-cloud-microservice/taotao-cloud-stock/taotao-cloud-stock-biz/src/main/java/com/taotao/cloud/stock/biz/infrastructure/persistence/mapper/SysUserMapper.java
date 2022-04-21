package com.taotao.cloud.stock.biz.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xtoon.cloud.sys.infrastructure.persistence.entity.SysUserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户Mapper
 *
 * @author shuigedeng
 * @date 2021-01-23
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUserDO> {

    /**
     * 分页查询
     *
     * @param page
     * @param params
     * @return
     */
    IPage<SysUserDO> queryPage(IPage<SysUserDO> page, @Param("params") Map<String, Object> params);

    /**
     * 查询用户
     *
     * @param params
     * @return
     */
    SysUserDO queryUser(@Param("params") Map<String, Object> params);

    /**
     * 查询用户(不包含租户)
     *
     * @param params
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    List<SysUserDO> queryUserNoTenant(@Param("params") Map<String, Object> params);
}
