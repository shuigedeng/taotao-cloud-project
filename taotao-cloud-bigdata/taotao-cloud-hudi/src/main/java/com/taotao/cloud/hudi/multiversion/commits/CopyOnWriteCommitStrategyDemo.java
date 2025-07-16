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

package com.taotao.cloud.hudi.multiversion.commits;

import com.taotao.cloud.hudi.common.CustomDataGenerator;
import com.taotao.cloud.hudi.common.OpType;
import com.taotao.cloud.hudi.multiversion.MultiVersionDemo;
import java.util.HashMap;
import java.util.Map;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;

/**
 * KEEP_LATEST_COMMITS Strategy, will retain two version of files.
 */
public class CopyOnWriteCommitStrategyDemo extends CommitStrategyMultiVersion {
    private static String basePath = "/tmp/multiversion/commits/copyonwrite/";

    public CopyOnWriteCommitStrategyDemo(Map<String, String> properties) {
        super(properties, basePath);
    }

    public static void main(String[] args) {
        Map<String, String> config = new HashMap<>();
        // use HoodieCleaningPolicy.KEEP_LATEST_COMMITS and retains max 3 commits in the timeline
        // and 2(1 + 1) version of data files.
        config.put("hoodie.keep.max.commits", "3");
        config.put("hoodie.keep.min.commits", "2");
        config.put("hoodie.cleaner.commits.retained", "1");
        MultiVersionDemo cowMultiVersionDemo = new CopyOnWriteCommitStrategyDemo(config);

        Dataset<Row> dataset = CustomDataGenerator.getCustomDataset(10, OpType.INSERT, spark);

        cowMultiVersionDemo.writeHudi(dataset, SaveMode.Overwrite);

        // update 10 times to shanghai partition.
        for (int i = 0; i < 10; i++) {
            dataset = CustomDataGenerator.getCustomDataset(10, OpType.UPDATE, i, "shanghai", spark);
            cowMultiVersionDemo.writeHudi(dataset, SaveMode.Append);
        }
    }

    @Override
    public String tableType() {
        return "COPY_ON_WRITE";
    }
}
