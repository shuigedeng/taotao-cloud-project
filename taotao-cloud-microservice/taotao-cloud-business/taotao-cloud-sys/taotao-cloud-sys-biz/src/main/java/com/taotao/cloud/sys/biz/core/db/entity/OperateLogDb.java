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
import com.taotao.cloud.log.biz.log.dto.OperateLogDto;
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
 * 操作日志
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
@Table(name = OperateLogDb.TABLE_NAME)
@TableName(OperateLogDb.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = OperateLogDb.TABLE_NAME, comment = "app配置表")
public class OperateLogDb extends BaseSuperEntity<OperateLogDb, Long> {

    public static final String TABLE_NAME = "tt_request_log";
    /**
     * 操作模块
     */
    private String title;

    /**
     * 操作人员id
     */
    private Long operateId;

    /**
     * 操作人员账号
     */
    private String username;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 请求url
     */
    private String operateUrl;

    /**
     * 操作ip
     */
    private String operateIp;

    /**
     * 操作地点
     */
    private String operateLocation;

    /**
     * 请求参数
     */
    private String operateParam;

    /**
     * 返回参数
     */
    private String operateReturn;

    /**
     * 操作状态（0正常 1异常）
     */
    private Boolean success;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 操作时间
     */
    private LocalDateTime operateTime;

    public OperateLogDto toDto() {
        return LogConvert.CONVERT.convert(this);
    }
}
