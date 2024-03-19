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

package com.taotao.cloud.sys.biz.model.convert;

import com.taotao.cloud.sys.biz.model.vo.logistics.LogisticsVO;
import com.taotao.cloud.sys.biz.model.entity.config.LogisticsConfig;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ilogistics地图结构
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 13:39:34
 */
public class LogisticsConvertTest {

	public static List<LogisticsVO> convert(List<LogisticsConfig> logisticsConfigList){


		if (logisticsConfigList == null) {
			return Collections.emptyList();
		}
		List<LogisticsVO> logisticsVOList = new ArrayList<>();
		for (LogisticsConfig logisticsConfig : logisticsConfigList) {
			logisticsVOList.add(convert(logisticsConfig));
		}
		return logisticsVOList;
	}
    /**
     * 物流文件签证官
     *
     * @param logisticsConfig 物流配置
     * @return {@link LogisticsVO }
     * @since 2022-04-28 13:39:35
     */
    public static LogisticsVO convert(LogisticsConfig logisticsConfig){

		if (logisticsConfig == null) {
			return null;
		}
		LogisticsVO logisticsVO = new LogisticsVO();
		logisticsVO.setName(logisticsConfig.getName());
		logisticsVO.setCode(logisticsConfig.getCode());
		logisticsVO.setContactName(logisticsConfig.getContactName());
		logisticsVO.setContactMobile(logisticsConfig.getContactMobile());
		logisticsVO.setStandBy(logisticsConfig.getStandBy());
		logisticsVO.setFormItems(logisticsConfig.getFormItems());
		logisticsVO.setDisabled(logisticsConfig.getDisabled());
// Not mapped TO fields:
// id
// Not mapped FROM fields:
// createTime
// createBy
// updateTime
// updateBy
// version
// delFlag
// id
// entityClass
		return logisticsVO;
	}

}
