package com.taotao.cloud.bigdata.azkaban.mq.base;

import com.free.bsf.core.base.BsfException;

/**
 * @author: chejiangyi
 * @version: 2019-06-12 14:35
 * 消息队列的异常
 **/
public class MQException extends BsfException {
    public MQException(Exception exp)
    {
        super(exp);
    }
}
