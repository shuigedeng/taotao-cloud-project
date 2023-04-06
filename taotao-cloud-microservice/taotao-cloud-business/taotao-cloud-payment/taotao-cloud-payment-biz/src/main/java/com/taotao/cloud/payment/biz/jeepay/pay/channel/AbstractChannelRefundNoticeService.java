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

package com.taotao.cloud.payment.biz.jeepay.pay.channel;

import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.payment.biz.jeepay.core.beans.RequestKitBean;
import com.taotao.cloud.payment.biz.jeepay.pay.service.ConfigContextQueryService;
import com.taotao.cloud.payment.biz.jeepay.pay.util.ChannelCertConfigKitBean;
import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/*
 * 实现退款回调接口抽象类
 *
 * @author jmdhappy
 * @site https://www.jeequan.com
 * @date 2021/9/25 23:18
 */
public abstract class AbstractChannelRefundNoticeService implements IChannelRefundNoticeService {

    @Autowired
    private RequestKitBean requestKitBean;

    @Autowired
    private ChannelCertConfigKitBean channelCertConfigKitBean;

    @Autowired
    protected ConfigContextQueryService configContextQueryService;

    @Override
    public ResponseEntity doNotifyOrderNotExists(HttpServletRequest request) {
        return textResp("order not exists");
    }

    @Override
    public ResponseEntity doNotifyOrderStateUpdateFail(HttpServletRequest request) {
        return textResp("update status error");
    }

    /** 文本类型的响应数据 * */
    protected ResponseEntity textResp(String text) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_HTML);
        return new ResponseEntity(text, httpHeaders, HttpStatus.OK);
    }

    /** json类型的响应数据 * */
    protected ResponseEntity jsonResp(Object body) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(body, httpHeaders, HttpStatus.OK);
    }

    /** request.getParameter 获取参数 并转换为JSON格式 * */
    protected JSONObject getReqParamJSON() {
        return requestKitBean.getReqParamJSON();
    }

    /** request.getParameter 获取参数 并转换为JSON格式 * */
    protected String getReqParamFromBody() {
        return requestKitBean.getReqParamFromBody();
    }

    /** 获取文件路径 * */
    protected String getCertFilePath(String certFilePath) {
        return channelCertConfigKitBean.getCertFilePath(certFilePath);
    }

    /** 获取文件File对象 * */
    protected File getCertFile(String certFilePath) {
        return channelCertConfigKitBean.getCertFile(certFilePath);
    }
}
