package com.taotao.cloud.captcha.config;

import org.springframework.context.annotation.Import;

@Import({CaptchaServiceAutoConfiguration.class, CaptchaStorageAutoConfiguration.class})
public class CaptchaAutoConfiguration {

}
