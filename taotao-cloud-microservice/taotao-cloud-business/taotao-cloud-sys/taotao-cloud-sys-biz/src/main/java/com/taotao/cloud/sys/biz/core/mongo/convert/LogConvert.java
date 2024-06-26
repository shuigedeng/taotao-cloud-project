/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.sys.biz.core.mongo.convert;

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
 * @since 2021/8/12
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
