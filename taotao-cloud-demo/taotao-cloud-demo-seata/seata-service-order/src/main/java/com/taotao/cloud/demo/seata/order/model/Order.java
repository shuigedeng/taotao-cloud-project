package com.taotao.cloud.demo.seata.order.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 订单
 *
 * @date 2019/9/14
 */
@Data
@Accessors(chain = true)
@TableName("order_tbl")
public class Order {
	@TableId(type = IdType.AUTO)
	private Integer id;
	private String userId;
	private String commodityCode;
	private Integer count;
	private Integer money;
}
