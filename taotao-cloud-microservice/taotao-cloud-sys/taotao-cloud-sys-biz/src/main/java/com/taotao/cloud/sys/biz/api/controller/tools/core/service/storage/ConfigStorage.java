package com.taotao.cloud.sys.biz.api.controller.tools.core.service.storage;

import java.util.List;
import java.util.Map;

public interface ConfigStorage {
    /**
     * 写入一个配置,如果已经存在, 则是覆盖
     * @param configInfo
     */
    void writeConfig(ConfigInfo configInfo);

    /**
     * @param id 配置编号
     * @return 根据 id 读取配置
     */
    ConfigInfo readConfigById(String id);

    /**
     * @param module 模块名
     * @param group 分组名
     * @return 配置树
     */
    List<ConfigTree> readConfigTree(String module,String group);

    /**
     *
     * @param module 模块名
     * @param group  分组名
     * @param path   配置路径
     * @return 根据路径加载唯一的配置
     */
    ConfigInfo readConfigByUnique(String module,String group,String path);

    /**
     * 加载某个路径下的配置信息
     * @param module
     * @param group
     * @param path
     * @return
     *  config1 => configInfo
     *  config2 => configInfo
     */
    Map<String,ConfigInfo> readConfigsMap(String module,String group,String path);

    /**
     * 删除某个配置
     * @param id
     */
    void deleteConfigById(String id);

    /**
     * 批量删除配置
     * @param module
     * @param group
     * @param path
     */
    void deletePathConfigs(String module,String group,String path);
}
