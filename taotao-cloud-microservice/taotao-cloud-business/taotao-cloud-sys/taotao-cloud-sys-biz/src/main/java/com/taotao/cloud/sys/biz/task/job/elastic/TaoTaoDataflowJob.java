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

package com.taotao.cloud.sys.biz.task.job.elastic;

import com.taotao.boot.common.utils.log.LogUtils;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.dataflow.job.DataflowJob;
import org.springframework.stereotype.Component;

/**
 * 淘淘数据流工作
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 11:54:23
 */
@Component
public class TaoTaoDataflowJob implements DataflowJob<TaoTaoDataflowJob.Foo> {

    @Override
    public List<Foo> fetchData( final ShardingContext shardingContext ) {
        // 获取数据
        LogUtils.info("MyDataflowJob *******************");
        LogUtils.info(
                "Item : {}, Time: {}, Thread: {}, type:  {}",
                shardingContext.getShardingItem(),
                LocalDateTime.now(),
                Thread.currentThread().getId(),
                "DATAFLOW");

        return new ArrayList<>();
    }

    @Override
    public void processData( final ShardingContext shardingContext, final List<Foo> data ) {
        // 处理数据
        LogUtils.info(
                "Item : {}, Time: {}, Thread: {}, type:  {}",
                shardingContext.getShardingItem(),
                LocalDateTime.now(),
                Thread.currentThread().getId(),
                "DATAFLOW");
    }

    /**
     * Foo
     *
     * @author shuigedeng
     * @version 2026.03
     * @since 2025-12-19 09:30:45
     */
    public static class Foo implements Serializable {

        private final long id;
        private final String location;
        private final String status;

        public Foo( long id, String location, String status ) {
            this.id = id;
            this.location = location;
            this.status = status;
        }

        public long getId() {
            return id;
        }

        public String getLocation() {
            return location;
        }

        public String getStatus() {
            return status;
        }
    }
}
