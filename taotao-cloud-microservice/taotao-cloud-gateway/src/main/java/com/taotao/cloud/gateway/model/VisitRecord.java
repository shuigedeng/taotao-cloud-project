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

package com.taotao.cloud.gateway.model;

import java.time.LocalDateTime;

import cn.hutool.core.net.Ipv4Util;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.*;

/**
 * VisitRecord
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Data
public class VisitRecord {

    /**
     * 主键
     */
    @ToString.Exclude
    Long id;

    /**
     * ip地址
     */
    @ToString.Exclude
    Long ip;

    /**
     * 请求方法
     */
    String method;

    /**
     * 请求资源路径
     */
    String uri;

    /**
     * 请求url参数
     */
    String queryParam;

    /**
     * 请求状态码
     */
    Integer status;

    /**
     * 用户id
     */
    Long userId;

    /**
     * 请求发起时间
     */
    @ToString.Exclude
    LocalDateTime creatTime;

    public void setIp( String ip ) {
        this.ip = Ipv4Util.ipv4ToLong(ip);
    }

    @ToString.Include(name = "ipv4", rank = 100)
    public String printIpv4() {
        return Ipv4Util.longToIpv4(ip);
    }
}
