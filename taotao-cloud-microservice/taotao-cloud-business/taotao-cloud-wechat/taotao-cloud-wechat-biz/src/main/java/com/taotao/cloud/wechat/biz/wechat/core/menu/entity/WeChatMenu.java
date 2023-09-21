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

package com.taotao.cloud.wechat.biz.wechat.core.menu.entity;

import cn.bootx.common.core.annotation.BigField;
import cn.bootx.common.core.function.EntityBaseFunction;
import cn.bootx.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.starter.wechat.core.menu.convert.WeChatMenuConvert;
import cn.bootx.starter.wechat.core.menu.domin.WeChatMenuInfo;
import cn.bootx.starter.wechat.dto.menu.WeChatMenuDto;
import cn.bootx.starter.wechat.param.menu.WeChatMenuParam;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
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
@Accessors(chain = true)
@TableName(value = "starter_wx_menu", autoResultMap = true)
public class WeChatMenu extends MpBaseEntity implements EntityBaseFunction<WeChatMenuDto> {

    /** 名称 */
    private String name;

    /** 菜单信息 */
    @BigField
    @TableField(typeHandler = JacksonTypeHandler.class)
    private WeChatMenuInfo menuInfo;

    /** 是否发布 */
    private boolean publish;

    /** 备注 */
    private String remark;

    /** 创建对象 */
    public static WeChatMenu init(WeChatMenuParam in) {
        return WeChatMenuConvert.CONVERT.convert(in);
    }

    /** 转换成dto */
    @Override
    public WeChatMenuDto toDto() {
        return WeChatMenuConvert.CONVERT.convert(this);
    }
}
