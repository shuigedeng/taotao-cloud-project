package com.taotao.cloud.log.biz.log.core.db.convert;

import cn.bootx.starter.audit.log.core.db.entity.DataVersionLogDb;
import cn.bootx.starter.audit.log.core.db.entity.LoginLogDb;
import cn.bootx.starter.audit.log.core.db.entity.OperateLogDb;
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

    OperateLogDto convert(OperateLogDb in);

    LoginLogDto convert(LoginLogDb in);

    OperateLogDb convert(OperateLogParam in);

    LoginLogDb convert(LoginLogParam in);

    DataVersionLogDto convert(DataVersionLogDb in);

}
