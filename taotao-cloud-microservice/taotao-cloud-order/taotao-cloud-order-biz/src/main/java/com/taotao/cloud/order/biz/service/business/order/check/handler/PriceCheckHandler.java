package com.taotao.cloud.order.biz.service.business.order.check.handler;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.order.biz.service.business.order.check.ErrorCode;
import com.taotao.cloud.order.biz.service.business.order.check.ProductVO;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

/**
 * 价格校验处理器
 */
@Component
public class PriceCheckHandler extends AbstractCheckHandler{
    @Override
    public Result handle(ProductVO param) {
        System.out.println("价格校验 Handler 开始...");

        //非法价格校验
        boolean illegalPrice =  param.getPrice().compareTo(BigDecimal.ZERO) <= 0;
        if (illegalPrice) {
            return Result.failure(ErrorCode.PARAM_PRICE_ILLEGAL_ERROR);
        }
        //其他校验逻辑...

        System.out.println("价格校验 Handler 通过...");

        //执行下一个处理器
        return super.next(param);
    }
}
