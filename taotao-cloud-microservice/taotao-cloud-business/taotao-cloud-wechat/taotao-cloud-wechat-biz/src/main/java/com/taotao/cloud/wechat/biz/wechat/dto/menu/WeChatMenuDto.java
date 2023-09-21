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

package com.taotao.cloud.wechat.biz.wechat.dto.menu;

import cn.bootx.common.core.rest.dto.BaseDto;
import cn.bootx.starter.wechat.core.menu.domin.WeChatMenuInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 微信自定义菜单
 *
 * @author xxm
 * @since 2022-08-08
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "微信自定义菜单")
@Accessors(chain = true)
public class WeChatMenuDto extends BaseDto {

    @Schema(description = "名称")
    private String name;

    @Schema(description = "菜单信息")
    private WeChatMenuInfo menuInfo;

    @Schema(description = "是否发布")
    private boolean publish;

    @Schema(description = "备注")
    private String remark;
}
