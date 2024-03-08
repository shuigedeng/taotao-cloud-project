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

package com.taotao.cloud.workflow.biz.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
@TableName("base_datainterfacelog")
public class DataInterfaceLogEntity implements Serializable {

    /** 主键id */
    @TableId("F_Id")
    private String id;

    /** 调用接口id */
    @TableField("F_InvokId")
    private String invokId;

    /** 调用时间 */
    @TableField(value = "F_InvokTime", fill = FieldFill.INSERT)
    private Date invokTime;

    /** 调用者id */
    @TableField("F_UserId")
    private String userId;

    /** 请求ip */
    @TableField("F_InvokIp")
    private String invokIp;

    /** 请求设备 */
    @TableField("F_InvokDevice")
    private String invokDevice;

    /** 请求类型 */
    @TableField("F_InvokType")
    private String invokType;

    /** 请求耗时 */
    @TableField("F_InvokWasteTime")
    private Integer invokWasteTime;
}
