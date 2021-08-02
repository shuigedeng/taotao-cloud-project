package com.tatoao.cloud.demo.seata.storage.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 库存
 *
 * @since 2019/9/14
 */
@TableName("storage_tbl")
public class Storage {
	@TableId
	private Long id;
	private String commodityCode;
	private Long count;
}
