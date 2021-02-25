package com.taotao.cloud.bigdata.hudi.multiversion.commits;


import com.taotao.cloud.bigdata.hudi.multiversion.MultiVersionDemo;

import java.util.Map;

public abstract class CommitStrategyMultiVersion extends MultiVersionDemo {
    public CommitStrategyMultiVersion(Map<String, String> properties, String basePath) {
        super(properties, basePath);
    }
}
