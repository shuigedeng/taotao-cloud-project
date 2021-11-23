package com.taotao.cloud.pay.merchant;

/**
 * 商户不存在异常
 *
 */

public class MerchantNotFoundException extends RuntimeException {

	/**
	 * 商户列表id
	 */
	private String detailsId;
	private static final String MESSAGE = "不存在的商户列表id：";

	/**
	 * @param detailsId 商户列表id
	 */
	public MerchantNotFoundException(String detailsId) {
		this(MESSAGE + detailsId, null);
	}

	/**
	 * @param detailsId 商户列表id
	 * @param cause     异常
	 */
	public MerchantNotFoundException(String detailsId, Throwable cause) {
		super(MESSAGE + detailsId, cause);
		this.detailsId = detailsId;
	}

	/**
	 * 获取商户id
	 *
	 * @return 商户id
	 */
	public String getMerchantId() {
		return detailsId;
	}


}
