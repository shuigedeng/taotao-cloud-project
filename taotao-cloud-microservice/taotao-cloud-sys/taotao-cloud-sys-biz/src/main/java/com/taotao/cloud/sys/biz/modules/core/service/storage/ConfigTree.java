package com.taotao.cloud.sys.biz.modules.core.service.storage;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ConfigTree {
    private String name;
    private List<ConfigTree> childes = new ArrayList<>();
    private String path;
    private ConfigInfo origin;
}
