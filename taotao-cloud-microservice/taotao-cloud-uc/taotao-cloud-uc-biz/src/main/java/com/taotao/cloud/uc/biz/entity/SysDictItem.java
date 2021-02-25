package com.taotao.cloud.uc.biz.entity;// package com.taotao.cloud.uc.biz.entity;

import com.taotao.cloud.data.jpa.entity.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 字典项表
 *
 * @author dengtao
 * @date 2020/5/2 16:42
 */
@Data
@SuperBuilder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tt_sys_dict_item")
@org.hibernate.annotations.Table(appliesTo = "tt_sys_dict_item", comment = "字典项表")
// @SQLDelete(sql = "update sys_dict_item set del_flag = 1 where id = ?")
// @Where(clause = "del_flag = 1")
public class SysDictItem extends BaseEntity {

    /**
     * 字典id
     *
     * @see SysDict
     */
    @Column(name = "dict_id", nullable = false, columnDefinition = "bigint not null comment '字典id'")
    private Long dictId;

    /**
     * 字典项文本
     */
    @Column(name = "item_text", nullable = false, columnDefinition = "varchar(2000) not null comment '字典项文本'")
    private String itemText;

    /**
     * 字典项值
     */
    @Column(name = "item_value", nullable = false, columnDefinition = "varchar(2000) not null comment '字典项文本'")
    private String itemValue;

    /**
     * 描述
     */
    @Column(name = "description", columnDefinition = "varchar(255) comment '描述'")
    private String description;

    /**
     * 状态 0不启用 1启用 默认值(1)
     */
    @Builder.Default
    @Column(name = "status", nullable = false, columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 comment '状态 0不启用 1启用 默认值(1)'")
    private Boolean status = true;

    /**
     * 排序值
     */
    @Builder.Default
    @Column(name = "sort_num", columnDefinition = "int(11) not null default 0 comment '排序值'")
    private Integer sortNum = 0;
}
