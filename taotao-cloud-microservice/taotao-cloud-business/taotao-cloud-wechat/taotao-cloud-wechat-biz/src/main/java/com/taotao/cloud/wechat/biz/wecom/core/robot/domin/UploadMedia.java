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

package com.taotao.cloud.wechat.biz.wecom.core.robot.domin;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.InputStream;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 企微文件上传
 *
 * @author xxm
 * @since 2022/7/24
 */
@Data
@Accessors(chain = true)
@Schema(title = "企微文件上传")
public class UploadMedia {

    @Schema(description = "文件名称")
    private String filename;

    @Schema(description = "文件类型")
    private String fileType;

    @Schema(description = "文件流")
    private InputStream inputStream;
}
