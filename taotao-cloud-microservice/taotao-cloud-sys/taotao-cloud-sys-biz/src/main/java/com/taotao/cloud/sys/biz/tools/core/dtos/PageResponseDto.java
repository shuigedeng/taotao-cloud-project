package com.taotao.cloud.sys.biz.tools.core.dtos;

public class PageResponseDto<T> {
    //返回对象，可装list以及其他参数
    private Object obj;
    private Long total;

    public PageResponseDto() {
    }

    public PageResponseDto(Object obj, Long total) {
        this.obj = obj;
        this.total = total;
    }

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}
}
