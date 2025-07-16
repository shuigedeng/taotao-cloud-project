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

package com.taotao.cloud.hadoop.mr.service.impl;

import com.taotao.cloud.hadoop.mr.component.ReduceJobsUtils;
import com.taotao.cloud.hadoop.mr.service.MapReduceService;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * MapReduceServiceImpl
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/10/30 17:40
 */
@Service
public class MapReduceServiceImpl implements MapReduceService {

    // 默认reduce输出目录
    private static final String OUTPUT_PATH = "/output";

    @Override
    public void groupSort(String jobName, String inputPath)
            throws InterruptedException, IOException, ClassNotFoundException {
        if (StringUtils.isEmpty(jobName) || StringUtils.isEmpty(inputPath)) {
            return;
        }
        // 输出目录 = output/当前Job
        String outputPath = OUTPUT_PATH + "/" + jobName;
        // if (HdfsService.existFile(outputPath)) {
        // 	HdfsService.deleteFile(outputPath);
        // }
        ReduceJobsUtils.groupSort(jobName, inputPath, outputPath);
    }
}
