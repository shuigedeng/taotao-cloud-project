/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.sys.api.vo.alipay.EmailVo;
import com.taotao.cloud.sys.biz.entity.EmailConfig;
import com.taotao.cloud.sys.biz.mapper.IEmailConfigMapper;
import com.taotao.cloud.sys.biz.service.IEmailConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;


@Service
//@CacheConfig(cacheNames = "emailConfig")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class EmailConfigServiceImpl extends ServiceImpl<IEmailConfigMapper, EmailConfig> implements
	IEmailConfigService {

	//private final IGenerator generator;
	//
	//public EmailConfigServiceImpl(IGenerator generator) {
	//
	//	this.generator = generator;
	//}

	@Override
//    @CachePut(key = "'1'")
	@Transactional(rollbackFor = Exception.class)
	public void update(EmailConfig emailConfig, EmailConfig old) {
		//try {
		//    if (!emailConfig.getPass().equals(old.getPass())) {
		//        // 对称加密
		//        emailConfig.setPass(EncryptUtils.desEncrypt(emailConfig.getPass()));
		//    }
		//} catch (Exception e) {
		//    e.printStackTrace();
		//}
		//this.save(emailConfig);
	}

	@Override
//    @Cacheable(key = "'1'")
	public EmailConfig find() {
		EmailConfig emailConfig = this.list().get(0);
		return emailConfig;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void send(EmailVo emailVo, EmailConfig emailConfig) {
		//if (emailConfig == null) {
		//    throw new BadRequestException("请先配置，再操作");
		//}
		//// 封装
		//MailAccount account = new MailAccount();
		//account.setHost(emailConfig.getHost());
		//account.setPort(Integer.parseInt(emailConfig.getPort()));
		//account.setAuth(true);
		//try {
		//    // 对称解密
		//    account.setPass(EncryptUtils.desDecrypt(emailConfig.getPass()));
		//} catch (Exception e) {
		//    throw new BadRequestException(e.getMessage());
		//}
		//account.setFrom(emailConfig.getUser() + "<" + emailConfig.getFromUser() + ">");
		//// ssl方式发送
		//account.setSslEnable(true);
		//// 使用STARTTLS安全连接
		//account.setStarttlsEnable(true);
		//String content = emailVo.getContent();
		//// 发送
		//try {
		//    int size = emailVo.getTos().size();
		//    Mail.create(account)
		//            .setTos(emailVo.getTos().toArray(new String[size]))
		//            .setTitle(emailVo.getSubject())
		//            .setContent(content)
		//            .setHtml(true)
		//            //关闭session
		//            .setUseGlobalSession(false)
		//            .send();
		//} catch (Exception e) {
		//    throw new BadRequestException(e.getMessage());
		//}
	}
}
