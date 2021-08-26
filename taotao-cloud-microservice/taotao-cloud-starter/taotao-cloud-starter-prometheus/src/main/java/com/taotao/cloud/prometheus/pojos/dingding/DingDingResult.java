package com.taotao.cloud.prometheus.pojos.dingding;

public class DingDingResult {

	private int errcode;
	private String errmsg;

	public int getErrcode() {
		return errcode;
	}

	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	@Override
	public String toString() {
		return "DingDingResult [errcode=" + errcode + ", errmsg=" + errmsg + "]";
	}

}
