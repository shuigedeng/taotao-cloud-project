package com.taotao.cloud.sys.biz.modules.core.service.connect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cloud.sys.biz.modules.core.dtos.param.AbstractConnectParam;
import com.taotao.cloud.sys.biz.modules.core.dtos.param.DatabaseConnectParam;
import com.taotao.cloud.sys.biz.modules.core.dtos.param.GitParam;
import com.taotao.cloud.sys.biz.modules.core.dtos.param.KafkaConnectParam;
import com.taotao.cloud.sys.biz.modules.core.dtos.param.MongoConnectParam;
import com.taotao.cloud.sys.biz.modules.core.dtos.param.RedisConnectParam;
import com.taotao.cloud.sys.biz.modules.core.dtos.param.SimpleConnectParam;
import com.taotao.cloud.sys.biz.modules.core.exception.ToolException;
import com.taotao.cloud.sys.biz.modules.core.service.connect.dtos.ConnectInput;
import com.taotao.cloud.sys.biz.modules.core.service.connect.dtos.ConnectOutput;
import com.taotao.cloud.sys.biz.modules.core.service.connect.dtos.ConnectTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class ConnectService {
    /**
     * @return 模块列表
     */
    public abstract List<String> modules();

    /**
     * 创建一个模块
     * @param name 模块名
     */
    public abstract void createModule(String name);
    /**
     * 删除模块
     * @param module 模块名
     */
    public abstract void deleteModule(String module);

    /**
     * 连接模板
     * @param module 模块名
     * @return
     */
    public abstract ConnectTemplate connectTemplate(String module);


    /**
     * 创建/更新一个连接
     * @param connectInput 连接信息
     */
    public abstract void updateConnect(ConnectInput connectInput) throws IOException;

    /**
     *
     * @param module 模块名
     * @param baseName 基础名
     * @return 查找到的连接
     */
    public abstract ConnectOutput findConnect(String module, String baseName);

    /**
     * 当权限模块加入时, 这个连接列表会按照权限过滤
     * @return 所有连接列表
     */
    protected abstract List<ConnectOutput> connectsInternal();

    /**
     * 连接列表
     * @return
     */
    public List<ConnectOutput> connects(){
        final List<ConnectOutput> connectOutputs = connectsInternal();
        sortConnects(connectOutputs);
        return connectOutputs;
    }

    private void sortConnects(List<ConnectOutput> connectOutputs) {
        // 连接信息排序, 先按模块排序,再按最近访问时间排序
        Collections.sort(connectOutputs, new Comparator<ConnectOutput>() {
            @Override
            public int compare(ConnectOutput o1, ConnectOutput o2) {
                final ConnectInput connectInput1 = o1.getConnectInput();
                final ConnectInput connectInput2 = o2.getConnectInput();
                final int firstCompare = connectInput1.getModule().compareTo(connectInput2.getModule());
                if (firstCompare != 0){
                    return firstCompare;
                }

                return o1.getLastAccessTime().compareTo(o2.getLastAccessTime());
            }
        });
    }

    /**
     * 查询模块的连接列表
     * @param module 模块名
     * @return
     */
    public List<ConnectOutput> moduleConnects(String module){
        final List<ConnectOutput> connects = connects();

        final List<ConnectOutput> filterConnects = connects.stream().filter(connectOutput -> connectOutput.getConnectInput().getModule().equals(module)).collect(Collectors.toList());

        sortConnects(filterConnects);

        return filterConnects;
    }

    public abstract void visitConnect(String module,String baseName);

    /**
     * 加载连接信息
     * @return 连接信息的文本内容
     */
    public abstract String loadContent(String module, String baseName) throws IOException;

    public abstract File connectFile(String module,String baseName);

    /**
     * 删除一个连接
     * @param module 模块名
     * @param baseName 配置名
     */
    public abstract void deleteConnect(String module,String baseName);

    /**
     * 兼容以前的连接管理
     * @param module
     * @return
     */
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
        throw new ToolException("不支持的连接类");
    }
    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 使用 fastjson 在反序列化 kafka 的时候有问题, 这里换成 jackson
     * @param module 模块名
     * @param connName 连接名
     * @return
     * @throws IOException
     */
    public AbstractConnectParam readConnParams(String module, String connName) throws IOException {
        checkAccess(module,connName);

        final String content = loadContent(module, connName);
        Class<?> paramClass = moduleParamFactory(module);
        if (content == null){
            log.error("当前模块[{}],没有连接[{}]",module,connName);
            throw new ToolException("当前模块" + module + " 没有连接" + connName);
        }
        Object readValue = objectMapper.readValue(content, paramClass);
        return (AbstractConnectParam) readValue;
    }

    /**
     * 检查当前用户是否有连接信息的访问权限
     * @param module
     * @param connName
     */
    protected abstract void checkAccess(String module,String connName);
}
