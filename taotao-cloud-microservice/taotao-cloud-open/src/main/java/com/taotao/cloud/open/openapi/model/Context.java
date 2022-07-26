package com.taotao.cloud.open.openapi.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 上下文对象，保存程序运行所需数据
 *
 * @author wanghuidong
 * 时间： 2022/6/21 20:11
 */
@Data
@Component
public class Context {

    /**
     * api处理器集合
     */
    private Collection<ApiHandler> apiHandlers;
}
