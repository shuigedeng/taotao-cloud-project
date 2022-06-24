package com.taotao.cloud.logger.mztlog.service.impl;


import com.taotao.cloud.logger.mztlog.service.IParseFunction;

public class DefaultParseFunction implements IParseFunction {

    @Override
    public boolean executeBefore() {
        return true;
    }

    @Override
    public String functionName() {
        return null;
    }

    @Override
    public String apply(Object value) {
        return null;
    }
}
