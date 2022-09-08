package com.taotao.cloud.data.mybatisplus.interceptor;

import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;

/**
 * 议员拦截器
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-09-08 14:59:16
 */
public class MpInterceptor {

	private InnerInterceptor innerInterceptor;

	private int sortNo = 0;

	public MpInterceptor(InnerInterceptor innerInterceptor, int sortNo) {
		this.innerInterceptor = innerInterceptor;
		this.sortNo = sortNo;
	}

	public MpInterceptor(InnerInterceptor innerInterceptor) {
		this.innerInterceptor = innerInterceptor;
	}

	public InnerInterceptor getInnerInterceptor() {
		return innerInterceptor;
	}

	public void setInnerInterceptor(InnerInterceptor innerInterceptor) {
		this.innerInterceptor = innerInterceptor;
	}

	public int getSortNo() {
		return sortNo;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}
}
