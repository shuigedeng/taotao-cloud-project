package com.taotao.cloud.log.biz.log.core.mongo.convert;

import com.taotao.cloud.log.biz.log.core.mongo.entity.DataVersionLogMongo;
import com.taotao.cloud.log.biz.log.core.mongo.entity.LoginLogMongo;
import com.taotao.cloud.log.biz.log.core.mongo.entity.OperateLogMongo;
import com.taotao.cloud.log.biz.log.dto.DataVersionLogDto;
import com.taotao.cloud.log.biz.log.dto.LoginLogDto;
import com.taotao.cloud.log.biz.log.dto.OperateLogDto;
import com.taotao.cloud.log.biz.log.param.LoginLogParam;
import com.taotao.cloud.log.biz.log.param.OperateLogParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 日志转换
 *
 * @author shuigedeng
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
