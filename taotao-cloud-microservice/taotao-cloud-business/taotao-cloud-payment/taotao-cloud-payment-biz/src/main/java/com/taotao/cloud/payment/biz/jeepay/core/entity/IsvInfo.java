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
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 服务商信息表
 *
 * @author [mybatis plus generator]
 * @since 2021-04-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_isv_info")
public class IsvInfo extends BaseModel implements Serializable {

    // gw
    public static final LambdaQueryWrapper<IsvInfo> gw() {
        return new LambdaQueryWrapper<>();
    }

    private static final long serialVersionUID = 1L;

    /** 服务商号 */
    @TableId(value = "isv_no", type = IdType.INPUT)
    private String isvNo;

    /** 服务商名称 */
    private String isvName;

    /** 服务商简称 */
    private String isvShortName;

    /** 联系人姓名 */
    private String contactName;

    /** 联系人手机号 */
    private String contactTel;

    /** 联系人邮箱 */
    private String contactEmail;

    /** 状态: 0-停用, 1-正常 */
    private Byte state;

    /** 备注 */
    private String remark;

    /** 创建者用户ID */
    private Long createdUid;

    /** 创建者姓名 */
    private String createdBy;

    /** 创建时间 */
    private Date createdAt;

    /** 更新时间 */
    private Date updatedAt;
}
