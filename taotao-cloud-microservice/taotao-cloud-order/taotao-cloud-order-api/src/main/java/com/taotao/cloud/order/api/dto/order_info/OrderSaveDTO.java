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
package com.taotao.cloud.order.api.dto.order_info;


import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单添加对象
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 16:31:52
 */
@Schema(name = "OrderSaveDTO", description = "订单添加对象")
public class OrderSaveDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = 5126530068827085130L;

	/**
	 * 买家ID
	 */
	@Schema(description = "买家ID")
	private Long memberId;
	/**
	 * 订单编码
	 */
	@Schema(description = "订单编码")
	private String code;
	/**
	 * 订单金额
	 */
	@Schema(description = "订单金额")
	private BigDecimal amount;
	/**
	 * 订单主状态
	 */
	@Schema(description = "订单主状态")
	private Integer mainStatus;
	/**
	 * 订单子状态
	 */
	@Schema(description = "订单子状态")
	private Integer childStatus;
	/**
	 * 收货人姓名
	 */
	@Schema(description = "收货人姓名")
	private String receiverName;
	/**
	 * 收货人电话
	 */
	@Schema(description = "收货人电话")
	private String receiverPhone;
	/**
	 * 收货地址:json的形式存储
	 */
	@Schema(description = "收货地址:json的形式存储")
	private String receiverAddressJson;

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getMainStatus() {
		return mainStatus;
	}

	public void setMainStatus(Integer mainStatus) {
		this.mainStatus = mainStatus;
	}

	public Integer getChildStatus() {
		return childStatus;
	}

	public void setChildStatus(Integer childStatus) {
		this.childStatus = childStatus;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public String getReceiverAddressJson() {
		return receiverAddressJson;
	}

	public void setReceiverAddressJson(String receiverAddressJson) {
		this.receiverAddressJson = receiverAddressJson;
	}

	public static OrderDTOBuilder builder() {
		return new OrderDTOBuilder();
	}

	public static final class OrderDTOBuilder {

		private Long memberId;
		private String code;
		private BigDecimal amount;
		private Integer mainStatus;
		private Integer childStatus;
		private String receiverName;
		private String receiverPhone;
		private String receiverAddressJson;

		private OrderDTOBuilder() {
		}


		public OrderDTOBuilder memberId(Long memberId) {
			this.memberId = memberId;
			return this;
		}

		public OrderDTOBuilder code(String code) {
			this.code = code;
			return this;
		}

		public OrderDTOBuilder amount(BigDecimal amount) {
			this.amount = amount;
			return this;
		}

		public OrderDTOBuilder mainStatus(Integer mainStatus) {
			this.mainStatus = mainStatus;
			return this;
		}

		public OrderDTOBuilder childStatus(Integer childStatus) {
			this.childStatus = childStatus;
			return this;
		}

		public OrderDTOBuilder receiverName(String receiverName) {
			this.receiverName = receiverName;
			return this;
		}

		public OrderDTOBuilder receiverPhone(String receiverPhone) {
			this.receiverPhone = receiverPhone;
			return this;
		}

		public OrderDTOBuilder receiverAddressJson(String receiverAddressJson) {
			this.receiverAddressJson = receiverAddressJson;
			return this;
		}

		public OrderSaveDTO build() {
			OrderSaveDTO orderDTO = new OrderSaveDTO();
			orderDTO.setMemberId(memberId);
			orderDTO.setCode(code);
			orderDTO.setAmount(amount);
			orderDTO.setMainStatus(mainStatus);
			orderDTO.setChildStatus(childStatus);
			orderDTO.setReceiverName(receiverName);
			orderDTO.setReceiverPhone(receiverPhone);
			orderDTO.setReceiverAddressJson(receiverAddressJson);
			return orderDTO;
		}
	}
}
