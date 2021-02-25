package com.taotao.cloud.standalone.system.modules.sys.util;

import com.taotao.cloud.standalone.common.constant.PreConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Classname EmailUtil
 * @Description 邮件工具类
 * @Author Created by Lihaodong (alias:小东啊) lihaodongmail@163.com
 * @Date 2019-06-15 10:47
 * @Version 1.0
 */
@Component
public class EmailUtil {

    /**
     * 获取JavaMailSender bean
     */
    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * 获取配置文件的username
     */
    @Value("${spring.mail.username}")
    private String username;

    public void sendSimpleMail(String to, HttpServletRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        //设定邮件参数
        //发送者
        message.setFrom(username);
        //接收者
        message.setTo(to);
        //主题
        message.setSubject("修改pre系统用户邮箱");
        //邮件内容
        // 验证码
        String code = PreUtil.codeGen(4);
        message.setText("【Pre系统】" + ",验证码:" + code + "。" + "你正在使用邮箱验证码修改功能，该验证码仅用于身份验证，请勿透露给他人使用");
        // 发送邮件
        javaMailSender.send(message);
        request.getSession().setAttribute(PreConstant.RESET_MAIL, code.toLowerCase());
    }
}
