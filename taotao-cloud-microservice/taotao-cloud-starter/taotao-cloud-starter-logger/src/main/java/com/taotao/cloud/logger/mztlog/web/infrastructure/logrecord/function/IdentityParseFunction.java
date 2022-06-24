package com.taotao.cloud.logger.mztlog.web.infrastructure.logrecord.function;


import com.taotao.cloud.logger.mztlog.service.IParseFunction;
import org.springframework.stereotype.Component;

@Component
public class IdentityParseFunction implements IParseFunction {

    @Override
    public boolean executeBefore() {
        return true;
    }

    @Override
    public String functionName() {
        return "IDENTITY";
    }

    @Override
    public String apply(Object value) {
        return value.toString();
    }
}
