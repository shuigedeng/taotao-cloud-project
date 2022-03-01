package com.taotao.cloud.sys.biz.tools.codepatch.service.dtos;


public class CompileResult {
    private int exitValue;
    private String result;

    public CompileResult() {
    }

    public CompileResult(int exitValue, String result) {
        this.exitValue = exitValue;
        this.result = result;
    }

	public int getExitValue() {
		return exitValue;
	}

	public void setExitValue(int exitValue) {
		this.exitValue = exitValue;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
