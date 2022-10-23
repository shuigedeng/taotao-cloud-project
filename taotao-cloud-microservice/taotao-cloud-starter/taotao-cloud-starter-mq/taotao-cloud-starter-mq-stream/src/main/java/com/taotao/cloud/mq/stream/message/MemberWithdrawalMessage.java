package com.taotao.cloud.mq.stream.message;


import java.math.BigDecimal;

/**
 * 会员提现消息
 *
 * @since 2020/12/14 16:31
 */
public class MemberWithdrawalMessage {

	/**
	 * 金额
	 */
	private BigDecimal price;

	/**
	 * 会员id
	 */
	private Long memberId;

	/**
	 * 提现状态
	 */
	private String status;

	/**
	 * 提现到哪里
	 *
	 * @see MemberWithdrawalDestinationEnum
	 */
	private String destination;

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}
}
