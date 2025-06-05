package com.taotao.cloud.generator.maku.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.generator.maku.entity.TableEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据表
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface TableDao extends BaseMapper<TableEntity> {

}
