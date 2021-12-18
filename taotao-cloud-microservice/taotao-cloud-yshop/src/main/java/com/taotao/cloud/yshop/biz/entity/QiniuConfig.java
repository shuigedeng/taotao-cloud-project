/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.system.biz.entity;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.SuperEntity;

import java.io.Serializable;

@TableName("qiniu_config")
public class QiniuConfig extends SuperEntity<QiniuConfig, Long> implements Serializable {

    /** ID */
    @TableId
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    // @Column(name = "id")
    private Long id;


    /** accessKey */
    // @Column(name = "access_key")
    private String accessKey;


    /** Bucket 识别符 */
    // @Column(name = "bucket")
    private String bucket;


    /** 外链域名 */
    // @Column(name = "host",nullable = false)
    private String host;


    /** secretKey */
    // @Column(name = "secret_key")
    private String secretKey;


    /** 空间类型 */
    // @Column(name = "type")
    private String type;


    /** 机房 */
    // @Column(name = "zone")
    private String zone;


    public void copy(QiniuConfig source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}
}
