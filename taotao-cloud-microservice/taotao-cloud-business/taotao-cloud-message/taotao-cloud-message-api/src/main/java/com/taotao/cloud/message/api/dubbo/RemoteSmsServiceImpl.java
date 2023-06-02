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

package com.taotao.cloud.message.api.dubbo;
//
// import org.dromara.hutoolcore.bean.BeanUtil;
// import com.ruoyi.common.core.exception.ServiceException;
// import com.ruoyi.common.core.utils.SpringUtils;
// import com.ruoyi.common.sms.config.properties.SmsProperties;
// import com.ruoyi.common.sms.core.SmsTemplate;
// import com.ruoyi.common.sms.entity.SmsResult;
// import com.ruoyi.resource.api.RemoteSmsService;
// import com.ruoyi.resource.api.domain.SysSms;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.apache.dubbo.config.annotation.DubboService;
// import org.springframework.stereotype.Service;
//
// import java.util.Map;
//
/// **
// * 短信服务
// *
// * @author Lion Li
// */
// @Slf4j
// @RequiredArgsConstructor
// @Service
// @DubboService
// public class RemoteSmsServiceImpl implements RemoteSmsService {
//
//    private final SmsProperties smsProperties;
//
//    /**
//     * 发送短信
//     *
//     * @param phones     电话号(多个逗号分割)
//     * @param templateId 模板id
//     * @param param      模板对应参数
//     */
//    public SysSms send(String phones, String templateId, Map<String, String> param) throws
// ServiceException {
//        if (!smsProperties.getEnabled()) {
//            throw new ServiceException("当前系统没有开启短信功能！");
//        }
//        SmsTemplate smsTemplate = SpringUtils.getBean(SmsTemplate.class);
//        SmsResult smsResult = smsTemplate.send(phones, templateId, param);
//        return BeanUtil.toBean(smsResult, SysSms.class);
//    }
//
// }
