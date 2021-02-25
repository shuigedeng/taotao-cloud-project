package com.taotao.cloud.standalone.system.modules.security.controller;

import cn.hutool.core.util.ObjectUtil;
import com.taotao.cloud.standalone.common.constant.PreConstant;
import com.taotao.cloud.standalone.common.utils.R;
import com.taotao.cloud.standalone.system.modules.security.code.sms.AliYunSmsUtils;
import com.taotao.cloud.standalone.system.modules.security.code.sms.SmsResponse;
import com.wf.captcha.ArithmeticCaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Classname AuthController
 * @Description TODO
 * @Author Created by Lihaodong (alias:小东啊) im.lihaodong@gmail.com
 * @Date 2019/12/15 4:04 下午
 * @Version 1.0
 */
@RequestMapping("/auth")
@RestController
public class AuthController {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 生成验证码
     *
     * @throws ServletException
     * @throws IOException
     */
    @GetMapping("/captcha.jpg")
    public R captcha() throws IOException {
        // 算术类型
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(111, 36);
        // 几位数运算，默认是两位
        captcha.setLen(2);
        // 获取运算的结果
        String result = captcha.text();

        String key = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(PreConstant.PRE_IMAGE_KEY + key, result, 2, TimeUnit.MINUTES);
        Map map = new HashMap();
        map.put("key", key);
        map.put("img", captcha.toBase64());
        return R.ok(map);
    }

    /**
     * 发送短信验证码
     *
     * @param phone
     * @return
     */
    @PostMapping("/sendCode/{phone}")
    public R sendSmsCode(@PathVariable("phone") String phone) {
        SmsResponse smsResponse = AliYunSmsUtils.sendSms(phone, "prex", "登录");

        if (ObjectUtil.isNull(smsResponse)) {
            return R.error("短信发送失败");
        }
        // 保存到验证码到 redis 有效期两分钟
        redisTemplate.opsForValue().set(phone, smsResponse.getSmsCode(), 2, TimeUnit.MINUTES);
        return R.ok();
    }


}
