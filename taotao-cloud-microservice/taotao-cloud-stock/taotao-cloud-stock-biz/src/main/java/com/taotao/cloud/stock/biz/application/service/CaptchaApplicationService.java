package com.taotao.cloud.stock.biz.application.service;

import java.awt.image.BufferedImage;

/**
 * 验证码应用服务
 *
 * @author haoxin
 * @date 2021-06-23
 **/
public interface CaptchaApplicationService {

    /**
     * 生成ø验证码
     *
     * @param uuid
     * @return
     */
    BufferedImage getCaptcha(String uuid);
}
