package com.taotao.cloud.log.biz.log.core.db.dao;

import cn.bootx.starter.audit.log.core.db.entity.OperateLogDb;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperateLogDbMapper extends BaseMapper<OperateLogDb> {
}
