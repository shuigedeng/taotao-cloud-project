package com.taotao.cloud.stock.biz.domain.captcha.repository;

import com.taotao.cloud.stock.biz.domain.captcha.model.entity.Captcha;
import com.taotao.cloud.stock.biz.domain.model.captcha.Captcha;
import com.taotao.cloud.stock.biz.domain.model.captcha.Uuid;
import com.taotao.cloud.stock.biz.domain.captcha.model.vo.Uuid;

/**
 * 验证码Repository
 *
 * @author haoxin
 * @date 2021-05-10
 **/
public interface CaptchaRepository {

    /**
     * 获取编码
     *
     * @param uuid
     * @return
     */
    com.taotao.cloud.stock.biz.domain.model.captcha.Captcha find(
		    com.taotao.cloud.stock.biz.domain.model.captcha.Uuid uuid);

    /**
     * 保存
     *
     * @param captcha
     */
    void store(Captcha captcha);

    /**
     * 删除
     *
     * @param uuid
     */
    void remove(Uuid uuid);
}
