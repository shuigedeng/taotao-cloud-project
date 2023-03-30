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

package com.taotao.cloud.payment.biz.jeepay.core.exception;

import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/*
 * 响应异常， 一般用于支付接口回调函数
 *
 * @author terrfly
 * @site https://www.jeequan.com
 * @date 2021/6/8 16:31
 */
@Getter
public class ResponseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private ResponseEntity responseEntity;

    /** 业务自定义异常 * */
    public ResponseException(ResponseEntity resp) {
        super();
        this.responseEntity = resp;
    }

    /** 生成文本类型的响应 * */
    public static ResponseException buildText(String text) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_HTML);
        ResponseEntity entity = new ResponseEntity(text, httpHeaders, HttpStatus.OK);
        return new ResponseException(entity);
    }
}
