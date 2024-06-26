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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/** 系统日志 */
@Data
@Schema(description = "系统日志")
public class SysLog implements Serializable {

    /** 编号 */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "编号")
    private Long id;

    /** 操作IP */
    @Schema(description = "操作IP")
    private String requestIp;

    /** 日志类型 #LogType{0:操作类型;1:异常类型} */
    @Schema(description = "日志类型,#LogType{0:操作类型;1:异常类型}")
    private String type;

    /** 操作人 */
    @Schema(description = "操作人")
    private String userName;

    /** 操作描述 */
    @Schema(description = "操作描述")
    private String description;

    /** 类路径 */
    @Schema(description = "类路径")
    private String classPath;

    /** 请求方法 */
    @Schema(description = "请求方法")
    private String requestMethod;

    /** 请求地址 */
    @Schema(description = "请求地址")
    private String requestUri;

    /**
     * 请求类型
     * {GET:GET请求;POST:POST请求;PUT:PUT请求;DELETE:DELETE请求;PATCH:PATCH请求;TRACE:TRACE请求;HEAD:HEAD请求;OPTIONS:OPTIONS请求;}
     */
    @Schema(
            description =
                    "请求类型 {GET:GET请求;POST:POST请求;PUT:PUT请求;DELETE:DELETE请求;PATCH:PATCH请求;TRACE:TRACE请求;HEAD:HEAD请求;OPTIONS:OPTIONS请求;}")
    private String httpMethod;

    /** 请求参数 */
    @Schema(description = "请求参数")
    private String params;

    /** 返回值 */
    @Schema(description = "返回值")
    private String result;

    /** 异常详情信息 */
    @Schema(description = "异常详情信息")
    private String exDesc;

    /** 异常描述 */
    @Schema(description = "异常描述")
    private String exDetail;

    /** 创建时间 */
    @Schema(description = "创建时间", hidden = true)
    private LocalDateTime createTime;

    /** 结束时间 */
    @Schema(description = "结束时间")
    private LocalDateTime finishTime;

    /** 执行时间 */
    @Schema(description = "执行时间")
    private String time;

    /** 浏览器 */
    @Schema(description = "浏览器")
    private String userAgent;

    /** 0:否 1:是 */
    @Schema(description = "0:否 1:是")
    private Integer isDel;

    /** 租户id */
    @Schema(description = "租户id")
    private Integer tenantId;

    private static final long serialVersionUID = 1L;
}
