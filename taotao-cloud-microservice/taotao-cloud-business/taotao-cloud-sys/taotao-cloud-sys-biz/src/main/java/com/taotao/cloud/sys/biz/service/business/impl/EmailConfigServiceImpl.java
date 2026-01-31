/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.sys.biz.service.business.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.sys.biz.model.vo.alipay.EmailVO;
import com.taotao.cloud.sys.biz.mapper.IEmailConfigMapper;
import com.taotao.cloud.sys.biz.model.entity.config.EmailConfig;
import com.taotao.cloud.sys.biz.service.business.IEmailConfigService;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
// 默认不使用缓存
// import org.springframework.cache.annotation.CacheConfig;
// import org.springframework.cache.annotation.CacheEvict;
// import org.springframework.cache.annotation.Cacheable;

/**
 * EmailConfigServiceImpl
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Service
@AllArgsConstructor
// @CacheConfig(cacheNames = "emailConfig")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class EmailConfigServiceImpl extends ServiceImpl<IEmailConfigMapper, EmailConfig> implements
        IEmailConfigService {

    @Override
    //    @CachePut(key = "'1'")
    @Transactional(rollbackFor = Exception.class)
    public Boolean update( EmailConfig emailConfig, EmailConfig old ) {
        // try {
        //    if (!emailConfig.getPass().equals(old.getPass())) {
        //        // 对称加密
        //        emailConfig.setPass(EncryptUtils.desEncrypt(emailConfig.getPass()));
        //    }
        // } catch (Exception e) {
        //    LogUtils.error(e);
        // }
        // this.save(emailConfig);
        return true;
    }

    @Override
    //    @Cacheable(key = "'1'")
    public EmailConfig find() {
        EmailConfig emailConfig = this.list().get(0);
        return emailConfig;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void send( EmailVO emailVo, EmailConfig emailConfig ) {
        // if (emailConfig == null) {
        //    throw new BadRequestException("请先配置，再操作");
        // }
        //// 封装
        // MailAccount account = new MailAccount();
        // account.setHost(emailConfig.getHost());
        // account.setPort(Integer.parseInt(emailConfig.getPort()));
        // account.setAuth(true);
        // try {
        //    // 对称解密
        //    account.setPass(EncryptUtils.desDecrypt(emailConfig.getPass()));
        // } catch (Exception e) {
        //    throw new BadRequestException(e.getMessage());
        // }
        // account.setFrom(emailConfig.getUser() + "<" + emailConfig.getFromUser() + ">");
        //// ssl方式发送
        // account.setSslEnable(true);
        //// 使用STARTTLS安全连接
        // account.setStarttlsEnable(true);
        // String content = emailVo.getContent();
        //// 发送
        // try {
        //    int size = emailVo.getTos().size();
        //    Mail.create(account)
        //            .setTos(emailVo.getTos().toArray(new String[size]))
        //            .setTitle(emailVo.getSubject())
        //            .setContent(content)
        //            .setHtml(true)
        //            //关闭session
        //            .setUseGlobalSession(false)
        //            .send();
        // } catch (Exception e) {
        //    throw new BadRequestException(e.getMessage());
        // }
    }
}
