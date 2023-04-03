package com.taotao.cloud.log.biz.log.core.mongo.convert;

import cn.bootx.starter.audit.log.core.mongo.entity.DataVersionLogMongo;
import cn.bootx.starter.audit.log.core.mongo.entity.LoginLogMongo;
import cn.bootx.starter.audit.log.core.mongo.entity.OperateLogMongo;
import cn.bootx.starter.audit.log.dto.DataVersionLogDto;
import cn.bootx.starter.audit.log.dto.LoginLogDto;
import cn.bootx.starter.audit.log.dto.OperateLogDto;
import cn.bootx.starter.audit.log.param.LoginLogParam;
import cn.bootx.starter.audit.log.param.OperateLogParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
* 日志转换
* @author xxm
* @date 2021/8/12
*/
@Mapper
public interface LogConvert {
    LogConvert CONVERT = Mappers.getMapper(LogConvert.class);

    OperateLogDto convert(OperateLogMongo in);

    LoginLogDto convert(LoginLogMongo in);

    OperateLogMongo convert(OperateLogParam in);

    LoginLogMongo convert(LoginLogParam in);

    DataVersionLogDto convert(DataVersionLogMongo in);
}
