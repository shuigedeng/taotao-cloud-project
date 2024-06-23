package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.dcloud.service;

import javax.servlet.http.HttpServletRequest;


/**
 * @author 刘森飚
 * @since 2023-02-13
 */

public interface LogService {

    /**
     * 记录日志
     * @param request
     * @param shortLinkCode
     * @param accountNo
     * @return
     */
    void recordShortLinkLog(HttpServletRequest request, String shortLinkCode, Long accountNo);

}
