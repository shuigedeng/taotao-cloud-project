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

package com.taotao.cloud.workflow.biz.common.database.util;

import com.baomidou.mybatisplus.annotation.TableField;
import com.taotao.cloud.workflow.biz.common.database.model.dto.DataSourceDTO;
import com.taotao.cloud.workflow.biz.common.database.model.interfaces.DataSourceMod;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** 数据源基础工具类 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceUtil implements DataSourceMod {

    /** 数据库类型 */
    @TableField("F_DBTYPE")
    private String dbType;

    /** 主机ip */
    @TableField("F_HOST")
    private String host;

    /** 端口 */
    @TableField("F_PORT")
    private Integer port;

    /** 库名 */
    @TableField("F_SERVICENAME")
    private String dbName;

    /** 用户 */
    @TableField("F_USERNAME")
    private String userName;

    /** 密码 */
    @TableField("F_PASSWORD")
    private String password;

    /** 表空间 */
    @TableField(value = "F_TABLESPACE")
    private String dbTableSpace;

    /** 模式 */
    @TableField(value = "F_DBSCHEMA")
    private String dbSchema;

    /** 数据连接jdbc-url参数 */
    @TableField(exist = false)
    private String urlParams;

    /** url地址 */
    @TableField(exist = false)
    private String url;

    /** 数据连接jdbc-url参数 */
    @TableField(exist = false)
    private String prepareUrl;

    /** 驱动包 */
    @TableField(exist = false)
    private String driver;

    /** oracle多方式登录参数 */
    @TableField(value = "F_OracleParam")
    private String oracleParam;

    /** -- 这里的参数dataSourceUtil是spring托管的全局唯一变量， New对象防止数据源互串，防止Bean覆盖 */
    @Override
    public DataSourceDTO convertDTO() {
        return convertDTO(null);
    }

    @Override
    public DataSourceDTO convertDTO(String dbName) {
        return convertDTO(dbName, 1);
    }

    protected DataSourceDTO convertDTO(String dbName, Integer dataSourceFrom) {
        DataSourceDTO dataSourceDTO = new DataSourceDTO();
        BeanUtils.copyProperties(this, dataSourceDTO);
        if (StringUtil.isNotEmpty(dbName)) {
            dataSourceDTO.setDbName(dbName);
        }
        dataSourceDTO.setDataSourceFrom(dataSourceFrom);
        return dataSourceDTO;
    }
}
