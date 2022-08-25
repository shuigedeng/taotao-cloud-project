package com.taotao.cloud.monitor.kuding.pojos.dingding;

public class DingDingNotice {

	protected DingDingAt at;

	protected String msgtype = "text";

	/**
	 * @param at
	 * @param msgtype
	 */
	public DingDingNotice(DingDingAt at, String msgtype) {
		this.at = at;
		this.msgtype = msgtype;
	}

	/**
	 * @return the at
	 */
	public DingDingAt getAt() {
		return at;
	}

	/**
	 * @param at the at to set
	 */
	public void setAt(DingDingAt at) {
		this.at = at;
	}

	/**
	 * @return the msgtype
	 */
	public String getMsgtype() {
		return msgtype;
	}

	/**
	 * @param msgtype the msgtype to set
	 */
	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

}
