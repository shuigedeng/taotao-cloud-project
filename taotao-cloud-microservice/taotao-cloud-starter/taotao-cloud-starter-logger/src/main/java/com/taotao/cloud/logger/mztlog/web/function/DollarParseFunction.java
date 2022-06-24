package com.taotao.cloud.logger.mztlog.web.function;

import com.taotao.cloud.logger.mztlog.service.IParseFunction;
import org.springframework.stereotype.Component;

@Component
public class DollarParseFunction implements IParseFunction {
    @Override
    public String functionName() {
        return "DOLLAR";
    }

    @Override
    public String apply(Object value) {
        return "10$,/666";
    }
}
