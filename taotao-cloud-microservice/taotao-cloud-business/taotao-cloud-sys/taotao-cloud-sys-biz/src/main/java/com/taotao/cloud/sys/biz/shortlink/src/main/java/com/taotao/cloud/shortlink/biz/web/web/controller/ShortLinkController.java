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

package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.web.web.controller;

import com.taotao.cloud.shortlink.biz.web.service.IShortLinkService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * This is Description
 *
 * @since 2022/05/06
 */
@Slf4j
@Controller
public class ShortLinkController {

    //    @Resource
    //    private ShortLinkBiz shortLinkBiz;

    @Resource(name = "shortLinkServiceSimpleImpl")
    private IShortLinkService shortLinkService;

    // TODO 临时使用该方法，方便测试
    @GetMapping(path = "/{shortLinkCode}")
    @ResponseBody
    public String dispatch(
            @PathVariable(name = "shortLinkCode") String shortLinkCode,
            HttpServletRequest request,
            HttpServletResponse response) {
        try {
            Optional<String> originUrlOpt = shortLinkService.parseShortLinkCode(shortLinkCode);
            if (!originUrlOpt.isPresent()) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return "查找不到，code -> " + shortLinkCode;
            }

            return originUrlOpt.get();
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            log.info("dispatch: 请求短链时异常, ,code -> {},e -> {}", shortLinkCode, e.toString());
        }

        return "";
    }

    //    @GetMapping(path = "/{shortLinkCode}")
    //    public void dispatch(@PathVariable(name = "shortLinkCode") String shortLinkCode,
    //                         HttpServletRequest request, HttpServletResponse response) {
    //        try {
    //            Optional<String> originUrlOpt = shortLinkBiz.parseShortLinkCode(shortLinkCode);
    //            if (!originUrlOpt.isPresent()){
    //                response.setStatus(HttpStatus.NOT_FOUND.value());
    //                return;
    //            }
    //
    //            // TODO test
    //            response.setHeader("Location", originUrlOpt.get());
    //            response.setStatus(HttpStatus.FOUND.value());
    //        } catch (Exception e) {
    //            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    //            log.info("dispatch: 请求短链时异常, ,code -> {},e -> {}",shortLinkCode,e.toString());
    //        }
    //    }

    @GetMapping(path = "")
    @ResponseBody
    public String test() {
        return "跳转成功！！！";
    }
}
