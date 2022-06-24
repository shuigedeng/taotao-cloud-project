package com.taotao.cloud.logger.mztlog.service.impl;


import com.taotao.cloud.logger.mztlog.beans.Operator;
import com.taotao.cloud.logger.mztlog.service.IOperatorGetService;

public class DefaultOperatorGetServiceImpl implements IOperatorGetService {

    @Override
    public Operator getUser() {
        // return Optional.ofNullable(UserUtils.getUser())
        //                .map(a -> new Operator(a.getName(), a.getLogin()))
        //                .orElseThrow(()->new IllegalArgumentException("user is null"));
        Operator operator = new Operator();
        operator.setOperatorId("111");
        return operator;
    }
}
