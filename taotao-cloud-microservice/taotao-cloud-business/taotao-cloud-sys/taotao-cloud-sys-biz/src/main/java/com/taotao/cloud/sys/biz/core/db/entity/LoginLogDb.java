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

package com.taotao.cloud.sys.biz.core.db.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.log.biz.log.core.db.convert.LogConvert;
import com.taotao.cloud.log.biz.log.dto.LoginLogDto;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 登录日志
 *
 * @author shuigedeng
 * @since 2021/8/12
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = LoginLogDb.TABLE_NAME)
@TableName(LoginLogDb.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = LoginLogDb.TABLE_NAME, comment = "app配置表")
public class LoginLogDb extends BaseSuperEntity<LoginLogDb, Long> {

    public static final String TABLE_NAME = "tt_login_log";

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
