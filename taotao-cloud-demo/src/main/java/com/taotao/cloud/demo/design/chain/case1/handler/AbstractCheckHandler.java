package com.taotao.cloud.demo.design.chain.case1.handler;

import com.taotao.cloud.demo.design.chain.case1.ProductCheckHandlerConfig;
import com.taotao.cloud.demo.design.chain.case1.ProductVO;
import com.taotao.cloud.demo.design.chain.case1.Result;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Objects;


/**
 * 抽象类处理器
 */
@Component
public abstract class AbstractCheckHandler {

    /**
     * 当前处理器持有下一个处理器的引用
     */
    @Getter
    @Setter
    private AbstractCheckHandler nextHandler;


    /**
     * 处理器执行方法
     * @param param
     * @return
     */
    public abstract Result handle(ProductVO param);

    /**
     * 处理器配置
     */
    @Setter
    @Getter
    protected ProductCheckHandlerConfig config;

    /**
     * 链路传递
     * @param param
     * @return
     */
    protected Result next(ProductVO param) {
        //下一个链路没有处理器了，直接返回
        if (Objects.isNull(nextHandler)) {
            return Result.success();
        }

        //执行下一个处理器
        return nextHandler.handle(param);
    }

}
