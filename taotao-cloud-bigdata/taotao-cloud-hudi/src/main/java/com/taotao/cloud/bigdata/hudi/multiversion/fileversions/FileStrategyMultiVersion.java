package com.taotao.cloud.bigdata.hudi.multiversion.fileversions;


import com.taotao.cloud.bigdata.hudi.multiversion.MultiVersionDemo;

import java.util.Map;

public abstract class FileStrategyMultiVersion extends MultiVersionDemo {

    public FileStrategyMultiVersion(Map<String, String> properties, String basePath) {
        super(properties, basePath);
    }
}
