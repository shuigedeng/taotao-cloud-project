package com.taotao.cloud.sys.biz.modules.core.service.storage;

import com.taotao.cloud.sys.biz.modules.core.service.file.FileManagerProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * $configDir
 *   groups
 *     $group
 *       modules
 *         $module
 *           $path
 *            文件名(取路径最后一段) => 数据内容
 *    index , 创建一个配置索引库, 快速查找某一个配置
 */
//@Service
public class FileConfigStorage implements ConfigStorage , InitializingBean {
    private FileManagerProperties fileManagerProperties;

    @Autowired
    public FileConfigStorage(FileManagerProperties fileManagerProperties) {
        this.fileManagerProperties = fileManagerProperties;
    }

    @Override
    public void writeConfig(ConfigInfo configInfo) {

    }

    @Override
    public ConfigInfo readConfigById(String id) {
        return null;
    }

    @Override
    public List<ConfigTree> readConfigTree(String module, String group) {
        return null;
    }

    @Override
    public ConfigInfo readConfigByUnique(String module, String group, String path) {
        return null;
    }

    @Override
    public Map<String, ConfigInfo> readConfigsMap(String module, String group, String path) {
        return null;
    }

    @Override
    public void deleteConfigById(String id) {

    }

    @Override
    public void deletePathConfigs(String module, String group, String path) {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        File configs = fileManagerProperties.getConfigs();
        File base = fileManagerProperties.getBase();

        if (configs != null && !configs.exists()){
            configs.mkdirs();
        }

        if (base != null){
            if (!base.exists()){
                base.mkdirs();
            }
            if (configs == null){
                configs = new File(base, "configs");
                configs.mkdir();
                fileManagerProperties.setConfigs(configs);
            }
        }

        Assert.notNull(configs, "configs Dir cannot be null");
    }

    public void updateIndex(){

    }
}
