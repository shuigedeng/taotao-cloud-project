package com.taotao.cloud.open.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 上下文对象，保存程序运行所需数据
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:12:00
 */
@Data
@Component
public class Context {

    /**
     * api处理器集合
     */
    private Collection<ApiHandler> apiHandlers;
}
