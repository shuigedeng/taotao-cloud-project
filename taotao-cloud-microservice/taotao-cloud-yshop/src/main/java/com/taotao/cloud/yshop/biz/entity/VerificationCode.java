/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.system.biz.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.SuperEntity;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.validation.constraints.NotBlank;

@TableName("verification_code")
public class VerificationCode extends SuperEntity<VerificationCode, Long> implements Serializable {

    @TableId
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    /** 使用场景，自己定义  */
    private String scenes;

    /** true 为有效，false 为无效，验证时状态+时间+具体的邮箱或者手机号 */
    private Boolean status = true;

    /** 类型 ：phone 和 email */
    private String type;

    /** 具体的phone与email */
    private String value;

    /** 创建日期 */
    @TableField(fill = FieldFill.INSERT)
    // @Column(name = "create_time")
    private Timestamp createTime;

    public VerificationCode(String code, String scenes, @NotBlank String type, @NotBlank String value) {
        this.code = code;
        this.scenes = scenes;
        this.type = type;
        this.value = value;
    }

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getScenes() {
		return scenes;
	}

	public void setScenes(String scenes) {
		this.scenes = scenes;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
}
