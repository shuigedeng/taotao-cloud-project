package com.taotao.cloud.generator.maku.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.taotao.cloud.generator.maku.common.dao.BaseDao;
import com.taotao.cloud.generator.maku.entity.ProjectModifyEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目名变更
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface ProjectModifyDao extends BaseDao<ProjectModifyEntity> {

}
