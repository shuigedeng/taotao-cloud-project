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

package com.taotao.cloud.sys.biz.controller.business.tools;

import com.taotao.cloud.sys.biz.service.business.IPinYinService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * PinYinController
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022/03/03 14:57
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "工具管理端-拼音管理API", description = "工具管理端-拼音管理API")
@RequestMapping("/sys/tools/pinyin")
public class PinYinController {

    private final IPinYinService pinYinService;
}
