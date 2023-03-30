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

package com.taotao.cloud.wechat.biz.mp.convert.material;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.mp.controller.admin.material.vo.MpMaterialRespVO;
import cn.iocoder.yudao.module.mp.controller.admin.material.vo.MpMaterialUploadRespVO;
import cn.iocoder.yudao.module.mp.dal.dataobject.account.MpAccountDO;
import cn.iocoder.yudao.module.mp.dal.dataobject.material.MpMaterialDO;
import java.io.File;
import me.chanjar.weixin.mp.bean.material.WxMpMaterial;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MpMaterialConvert {

    MpMaterialConvert INSTANCE = Mappers.getMapper(MpMaterialConvert.class);

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(source = "account.id", target = "accountId"),
        @Mapping(source = "account.appId", target = "appId"),
        @Mapping(source = "name", target = "name")
    })
    MpMaterialDO convert(String mediaId, String type, String url, MpAccountDO account, String name);

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(source = "account.id", target = "accountId"),
        @Mapping(source = "account.appId", target = "appId"),
        @Mapping(source = "name", target = "name")
    })
    MpMaterialDO convert(
            String mediaId,
            String type,
            String url,
            MpAccountDO account,
            String name,
            String title,
            String introduction,
            String mpUrl);

    MpMaterialUploadRespVO convert(MpMaterialDO bean);

    default WxMpMaterial convert(String name, File file, String title, String introduction) {
        return new WxMpMaterial(name, file, title, introduction);
    }

    PageResult<MpMaterialRespVO> convertPage(PageResult<MpMaterialDO> page);
}
