package com.taotao.cloud.open.common.model;


import java.util.List;

/**
 * 二进制类型参数，可以为Binary、Binary[]、Binary集合
 *
 * @author wanghuidong
 * 时间： 2022/7/24 19:42
 */
public class BinaryParam {

    /**
     * 二进制类型对象
     */
    private List<Binary> binaries;

    /**
     * 二进制类型对象的字符传表示
     */
    private String binariesStr;

	public List<Binary> getBinaries() {
		return binaries;
	}

	public void setBinaries(List<Binary> binaries) {
		this.binaries = binaries;
	}

	public String getBinariesStr() {
		return binariesStr;
	}

	public void setBinariesStr(String binariesStr) {
		this.binariesStr = binariesStr;
	}
}
