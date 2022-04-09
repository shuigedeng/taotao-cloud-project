/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.order.biz.entity.cart;

import com.taotao.cloud.data.jpa.entity.JpaSuperEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 购物车表
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/11/13 09:46
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tt_cart")
@org.hibernate.annotations.Table(appliesTo = "tt_cart", comment = "购物车表")
public class Cart extends JpaSuperEntity<Long> {

	private static final long serialVersionUID = 6887296988458221221L;

	/**
	 * 申请单号
	 */
	@Column(name = "code", unique = true, columnDefinition = "varchar(32) not null comment '申请单号'")
	private String code;

	/**
	 * 公司ID
	 */
	@Column(name = "company_id", columnDefinition = "bigint not null comment '公司ID'")
	private Long companyId;

	/**
	 * 商城ID
	 */
	@Column(name = "mall_id", columnDefinition = "bigint not null comment '商城ID'")
	private Long mallId;

	/**
	 * 提现金额
	 */
	@Column(name = "amount", columnDefinition = "decimal(10,2) not null default 0 comment '提现金额'")
	private BigDecimal amount;

	/**
	 * 钱包余额
	 */
	@Column(name = "balance_amount", columnDefinition = "decimal(10,2) not null default 0 comment '钱包余额'")
	private BigDecimal balanceAmount;

}
