package com.taotao.cloud.log.biz.log.core.db.convert;

import com.taotao.cloud.log.biz.log.core.db.entity.DataVersionLogDb;
import com.taotao.cloud.log.biz.log.core.db.entity.LoginLogDb;
import com.taotao.cloud.log.biz.log.core.db.entity.OperateLogDb;
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

	OperateLogDto convert(OperateLogDb in);

	LoginLogDto convert(LoginLogDb in);

	OperateLogDb convert(OperateLogParam in);

	LoginLogDb convert(LoginLogParam in);

	DataVersionLogDto convert(DataVersionLogDb in);

}
