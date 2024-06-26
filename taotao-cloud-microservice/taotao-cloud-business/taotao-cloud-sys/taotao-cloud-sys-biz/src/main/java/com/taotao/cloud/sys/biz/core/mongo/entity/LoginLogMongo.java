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

package com.taotao.cloud.sys.biz.core.mongo.entity;

import com.taotao.cloud.log.biz.log.core.mongo.convert.LogConvert;
import com.taotao.cloud.log.biz.log.dto.LoginLogDto;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author shuigedeng
 * @since 2021/12/2
 */
@Data
@Accessors(chain = true)
@Document(collection = "tt_login_log")
public class LoginLogMongo {

    @Id
    private Long id;

    /**
     * 用户账号id
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String account;

    /**
     * 登录成功状态
     */
    private Boolean login;

    /**
     * 登录终端
     */
    private String client;

    /**
     * 登录方式
     */
    private String loginType;

    /**
     * 登录IP地址
     */
    private String ip;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 提示消息
     */
    private String msg;

    /**
     * 访问时间
     */
    private LocalDateTime loginTime;

    public LoginLogDto toDto() {
        return LogConvert.CONVERT.convert(this);
    }
}
