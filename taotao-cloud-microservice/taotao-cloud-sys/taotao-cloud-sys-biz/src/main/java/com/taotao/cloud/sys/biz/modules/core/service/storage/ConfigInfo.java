package com.taotao.cloud.sys.biz.modules.core.service.storage;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * group1(group)
 *   kafka(module)
 *     connect(partPath)  ==> kafka 连接配置 yaml
 *     zk(partPath)
 *       connect(partPath)    ==> zk 连接配置 json
 */
@Data
public class ConfigInfo {
    private String id;
    private String module;
    private String group;
    /**
     * 配置路径,可构成树状结构
     */
    private String path;
    private Object content;
    private ConfigType configType;

    public ConfigInfo(String module, String path, Object content) {
        this.module = module;
        this.group = "DEFAULT_GROUP";
        this.path = path;
        this.content = content;
        this.configType = ConfigType.JSON;
    }

    /**
     *
     */
    public enum ConfigType{
        JSON("TEXT","JSON")
        ,YAML("TEXT","YAML")
        ,NUMBER("NUMBER","NUMBER")
        ,BOOL("BOOL","BOOL")
        ,BIN_JDK("BIN","JDK");
        private String mainType;
        private String subType;

        ConfigType(String mainType, String subType) {
            this.mainType = mainType;
            this.subType = subType;
        }

        @Override
        public String toString() {
            return "ConfigType{" +
                    "mainType='" + mainType + '\'' +
                    ", subType='" + subType + '\'' +
                    '}';
        }
    }
}
