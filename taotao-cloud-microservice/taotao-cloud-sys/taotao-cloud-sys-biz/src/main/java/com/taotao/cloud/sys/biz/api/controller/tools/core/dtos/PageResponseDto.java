package com.taotao.cloud.sys.biz.api.controller.tools.core.dtos;

import lombok.Data;

@Data
public class PageResponseDto<T> {
    /**
     * 返回对象，可装list以及其他参数
     */
    private Object obj;
    private Long total;

    public PageResponseDto() {
    }

    public PageResponseDto(Object obj, Long total) {
        this.obj = obj;
        this.total = total;
    }

    public Object getData(){
        return obj;
    }
}
