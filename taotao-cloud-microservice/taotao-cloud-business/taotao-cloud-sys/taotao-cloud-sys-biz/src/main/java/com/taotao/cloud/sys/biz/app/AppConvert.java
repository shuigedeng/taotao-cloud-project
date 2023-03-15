/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.sys.biz.app;

import com.art.system.api.app.dto.AppDTO;
import com.art.system.dao.dataobject.AppDO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/11/23 00:45
 */
@Mapper
public interface AppConvert {

	AppConvert INSTANCE = Mappers.getMapper(AppConvert.class);

	Page<AppDTO> convertPage(Page<AppDO> appDO);

	List<AppDTO> convertList(List<AppDO> appDO);

	AppDTO convert(AppDO appDO);

	AppDO convert(AppDTO appDTO);

}
