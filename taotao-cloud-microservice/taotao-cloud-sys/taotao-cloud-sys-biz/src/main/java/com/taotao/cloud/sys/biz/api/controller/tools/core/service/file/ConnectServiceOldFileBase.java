package com.taotao.cloud.sys.biz.api.controller.tools.core.service.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.taotao.cloud.sys.biz.api.controller.tools.core.exception.ToolException;
import com.taotao.cloud.sys.biz.api.controller.tools.core.security.UserService;
import com.taotao.cloud.sys.biz.api.controller.tools.core.dtos.ConfigPath;
import com.taotao.cloud.sys.biz.api.controller.tools.core.dtos.ConnectDto;
import com.taotao.cloud.sys.biz.api.controller.tools.core.dtos.UpdateConnectEvent;
import com.taotao.cloud.sys.biz.api.controller.tools.core.dtos.param.AbstractConnectParam;
import com.taotao.cloud.sys.biz.api.controller.tools.core.dtos.param.ConnectParam;
import com.taotao.cloud.sys.biz.api.controller.tools.core.dtos.param.DatabaseConnectParam;
import com.taotao.cloud.sys.biz.api.controller.tools.core.dtos.param.GitParam;
import com.taotao.cloud.sys.biz.api.controller.tools.core.dtos.param.KafkaConnectParam;
import com.taotao.cloud.sys.biz.api.controller.tools.core.dtos.param.MongoConnectParam;
import com.taotao.cloud.sys.biz.api.controller.tools.core.dtos.param.RedisConnectParam;
import com.taotao.cloud.sys.biz.api.controller.tools.core.dtos.param.SimpleConnectParam;
import com.taotao.cloud.sys.biz.api.controller.tools.core.utils.NetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConnectServiceOldFileBase {
    @Autowired
    private FileManager fileManager;

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired(required = false)
    private UserService userService;

    /**
     * 连接都保存在这个目录
     */
    public static final String MODULE = "connect";

    /**
     * 测试连接是否是通的
     * @param connectParam
     */
    public void testConnectReachable(ConnectParam connectParam){
        String host = connectParam.getHost();
        int port = connectParam.getPort();
        boolean hostConnectable = NetUtil.isHostConnectable(host, port);
        if (!hostConnectable){
            throw new ToolException("连接失败 "+host+":"+port);
        }
    }

    /**
     * 模块列表
     * @return
     */
    public List<String> modules(){
        List<ConfigPath> configPaths = fileManager.configNames(MODULE);
        return configPaths.stream().map(ConfigPath::getPathName).collect(Collectors.toList());
    }

    /**
     * 指定模块下的连接列表
     * @param module
     * @return
     */
    public List<String> names(String module){
        return fileManager.simpleConfigNames(MODULE,module);
    }

    /**
     * 获取连接详情
     * @param module
     * @param connName
     * @return
     */
    public String content(String module,String connName) throws IOException {
        return fileManager.readConfig(MODULE,module+"/"+connName);
    }

    /**
     * 创建连接
     * @param module
     * @param data
     * @throws IOException
     */
    public void createConnect(String module,String data) throws IOException {
        Class<?> param = moduleParamFactory(module);
        if(param == null){
            throw new IllegalArgumentException("模块配置不受支持:"+module+" 支持的模块列表为[database,kafka,redis,mongo,zookeeper]");
        }
        AbstractConnectParam abstractConnectParam = (AbstractConnectParam) JSON.parseObject(data, param);
        ConnectParam connectParam = abstractConnectParam.getConnectParam();
        if (connectParam != null) {
            NetUtil.isHostConnectable(connectParam.getHost(), connectParam.getPort());
        }

        String connName = abstractConnectParam.getConnectIdParam().getConnName();
        fileManager.writeConfig(MODULE,module+"/"+connName, data);
        applicationContext.publishEvent(new UpdateConnectEvent(new UpdateConnectEvent.ConnectInfo(connName,data,param,module)));
    }

    private Class<?> moduleParamFactory(String module) {
        switch (module){
            case "database":
                return DatabaseConnectParam.class;
            case "kafka":
                return KafkaConnectParam.class;
            case "redis":
                return RedisConnectParam.class;
            case "zookeeper":
            case "elasticsearch":
                return SimpleConnectParam.class;
            case "mongo":
               return MongoConnectParam.class;
            case "git":
                return GitParam.class;
            default:
        }
        return null;
    }

    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 使用 fastjson 在反序列化 kafka 的时候有问题, 这里换成 jackson
     * @param module 模块名
     * @param connName 连接名
     * @return
     * @throws IOException
     */
    public AbstractConnectParam readConnParams(String module,String connName) throws IOException {
        String content = content(module, connName);
        Class<?> paramClass = moduleParamFactory(module);
        if (content == null){
            log.error("当前模块[{}],没有连接[{}]",module,connName);
            throw new ToolException("当前模块" + module + " 没有连接" + connName);
        }
//        AbstractConnectParam abstractConnectParam = (AbstractConnectParam) JSON.parseObject(content, paramClass);
        Object readValue = objectMapper.readValue(content, paramClass);
        return (AbstractConnectParam) readValue;
    }

    /**
     * 列出所有连接
     * @return
     */
    public List<ConnectDto> selectAll() {
        List<ConnectDto> connects = new ArrayList<>();
        List<ConfigPath> configPaths = fileManager.configNames(MODULE);
        for (ConfigPath configPath : configPaths) {
            String childModule = configPath.getPathName();
            List<ConfigPath> childs = fileManager.configChildNames(MODULE, childModule);
            for (ConfigPath child : childs) {
                File file = child.getFile();
                long lastModified = file.lastModified();
                ConnectDto connect = new ConnectDto(childModule, file.getName(), new Date(lastModified));
                connects.add(connect);
            }
        }
        return connects;
    }

    /**
     * 删除连接文件
     * @param module
     * @param connName
     */
    public void dropConnect(String module, String connName) {
        fileManager.dropConfig(MODULE,module+"/"+connName);
    }

    /**
     * 建立新模块
     * @param name
     */
    public void createModule(String name) {
        fileManager.mkConfigDir(MODULE+"/"+name);
    }

    /**
     * 删除模块, 会把下面的连接一并删除
     * 注: 不会关闭连接
     * @param name
     */
    public void dropModule(String name) throws IOException {
        fileManager.dropConfig(MODULE + "/" + name);
    }

//    @PostConstruct
//    public void register(){
//        pluginManager.register(PluginDto.builder().module("core").name("connect").author("9420").desc("管理所有连接,支撑模块").build());
//    }
}
