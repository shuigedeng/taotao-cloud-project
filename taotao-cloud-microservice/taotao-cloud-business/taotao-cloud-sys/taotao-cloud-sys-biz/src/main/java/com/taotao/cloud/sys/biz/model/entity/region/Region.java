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

package com.taotao.cloud.sys.biz.model.entity.region;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.data.mybatis.mybatisplus.handler.typehandler.JacksonListTypeHandler;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;

/**
 * 地区表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:52:30
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = Region.TABLE_NAME)
@TableName(value = Region.TABLE_NAME, autoResultMap = true)
// @org.hibernate.annotations.Table(appliesTo = Region.TABLE_NAME, comment = "地区表")
public class Region extends BaseSuperEntity<Region, Long> {

    public static final String TABLE_NAME = "tt_region";

    /** 地区父节点 */
    @Column(name = "parent_id", columnDefinition = "bigint comment '地区父节点'")
    private Long parentId;

    /** 地区编码 */
    @Column(name = "code", columnDefinition = "varchar(255) not null comment '地区编码'")
    private String code;

    /** 地区名称 */
    @Column(name = "name", columnDefinition = "varchar(255) not null default '' comment '地区名称'")
    private String name;

    /**
     * 地区级别（1:省份province,2:市city,3:区县district,4:街道street） "行政区划级别" + "country:国家" +
     * "province:省份（直辖市会在province和city显示）" + "city:市（直辖市会在province和city显示）" + "district:区县" +
     * "street:街道"
     */
    @Column(
            name = "level",
            columnDefinition = "varchar(255) null comment" + " '地区级别（1:省份province,2:市city,3:区县district,4:街道street）'")
    private String level;

    /** 城市编码 */
    @Column(name = "city_code", columnDefinition = "varchar(255) null comment '城市编码'")
    private String cityCode;

    /** 城市中心经度 */
    @Column(name = "lng", columnDefinition = "varchar(255) null comment '城市中心经度'")
    private String lng;

    /** 城市中心纬度 */
    @Column(name = "lat", columnDefinition = "varchar(255) null comment '城市中心纬度'")
    private String lat;

    // @Column(name = "path", columnDefinition = "varchar(255) null comment '行政地区路径，类似：1，2，3'")
    // private String path;

    @Column(name = "order_num", columnDefinition = "int not null default 0 comment '排序'")
    private Integer orderNum;

    /**
     * 备注
     * 设置了ResultMap为`mybatis-plus_Person`后就可以拿到正确的值. @ResultMap("mybatis-plus_Person") @Select("SELECT
     * * FROM person WHERE id=#{id}") Person selectOneById(int id);
     */
    @Type(value = JsonType.class)
    @TableField(typeHandler = JacksonListTypeHandler.class)
    @Column(name = "id_tree", columnDefinition = "json null comment 'id树'")
    private List<Long> idTree;

    @Type(value = JsonType.class)
    @TableField(typeHandler = JacksonListTypeHandler.class)
    @Column(name = "code_tree", columnDefinition = "json null comment 'id树'")
    private List<String> codeTree;

    /** 当前深度 */
    @Column(name = "depth", columnDefinition = "int not null default 0 comment '当前深度 已1开始'")
    private Integer depth;

    @Builder
    public Region(
            Long id,
            LocalDateTime createTime,
            Long createBy,
            LocalDateTime updateTime,
            Long updateBy,
            Integer version,
            Boolean delFlag,
            Long parentId,
            String code,
            String name,
            String level,
            String cityCode,
            String lng,
            String lat,
            Integer orderNum) {
        super(id, createTime, createBy, updateTime, updateBy, version, delFlag);
        this.parentId = parentId;
        this.code = code;
        this.name = name;
        this.level = level;
        this.cityCode = cityCode;
        this.lng = lng;
        this.lat = lat;
        this.orderNum = orderNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Region region = (Region) o;
        return getId() != null && Objects.equals(getId(), region.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
