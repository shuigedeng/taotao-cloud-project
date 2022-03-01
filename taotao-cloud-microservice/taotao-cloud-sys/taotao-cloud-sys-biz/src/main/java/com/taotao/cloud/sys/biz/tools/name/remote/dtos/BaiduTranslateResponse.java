package com.taotao.cloud.sys.biz.tools.name.remote.dtos;


import java.util.List;
import java.util.Set;

public class BaiduTranslateResponse {
    private String error_code;
    private String error_msg;

    private List<TranslateResult> trans_result;

    public static class TranslateResult{
        private String src;
        private String dst;

	    public String getSrc() {
		    return src;
	    }

	    public void setSrc(String src) {
		    this.src = src;
	    }

	    public String getDst() {
		    return dst;
	    }

	    public void setDst(String dst) {
		    this.dst = dst;
	    }
    }

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	public String getError_msg() {
		return error_msg;
	}

	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}

	public List<TranslateResult> getTrans_result() {
		return trans_result;
	}

	public void setTrans_result(
		List<TranslateResult> trans_result) {
		this.trans_result = trans_result;
	}
}
