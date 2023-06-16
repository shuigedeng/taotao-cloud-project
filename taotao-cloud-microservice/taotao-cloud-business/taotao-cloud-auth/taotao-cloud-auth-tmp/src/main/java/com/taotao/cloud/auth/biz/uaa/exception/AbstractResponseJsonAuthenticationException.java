/*
 * MIT License
 * Copyright (c) 2020-2029 YongWu zheng (dcenter.top and gitee.com/pcore and github.com/ZeroOrInfinity)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.taotao.cloud.auth.biz.uaa.exception;

import com.taotao.cloud.auth.biz.uaa.enums.ErrorCodeEnum;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

/**
 * 继承此类后, 如果异常被 {@link SimpleUrlAuthenticationFailureHandler} 处理 Response 会返回 Json 数据
 * @author YongWu zheng
 * @version V1.0  Created by 2020/6/6 12:03
 */
public abstract class AbstractResponseJsonAuthenticationException extends AuthenticationException {
    private static final long serialVersionUID = 2661098918363948470L;

    @Getter
    protected ErrorCodeEnum errorCodeEnum;
    @Getter
    protected Object data;
    /**
     * 可以是用户名, userId, sessionId 等表示用户唯一的属性
     */
    @Getter
    protected String uid;

    public AbstractResponseJsonAuthenticationException(ErrorCodeEnum errorCodeEnum, Throwable t, Object data,
                                                       String uid) {
        super(errorCodeEnum.getMsg(), t);
        this.errorCodeEnum = errorCodeEnum;
        this.data = data;
        this.uid = uid;
    }

    public AbstractResponseJsonAuthenticationException(ErrorCodeEnum errorCodeEnum, Object data, String uid) {
        super(errorCodeEnum.getMsg());
        this.errorCodeEnum = errorCodeEnum;
        this.data = data;
        this.uid = uid;
    }
}
