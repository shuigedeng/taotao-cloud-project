package com.taotao.cloud.demo.design.chain.case2;

public class OneLevelFlowHandler extends AbstractFlowHandler{
    @Override
    public boolean approve(MoneyPayVO param) {
        return false;
    }
}
