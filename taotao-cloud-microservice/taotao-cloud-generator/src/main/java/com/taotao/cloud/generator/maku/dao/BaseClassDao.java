package com.taotao.cloud.generator.maku.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.maku.generator.entity.BaseClassEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 基类管理
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface BaseClassDao extends BaseMapper<BaseClassEntity> {

}
