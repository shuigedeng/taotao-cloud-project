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

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * 文件日志表
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/11/12 15:33
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = FileLog.TABLE_NAME)
@TableName(FileLog.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = FileLog.TABLE_NAME, comment = "文件表")
public class FileLog extends BaseSuperEntity<FileLog, Long> {

    public static final String TABLE_NAME = "tt_file_log";

    /** 业务ID */
    @Column(name = "biz_id", columnDefinition = "bigint comment '业务ID'")
    private Long bizId;

    /**
     * 业务类型
     *
     * @see BizType
     */
    @Column(name = "biz_type", columnDefinition = "varchar(32) not null comment '业务类型'")
    private String bizType;

    /**
     * 数据类型
     *
     * @see DataType {IMAGE:图片;VIDEO:视频;AUDIO:音频;DOC:文档;OTHER:其他}
     */
    @Column(name = "data_type", columnDefinition = "varchar(32) not null comment '数据类型'")
    private String dataType;

    /** 原始文件名 */
    @Column(name = "original_file_name", columnDefinition = "varchar(255) not null comment '原始文件名'")
    private String originalFileName;

    /** 文件访问链接 */
    @Column(name = "url", columnDefinition = "varchar(255) not null comment '文件访问链接'")
    private String url;

    /** 文件md5值 */
    @Column(name = "file_md5", columnDefinition = "varchar(255) not null comment '文件md5值'")
    private String fileMd5;

    /** 文件上传类型 取上传文件的值 */
    @Column(name = "context_type", columnDefinition = "varchar(255) not null comment '文件上传类型'")
    private String contextType;

    /** 唯一文件名 */
    @Column(name = "filename", columnDefinition = "varchar(255) not null comment '唯一文件名'")
    private String filename;

    /** 后缀(没有.) */
    @Column(name = "ext", columnDefinition = "varchar(64) not null comment '后缀'")
    private String ext;

    /** 大小 */
    @Column(name = "size", columnDefinition = "bigint not null comment '大小'")
    private Long size;

    @Builder
    public FileLog(
            Long id,
            LocalDateTime createTime,
            Long createBy,
            LocalDateTime updateTime,
            Long updateBy,
            Integer version,
            Boolean delFlag,
            Long bizId,
            String bizType,
            String dataType,
            String originalFileName,
            String url,
            String fileMd5,
            String contextType,
            String filename,
            String ext,
            Long size) {
        super(id, createTime, createBy, updateTime, updateBy, version, delFlag);
        this.bizId = bizId;
        this.bizType = bizType;
        this.dataType = dataType;
        this.originalFileName = originalFileName;
        this.url = url;
        this.fileMd5 = fileMd5;
        this.contextType = contextType;
        this.filename = filename;
        this.ext = ext;
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        FileLog fileLog = (FileLog) o;
        return getId() != null && Objects.equals(getId(), fileLog.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
