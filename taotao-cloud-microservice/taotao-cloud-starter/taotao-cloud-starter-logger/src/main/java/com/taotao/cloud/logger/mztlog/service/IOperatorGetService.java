package com.taotao.cloud.logger.mztlog.service;


import com.taotao.cloud.logger.mztlog.beans.Operator;

public interface IOperatorGetService {

    /**
     * 可以在里面外部的获取当前登陆的用户，比如UserContext.getCurrentUser()
     *
     * @return 转换成Operator返回
     */
    Operator getUser();
}
