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

package com.taotao.cloud.payment.biz.jeepay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.cloud.payment.biz.jeepay.core.model.BaseModel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付接口配置参数表
 *
 * @author [mybatis plus generator]
 * @since 2021-04-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_pay_interface_config")
public class PayInterfaceConfig extends BaseModel implements Serializable {

    public static final LambdaQueryWrapper<PayInterfaceConfig> gw() {
        return new LambdaQueryWrapper<>();
    }

    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 账号类型:1-服务商 2-商户 */
    private Byte infoType;

    /** 服务商或商户No */
    private String infoId;

    /** 支付接口代码 */
    private String ifCode;

    /** 接口配置参数,json字符串 */
    private String ifParams;

    /** 支付接口费率 */
    private BigDecimal ifRate;

    /** 状态: 0-停用, 1-启用 */
    private Byte state;

    /** 备注 */
    private String remark;

    /** 创建者用户ID */
    private Long createdUid;

    /** 创建者姓名 */
    private String createdBy;

    /** 创建时间 */
    private Date createdAt;

    /** 更新者用户ID */
    private Long updatedUid;

    /** 更新者姓名 */
    private String updatedBy;

    /** 更新时间 */
    private Date updatedAt;
}
