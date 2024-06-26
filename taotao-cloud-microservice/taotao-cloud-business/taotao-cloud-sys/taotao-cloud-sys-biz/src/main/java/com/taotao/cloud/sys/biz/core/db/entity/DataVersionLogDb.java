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
import com.taotao.cloud.log.biz.log.dto.DataVersionLogDto;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 数据版本日志
 *
 * @author shuigedeng
 * @since 2022/1/10
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = DataVersionLogDb.TABLE_NAME)
@TableName(DataVersionLogDb.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = DataVersionLogDb.TABLE_NAME, comment = "app配置表")
public class DataVersionLogDb extends BaseSuperEntity<DataVersionLogDb, Long> {

    public static final String TABLE_NAME = "tt_data_version_log";

    @Schema(description = "表名称")
    private String tableName;

    @Schema(description = "数据名称")
    private String dataName;

    @Schema(description = "数据主键")
    private String dataId;

    @Schema(description = "数据内容")
    private String dataContent;

    @Schema(description = "本次变动的数据内容")
    private String changeContent;

    @Schema(description = "数据版本")
    private Integer dataVersion;

    @Schema(description = "创建者ID")
    private Long creator;

    public DataVersionLogDto toDto() {
        return LogConvert.CONVERT.convert(this);
    }
}
