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

package com.taotao.cloud.sys.biz.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @program: logs
 * @description:
 * @author: Sinda
 * @create: 2022-03-19 20:42:34
 */
@Data
@TableName("sys_log_login")
@Schema(description = "")
public class SysLogLogin implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    @Schema(description = "id")
    private Integer id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "用户id")
    private Integer userId;

    @Schema(description = "登录时间")
    private LocalDateTime loginTime;

    @Schema(description = "登录ip")
    private String ip;

    @Schema(description = "浏览器信息")
    private String useragent;

    @Schema(description = "0:成功 1:失败")
    private Integer status;

    @Schema(description = "备注")
    private String remake;
}
