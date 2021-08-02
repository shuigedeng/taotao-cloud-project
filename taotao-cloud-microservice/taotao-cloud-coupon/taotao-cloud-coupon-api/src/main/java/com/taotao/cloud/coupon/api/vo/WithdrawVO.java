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
package com.taotao.cloud.coupon.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/11/20 上午9:42
 */
@Schema(name = "WithdrawVO", description = "提现申请VO")
public class WithdrawVO implements Serializable {

	private static final long serialVersionUID = 5126530068827085130L;

	@Schema(description = "id")
	private Long id;

	@Schema(description = "申请单号")
	private String code;

	@Schema(description = "公司ID")
	private Long companyId;

	@Schema(description = "商城ID")
	private Long mallId;

	@Schema(description = "提现金额")
	private BigDecimal amount;

	@Schema(description = "钱包余额")
	private BigDecimal balanceAmount;

	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	@Schema(description = "最后修改时间")
	private LocalDateTime lastModifiedTime;

	@Override
	public String toString() {
		return "WithdrawVO{" +
			"id=" + id +
			", code='" + code + '\'' +
			", companyId=" + companyId +
			", mallId=" + mallId +
			", amount=" + amount +
			", balanceAmount=" + balanceAmount +
			", createTime=" + createTime +
			", lastModifiedTime=" + lastModifiedTime +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		WithdrawVO that = (WithdrawVO) o;
		return Objects.equals(id, that.id) && Objects.equals(code, that.code)
			&& Objects.equals(companyId, that.companyId) && Objects.equals(mallId,
			that.mallId) && Objects.equals(amount, that.amount) && Objects.equals(
			balanceAmount, that.balanceAmount) && Objects.equals(createTime, that.createTime)
			&& Objects.equals(lastModifiedTime, that.lastModifiedTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, code, companyId, mallId, amount, balanceAmount, createTime,
			lastModifiedTime);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getMallId() {
		return mallId;
	}

	public void setMallId(Long mallId) {
		this.mallId = mallId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(BigDecimal balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(LocalDateTime lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public WithdrawVO() {
	}

	public WithdrawVO(Long id, String code, Long companyId, Long mallId, BigDecimal amount,
		BigDecimal balanceAmount, LocalDateTime createTime, LocalDateTime lastModifiedTime) {
		this.id = id;
		this.code = code;
		this.companyId = companyId;
		this.mallId = mallId;
		this.amount = amount;
		this.balanceAmount = balanceAmount;
		this.createTime = createTime;
		this.lastModifiedTime = lastModifiedTime;
	}

	public static WithdrawVOBuilder builder() {
		return new WithdrawVOBuilder();
	}

	public static final class WithdrawVOBuilder {

		private Long id;
		private String code;
		private Long companyId;
		private Long mallId;
		private BigDecimal amount;
		private BigDecimal balanceAmount;
		private LocalDateTime createTime;
		private LocalDateTime lastModifiedTime;

		private WithdrawVOBuilder() {
		}

		public static WithdrawVOBuilder aWithdrawVO() {
			return new WithdrawVOBuilder();
		}

		public WithdrawVOBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public WithdrawVOBuilder code(String code) {
			this.code = code;
			return this;
		}

		public WithdrawVOBuilder companyId(Long companyId) {
			this.companyId = companyId;
			return this;
		}

		public WithdrawVOBuilder mallId(Long mallId) {
			this.mallId = mallId;
			return this;
		}

		public WithdrawVOBuilder amount(BigDecimal amount) {
			this.amount = amount;
			return this;
		}

		public WithdrawVOBuilder balanceAmount(BigDecimal balanceAmount) {
			this.balanceAmount = balanceAmount;
			return this;
		}

		public WithdrawVOBuilder createTime(LocalDateTime createTime) {
			this.createTime = createTime;
			return this;
		}

		public WithdrawVOBuilder lastModifiedTime(LocalDateTime lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
			return this;
		}

		public WithdrawVO build() {
			WithdrawVO withdrawVO = new WithdrawVO();
			withdrawVO.setId(id);
			withdrawVO.setCode(code);
			withdrawVO.setCompanyId(companyId);
			withdrawVO.setMallId(mallId);
			withdrawVO.setAmount(amount);
			withdrawVO.setBalanceAmount(balanceAmount);
			withdrawVO.setCreateTime(createTime);
			withdrawVO.setLastModifiedTime(lastModifiedTime);
			return withdrawVO;
		}
	}
}
