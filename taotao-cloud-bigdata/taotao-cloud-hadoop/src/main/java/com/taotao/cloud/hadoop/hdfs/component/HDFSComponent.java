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

package com.taotao.cloud.hadoop.hdfs.component;

import com.taotao.cloud.hadoop.hdfs.configuration.HdfsConfiguration;
import com.taotao.cloud.hadoop.hdfs.utils.HDFSUtil;
import org.apache.hadoop.conf.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * HDFSComponent
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/10/29 15:19
 */
@Component
public class HDFSComponent {

    @Autowired private HdfsConfiguration hdfsConfiguration;

    @Bean
    public HDFSUtil hdfsUtil() {
        // System.setProperty("hadoop.home.dir", "D:\\software\\hadoop-dev\\hadoop-2.7.7");
        HDFSUtil hdfsUtil =
                new HDFSUtil(
                        getConfiguration(),
                        hdfsConfiguration.getPath(),
                        hdfsConfiguration.getUsername());
        return hdfsUtil;
    }

    /**
     * 获取HDFS配置信息
     *
     * @return org.apache.hadoop.conf.Configuration
     * @author shuigedeng
     * @since 2020/10/29 15:22
     */
    private Configuration getConfiguration() {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", hdfsConfiguration.getPath());

        // 参数优先级： 1、客户端代码中设置的值 2、classpath下的用户自定义配置文件 3、然后是服务器的默认配置
        configuration.set("dfs.replication", "1");
        configuration.set("dfs.block.size", "64m");
        return configuration;
    }
}
