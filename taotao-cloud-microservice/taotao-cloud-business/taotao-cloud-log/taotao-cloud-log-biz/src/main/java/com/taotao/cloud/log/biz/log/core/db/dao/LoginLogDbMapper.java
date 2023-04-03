package com.taotao.cloud.log.biz.log.core.db.dao;

import cn.bootx.starter.audit.log.core.db.entity.LoginLogDb;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* 登录日志
* @author xxm
* @date 2021/8/12
*/
@Mapper
public interface LoginLogDbMapper extends BaseMapper<LoginLogDb> {
}
