package com.taotao.cloud.payment.biz.daxpay.single.unisdk.common.util;

import lombok.extern.slf4j.Slf4j;
import com.taotao.cloud.payment.biz.daxpay.unisdk.common.api.PayErrorExceptionHandler;
import com.taotao.cloud.payment.biz.daxpay.unisdk.common.exception.PayErrorException;



/**
 * LogExceptionHandler 日志处理器
 * @author  egan
 * <pre>
 * email egzosn@gmail.com
 * date 2016-6-1 11:28:01
 *
 *
 * source chanjarster/weixin-java-tools
 * </pre>
 */
@Slf4j
public class LogExceptionHandler implements PayErrorExceptionHandler {


    @Override
    public void handle(PayErrorException e) {

        log.error("Error happens", e);

    }

}
