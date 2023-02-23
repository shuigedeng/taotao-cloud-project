package com.taotao.cloud.stock.biz.domain.captcha.external;


/**
 * 验证码校验服务
 *
 * @author shuigedeng
 * @date 2021-05-11
 */
public class CaptchaValidateService {

    private CaptchaRepository captchaRepository;

    public CaptchaValidateService(CaptchaRepository captchaRepository) {
        this.captchaRepository = captchaRepository;
    }

    /**
     * 校验验证码
     *
     * @param uuid
     * @param captchaCode
     * @return
     */
    public boolean validate(Uuid uuid, CaptchaCode captchaCode) {
        Captcha captcha = captchaRepository.find(uuid);
        if (captcha == null) {
            return false;
        }
        //删除验证码
        captchaRepository.remove(uuid);
        return captcha.validate(captchaCode);
    }
}
