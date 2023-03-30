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

package com.taotao.cloud.message.biz.austin.web.service;

import com.taotao.cloud.message.biz.austin.web.vo.DataParam;
import com.taotao.cloud.message.biz.austin.web.vo.amis.EchartsVo;
import com.taotao.cloud.message.biz.austin.web.vo.amis.SmsTimeLineVo;
import com.taotao.cloud.message.biz.austin.web.vo.amis.UserTimeLineVo;

/**
 * 数据链路追踪获取接口
 *
 * @author 3y
 */
public interface DataService {

    /**
     * 获取全链路追踪 用户维度信息
     *
     * @param receiver 接收者
     * @return
     */
    UserTimeLineVo getTraceUserInfo(String receiver);

    /**
     * 获取全链路追踪 消息模板维度信息
     *
     * @param businessId 业务ID（如果传入消息模板ID，则生成当天的业务ID）
     * @return
     */
    EchartsVo getTraceMessageTemplateInfo(String businessId);

    /**
     * 获取短信下发记录
     *
     * @param dataParam
     * @return
     */
    SmsTimeLineVo getTraceSmsInfo(DataParam dataParam);
}
