package com.taotao.cloud.generator.maku.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.generator.maku.entity.DataSourceEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据源管理
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface DataSourceDao extends BaseMapper<DataSourceEntity> {

}
