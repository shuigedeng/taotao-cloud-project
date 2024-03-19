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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.sys.biz.model.dto.app.AppDTO;
import com.taotao.cloud.sys.biz.model.entity.app.App;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author
 * @version 0.0.1
 * @since 2022/11/23 00:45
 */
@Mapper
public interface AppConvert {

	AppConvert INSTANCE = Mappers.getMapper(AppConvert.class);

	Page<AppDTO> convertPage(Page<App> appDO);

	List<AppDTO> convertList(List<App> app);

	AppDTO convert(App app);

	App convert(AppDTO appDTO);

}
