package com.taotao.cloud.sys.biz.api.controller.tools.core.dtos;

import java.util.List;

/**
 * 树结构响应数据
 * @param <T>
 */
public interface TreeResponseDto<T> {
    /**
     * 获取编号
     * @return
     */
    String getId();

    /**
     * 父级编号
     * @return
     */
    String getParentId();

    /**
     * 获取文本
     * @return
     */
    String getLabel();

    /**
     * 获取当前原始对象
     * @return
     */
    T getOrigin();

    List<? extends TreeResponseDto> getChildren();
}
