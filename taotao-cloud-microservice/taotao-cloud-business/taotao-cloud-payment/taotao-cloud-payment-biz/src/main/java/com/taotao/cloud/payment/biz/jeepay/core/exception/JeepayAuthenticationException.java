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
import lombok.Setter;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

/*
 * Spring Security 框架自定义异常类
 *
 * @author terrfly
 * @site https://www.jeequan.com
 * @date 2021/6/15 11:23
 */
@Getter
@Setter
public class JeepayAuthenticationException extends InternalAuthenticationServiceException {

    private BizException bizException;

    public JeepayAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JeepayAuthenticationException(String msg) {
        super(msg);
    }

    public static JeepayAuthenticationException build(String msg) {
        return build(new BizException(msg));
    }

    public static JeepayAuthenticationException build(BizException ex) {

        JeepayAuthenticationException result = new JeepayAuthenticationException(ex.getMessage());
        result.setBizException(ex);
        return result;
    }
}
