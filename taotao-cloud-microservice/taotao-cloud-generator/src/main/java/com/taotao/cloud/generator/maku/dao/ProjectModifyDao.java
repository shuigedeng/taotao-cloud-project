package com.taotao.cloud.generator.maku.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import net.maku.generator.common.dao.BaseDao;
import net.maku.generator.entity.ProjectModifyEntity;
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
