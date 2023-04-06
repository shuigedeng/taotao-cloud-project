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

package com.taotao.cloud.im.biz.platform.common.web.exception;

import com.platform.common.enums.ResultCodeEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.web.domain.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/** 全局异常处理器 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e) {
        log.error("全局异常:" + e.getMessage(), e);
        /** 路径不存在 */
        if (e instanceof NoHandlerFoundException
                || e instanceof org.springframework.web.HttpRequestMethodNotSupportedException) {
            return AjaxResult.result(ResultCodeEnum.NOT_FOUND);
        }
        /** 校验异常 */
        if (e instanceof MethodArgumentNotValidException) {
            return AjaxResult.fail(((MethodArgumentNotValidException) e)
                    .getBindingResult()
                    .getFieldError()
                    .getDefaultMessage());
        }
        /** 自定义异常 */
        if (e instanceof BaseException) {
            if (ResultCodeEnum.VERSION.equals(((BaseException) e).getResultCode())) {
                return AjaxResult.result(ResultCodeEnum.VERSION);
            }
            return AjaxResult.fail(e.getMessage());
        }
        return AjaxResult.fail();
    }
}
