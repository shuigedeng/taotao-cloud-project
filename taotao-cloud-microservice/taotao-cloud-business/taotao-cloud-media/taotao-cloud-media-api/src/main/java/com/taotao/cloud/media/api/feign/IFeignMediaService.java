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

package com.taotao.cloud.media.api.feign;

 import com.taotao.boot.common.constant.ServiceNameConstants;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.media.api.feign.fallback.FeignMediaFallback;
import com.taotao.cloud.media.api.model.vo.FileVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 远程调用售后模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(
        contextId = "remoteChatbotService",
        value = ServiceNameConstants.TAOTAO_CLOUD_FILE,
        fallbackFactory = FeignMediaFallback.class)
public interface IFeignMediaService {

    /**
     * 根据id查询文件信息
     *
     * @param id id
     * @return com.taotao.boot.core.model.Result<com.taotao.cloud.dfs.api.vo.FileVO>
     * @author shuigedeng
     * @since 2020/11/20 上午11:17
     */
    @GetMapping("/file/info/id/{id:[0-9]*}")
    public Result<FileVO> findFileById(@PathVariable(value = "id") Long id);
}
