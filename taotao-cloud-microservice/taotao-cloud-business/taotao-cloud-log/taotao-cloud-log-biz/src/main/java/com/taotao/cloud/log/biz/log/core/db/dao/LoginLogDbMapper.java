package com.taotao.cloud.log.biz.log.core.db.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.log.biz.log.core.db.entity.LoginLogDb;
import org.apache.ibatis.annotations.Mapper;

/**
* 登录日志
* @author shuigedeng
* @date 2021/8/12
*/
@Mapper
public interface LoginLogDbMapper extends BaseMapper<LoginLogDb> {
}
