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

package com.taotao.cloud.wechat.biz.niefy.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 系统日志
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@TableName("sys_log")
public class SysLogEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;
    // 用户名
    private String username;
    // 用户操作
    private String operation;
    // 请求方法
    private String method;
    // 请求参数
    private String params;
    // 执行时长(毫秒)
    private Long time;
    // IP地址
    private String ip;
    // 创建时间
    private Date createDate;

    public SysLogEntity() {}

    public SysLogEntity(String openid, String method, String params, String ip) {
        this.username = openid;
        this.method = method;
        this.params = params;
        this.ip = ip;
        this.createDate = new Date();
    }
}
