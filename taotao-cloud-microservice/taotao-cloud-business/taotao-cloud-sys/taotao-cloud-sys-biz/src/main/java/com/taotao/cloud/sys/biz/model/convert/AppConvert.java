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
import com.taotao.cloud.sys.api.model.dto.app.AppDTO;
import com.taotao.cloud.sys.biz.model.entity.app.App;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author
 * @version 0.0.1
 * @date 2022/11/23 00:45
 */
@Mapper
public interface AppConvert {

    AppConvert INSTANCE = Mappers.getMapper(AppConvert.class);

    Page<AppDTO> convertPage(Page<App> appDO);

    List<AppDTO> convertList(List<App> app);

    AppDTO convert(App app);

    App convert(AppDTO appDTO);

	//http://127.0.0.1:33337/oauth2/authorize?client_id=67601992f3574c75809a3d79888bf16e&response_type=code&scope=message.read&redirect_uri=http%3A%2F%2F127.0.0.1%3A8090%2Fauthorized
	//http://127.0.0.1:33337/oauth2/authorize?client_id=67601992f3574c75809a3d79888bf16e&response_type=code&scope=profile&state=46ge_TeI-dHuAnyv67nVmCcAmFgCVSZAqjTi9Om-1aA=&redirect_uri=http%3A%2F%2F192.168.101.10%3A8847%2Fdante-cloud-upms%2Fopen%2Fauthorized&code_challenge=KJlktPdfHdPPenXDN3HARjV6pzM7ljfHs-L-bFao3zM&code_challenge_method=S256
	//http://127.0.0.1:33337/oauth2/authorize?client_id=67601992f3574c75809a3d79888bf16e&response_type=code&scope=profile,read-user-by-page&redirect_uri=http%3A%2F%2F192.168.101.10%3A8847%2Fdante-cloud-upms%2Fopen%2Fauthorized&code_challenge=GMBkW4F_Ap4Us75TZ7nDhSUd87HXAt7cLMG-R_2VGwE&code_challenge_method=S256
}
