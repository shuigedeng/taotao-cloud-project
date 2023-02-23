package com.taotao.cloud.order.biz.service.business.order.check.handler;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.order.biz.service.business.order.check.ErrorCode;
import com.taotao.cloud.order.biz.service.business.order.check.ProductVO;
import org.springframework.stereotype.Component;

/**
 * 库存校验处理器
 */
@Component
public class StockCheckHandler extends AbstractCheckHandler {
    @Override
    public Result handle(ProductVO param) {
        System.out.println("库存校验 Handler 开始...");

        //非法库存校验
        boolean illegalStock = param.getStock() < 0;
        if (illegalStock) {
            return Result.failure(ErrorCode.PARAM_STOCK_ILLEGAL_ERROR);
        }
        //其他校验逻辑..

        System.out.println("库存校验 Handler 通过...");

        //执行下一个处理器
        return super.next(param);
    }
}
