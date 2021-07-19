package com.taotao.cloud.uc.biz.entity;// package com.taotao.cloud.uc.biz.entity;

import com.taotao.cloud.data.jpa.entity.BaseEntity;
import java.time.LocalDateTime;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * 字典表
 *
 * @author shuigedeng
 * @since 2020/6/15 11:00
 */
@Data
@SuperBuilder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tt_sys_dict")
@org.hibernate.annotations.Table(appliesTo = "tt_sys_dict", comment = "字典表")
public class SysDict {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "bigint not null comment 'id'")
	private Long id;

    /**
     * 字典名称
     */
    @Column(name = "dict_name", nullable = false, columnDefinition = "varchar(255) not null default '' comment '字典名称'")
    private String dictName;

    /**
     * 字典编码
     */
    @Column(name = "dict_code", unique = true, nullable = false, columnDefinition = "varchar(255) not null comment '字典编码'")
    private String dictCode;

    /**
     * 描述
     */
    @Column(name = "description", columnDefinition = "varchar(255) comment '描述'")
    private String description;

    /**
     * 排序值
     */
    @Builder.Default
    @Column(name = "sort_num", columnDefinition = "int(11) not null default 0 comment '排序值'")
    private Integer sortNum = 0;

    /**
     * 备注信息
     */
    @Column(name = "remark", columnDefinition = "varchar(255) comment '备注信息'")
    private String remark;


	@CreatedDate
	@Column(name = "create_time", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP comment '创建时间'")
	private LocalDateTime createTime;

	@LastModifiedDate
	@Column(name = "last_modified_time", columnDefinition = "TIMESTAMP comment '最后修改时间'")
	private LocalDateTime lastModifiedTime;
}
