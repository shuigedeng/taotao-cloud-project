package com.taotao.cloud.demo.seata.order.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 订单
 *
 * @since 2019/9/14
 */
@TableName("order_tbl")
public class Order {
	@TableId(type = IdType.AUTO)
	private Integer id;
	private String userId;
	private String commodityCode;
	private Integer count;
	private Integer money;
}
