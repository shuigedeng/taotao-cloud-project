package com.taotao.cloud.demo.sharding.jdbc.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.data.mybatis.plus.entity.MpSuperEntity;

/**
 */
@TableName("user")
public class User extends MpSuperEntity {
	private static final long serialVersionUID = 8898492657846787286L;
	private String companyId;
	private String name;
}
