package com.taotao.cloud.logger.mztlog.web.function;

import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.logger.mztlog.service.IParseFunction;
import com.taotao.cloud.logger.mztlog.web.pojo.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class OrderBeforeParseFunction implements IParseFunction {

    @Override
    public boolean executeBefore() {
        return true;
    }

    @Override
    public String functionName() {
        return "ORDER_BEFORE";
    }

    @Override
    public String apply(Object value) {
        LogUtil.info("@@@@@@@@");
        if (StringUtils.isEmpty(value)) {
            return "";
        }
        LogUtil.info("###########,{}", value);
        Order order = new Order();
        order.setProductName("xxxx");
        return order.getProductName().concat("(").concat(value.toString()).concat(")");
    }
}
