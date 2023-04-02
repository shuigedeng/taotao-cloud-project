package com.taotao.cloud.sys.biz.sensitive.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.*;

import org.hibernate.annotations.GenericGenerator;

import com.daffodil.core.annotation.Hql;
import com.daffodil.core.annotation.Hql.Logical;
import com.daffodil.core.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * -敏感词词语对象
 * @author yweijian
 * @date 2022-09-16
 * @version 1.0
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_sensitive_words")
public class SysSensitiveWords extends BaseEntity<String> {

    private static final long serialVersionUID = 1L;
    
    /** 主键编号 */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "id", length = 32)
    private String id;
    
    /** 词语 */
    @Column(name = "words", length = 32)
    @NotBlank(message = "敏感词词语不能为空")
    @Size(min = 0, max = 32, message = "敏感词词语不能超过32个字符")
    @Hql(type = Logical.LIKE)
    private String words;
    
    /** 创建者 */
    @Column(name = "create_by", length = 32)
    private String createBy;
    
    /** 创建时间 */
    @Column(name = "create_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /** 更新者 */
    @Column(name = "update_by", length = 32)
    private String updateBy;
    
    /** 更新时间 */
    @Column(name = "update_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
