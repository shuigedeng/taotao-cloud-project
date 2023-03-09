/*
 * MIT License
 * Copyright <2021-2022>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * @Author: Sinda
 * @Email:  xhuicloud@163.com
 */

package com.taotao.cloud.logs.api.feign;

import com.xhuicloud.common.core.constant.AuthorizationConstants;
import com.xhuicloud.common.core.constant.ServiceNameConstants;
import com.xhuicloud.common.core.utils.Response;
import com.xhuicloud.logs.entity.SysLogLogin;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(contextId = SysLogLoginFeign.SYSLOGLOGINFEIGN, value = ServiceNameConstants.XHUICLOUD_LOGS_SERVICE, path = "/sysLogLogin")
public interface SysLogLoginFeign {

    String SYSLOGLOGINFEIGN = "sysLogLoginFeign";

    @PostMapping
    Response save(@RequestBody SysLogLogin sysLogLogin,  @RequestHeader(AuthorizationConstants.FROM) String from);

}
