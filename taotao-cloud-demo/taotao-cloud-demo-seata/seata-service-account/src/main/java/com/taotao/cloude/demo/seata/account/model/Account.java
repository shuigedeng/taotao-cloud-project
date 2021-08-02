package com.taotao.cloude.demo.seata.account.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 账号
 *
 * @since 2019/9/14
 */
@TableName("account_tbl")
public class Account {
	@TableId
	private Long id;
	private String userId;
	private Integer money;
}
