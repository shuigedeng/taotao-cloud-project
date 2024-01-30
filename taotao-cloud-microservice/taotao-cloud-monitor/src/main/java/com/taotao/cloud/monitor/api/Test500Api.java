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

package com.taotao.cloud.monitor.api;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.monitor.model.TargetGroup;
import java.util.*;
import org.springframework.web.bind.annotation.*;

/**
 * prometheus http sd
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:01:42
 */
@RestController
@RequestMapping("/test")
public class Test500Api {

    @GetMapping("/api")
    public List<TargetGroup> getList() {
        LogUtils.error(new RuntimeException("测试500失败================="), "测试500失败");
        throw new RuntimeException("测试500失败");
    }
}
