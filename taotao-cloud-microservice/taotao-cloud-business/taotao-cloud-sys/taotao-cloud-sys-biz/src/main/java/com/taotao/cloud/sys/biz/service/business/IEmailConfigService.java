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

package com.taotao.cloud.sys.biz.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.sys.biz.model.vo.alipay.EmailVO;
import com.taotao.cloud.sys.biz.model.entity.config.EmailConfig;
import org.springframework.scheduling.annotation.Async;

/**
 * 邮件服务
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 14:25:28
 */
public interface IEmailConfigService extends IService<EmailConfig> {

    /**
     * 更新邮件配置
     *
     * @param emailConfig 邮件配置
     * @param old 旧的配置
     * @return 是否更新完成
     */
    Boolean update(EmailConfig emailConfig, EmailConfig old);

    /**
     * 查询配置
     *
     * @return EmailConfig 邮件配置
     */
    EmailConfig find();

    /**
     * 发送邮件
     *
     * @param emailVo 邮件发送的内容
     * @param emailConfig 邮件配置
     * @throws Exception 异常信息
     */
    @Async
    void send(EmailVO emailVo, EmailConfig emailConfig) throws Exception;
}
