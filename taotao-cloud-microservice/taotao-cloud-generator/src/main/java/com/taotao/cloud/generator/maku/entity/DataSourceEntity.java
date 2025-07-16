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

package com.taotao.cloud.generator.maku.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.generator.maku.common.annotation.EncryptParameter;
import java.util.Date;
import lombok.Data;

/**
 * 数据源管理
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Data
@TableName("gen_datasource")
public class DataSourceEntity {
    /**
     * id
     */
    @TableId private Long id;

    /**
     * 数据库类型
     */
    private String dbType;

    /**
     * 连接名
     */
    private String connName;

    /**
     * URL
     */
    private String connUrl;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    @EncryptParameter private String password;

    /**
     * 创建时间
     */
    private Date createTime;
}
