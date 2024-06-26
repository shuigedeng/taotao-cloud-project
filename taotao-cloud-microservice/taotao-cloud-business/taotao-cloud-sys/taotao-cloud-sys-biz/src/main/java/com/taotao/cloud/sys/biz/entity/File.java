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
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * 文件表
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
@Table(name = File.TABLE_NAME)
@TableName(File.TABLE_NAME)
//@jakarta.persistence.Table(appliesTo = File.TABLE_NAME, comment = "文件表")
public class File extends BaseSuperEntity<File, Long> {

    public static final String TABLE_NAME = "tt_file";

    /** 创建人 */
    @Column(name = "create_name", columnDefinition = "varchar(255) not null comment '创建人'")
    private String createName;

    /** 业务类型 供应商上传图片 供应商上传附件 商品上传图片 */
    @Column(name = "biz_type", columnDefinition = "varchar(255) not null comment '业务类型 '")
    private String bizType;

    /** 数据类型 {IMAGE:图片;VIDEO:视频;AUDIO:音频;DOC:文档;OTHER:其他} */
    @Column(name = "data_type", columnDefinition = "varchar(255) not null comment '数据类型'")
    private String dataType;

    /** 原始文件名 */
    @Column(name = "original", columnDefinition = "varchar(255) not null comment '原始文件名'")
    private String original;

    /** 文件访问链接 */
    @Column(name = "url", columnDefinition = "varchar(255) not null comment '文件访问链接'")
    private String url;

    /** 文件md5值 */
    @Column(name = "md5", columnDefinition = "varchar(255) not null comment '文件md5值'")
    private String md5;

    /** 文件上传类型 取上传文件的值 */
    @Column(name = "type", columnDefinition = "varchar(255) not null comment '文件上传类型'")
    private String type;

    /** 文件上传类型 取上传文件的值 */
    @Column(name = "context_type", columnDefinition = "varchar(255) not null comment '文件上传类型'")
    private String contextType;

    /** 唯一文件名 */
    @Column(name = "name", columnDefinition = "varchar(255) not null comment '唯一文件名'")
    private String name;

    /** 后缀(没有.) */
    @Column(name = "ext", columnDefinition = "varchar(255) not null comment '后缀'")
    private String ext;

    /** 大小 */
    @Column(name = "length", columnDefinition = "bigint null comment '大小'")
    private Long length;

    @Builder
    public File(
            Long id,
            LocalDateTime createTime,
            Long createBy,
            LocalDateTime updateTime,
            Long updateBy,
            Integer version,
            Boolean delFlag,
            String createName,
            String bizType,
            String dataType,
            String original,
            String url,
            String md5,
            String type,
            String contextType,
            String name,
            String ext,
            Long length) {
        super(id, createTime, createBy, updateTime, updateBy, version, delFlag);
        this.createName = createName;
        this.bizType = bizType;
        this.dataType = dataType;
        this.original = original;
        this.url = url;
        this.md5 = md5;
        this.type = type;
        this.contextType = contextType;
        this.name = name;
        this.ext = ext;
        this.length = length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        File file = (File) o;
        return getId() != null && Objects.equals(getId(), file.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
