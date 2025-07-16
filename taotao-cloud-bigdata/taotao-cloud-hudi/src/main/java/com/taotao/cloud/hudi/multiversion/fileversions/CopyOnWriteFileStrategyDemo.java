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

package com.taotao.cloud.hudi.multiversion.fileversions;

import com.taotao.cloud.hudi.common.CustomDataGenerator;
import com.taotao.cloud.hudi.common.OpType;
import com.taotao.cloud.hudi.multiversion.MultiVersionDemo;
import com.taotao.cloud.hudi.multiversion.commits.CommitStrategyMultiVersion;
import java.util.HashMap;
import java.util.Map;
import org.apache.hudi.common.model.HoodieCleaningPolicy;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;

/**
 * KEEP_LATEST_FILE_VERSIONS Strategy, will retain one version of files.
 */
public class CopyOnWriteFileStrategyDemo extends CommitStrategyMultiVersion {
    private static String basePath = "/tmp/multiversion/file/copyonwrite/";

    public CopyOnWriteFileStrategyDemo(Map<String, String> properties) {
        super(properties, basePath);
    }

    public static void main(String[] args) {
        Map<String, String> config = new HashMap<>();
        // use HoodieCleaningPolicy.KEEP_LATEST_FILE_VERSIONS and retains max 1 version of data
        // files.
        config.put("hoodie.cleaner.fileversions.retained", "1");
        config.put("hoodie.cleaner.policy", HoodieCleaningPolicy.KEEP_LATEST_FILE_VERSIONS.name());

        MultiVersionDemo cowMultiVersionDemo = new CopyOnWriteFileStrategyDemo(config);

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
