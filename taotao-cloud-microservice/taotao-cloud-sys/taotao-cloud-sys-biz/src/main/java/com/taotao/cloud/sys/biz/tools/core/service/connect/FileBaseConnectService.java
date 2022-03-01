package com.taotao.cloud.sys.biz.tools.core.service.connect;

import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.sys.biz.tools.core.aspect.SerializerToFile;
import com.taotao.cloud.sys.biz.tools.core.exception.SystemMessage;
import com.taotao.cloud.sys.biz.tools.core.exception.ToolException;
import com.taotao.cloud.sys.biz.tools.core.security.UserService;
import com.taotao.cloud.sys.biz.tools.core.service.connect.dtos.ConnectInput;
import com.taotao.cloud.sys.biz.tools.core.service.connect.dtos.ConnectOutput;
import com.taotao.cloud.sys.biz.tools.core.service.connect.dtos.ConnectTemplate;
import com.taotao.cloud.sys.biz.tools.core.service.connect.events.DeleteSecurityConnectEvent;
import com.taotao.cloud.sys.biz.tools.core.service.connect.events.UpdateSecurityConnectEvent;
import com.taotao.cloud.sys.biz.tools.core.service.file.FileManager;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class FileBaseConnectService extends ConnectService implements InitializingBean {

    private final FileManager fileManager;
    private final ApplicationContext applicationContext;

    /**
     * 保存的连接信息 module => baseName => ConnectOutput
     */
    private static final Map<String, Map<String, ConnectOutput>> connectInfoMap = new ConcurrentHashMap<>();

    /**
     * 保存的连接模板信息 module => ConnectTemplate
     */
    private static final Map<String, ConnectTemplate> connectTemplates = new HashMap<>();

    @Autowired(required = false)
    private UserService userService;

    public FileBaseConnectService(FileManager fileManager, ApplicationContext applicationContext) {
        this.fileManager = fileManager;
        this.applicationContext = applicationContext;
    }

    @Override
    public List<String> modules() {
//        return new ArrayList<>(connectInfoMap.keySet());
        return new ArrayList<>(connectTemplates.keySet());
    }

    @Override
    public void deleteModule(String module) {
        throw new ToolException("不支持模块删除");
    }

    @Override
    public void createModule(String name) {
//        connectInfoMap.computeIfAbsent(name,key -> new ConcurrentHashMap<>());
        throw new ToolException("不支持创建模块");
    }

    @Override
    public ConnectTemplate connectTemplate(String module) {
        return connectTemplates.get(module);
    }

    @Override
    @SerializerToFile
    public void updateConnect(ConnectInput connectInput) throws IOException {
        // 填充元数据
        final String module = connectInput.getModule();
        final String baseName = connectInput.getBaseName();
        final Map<String, ConnectOutput> connectOutputMap = connectInfoMap.computeIfAbsent(module, value -> new ConcurrentHashMap<>(16));
        ConnectOutput connectOutput = connectOutputMap.get(baseName);
        String username = userService != null ? userService.username() : null;
        if (connectOutput != null){
            // 只有当不需要权限或者是本人或者 admin , 才能修改连接信息
            final String lastUpdateUser = connectOutput.getLastUpdateUser();
            if (userService == null || "admin".equals(username) || username.equals(lastUpdateUser)) {
                connectOutput.setConnectInput(connectInput);
                connectOutput.setLastUpdateTime(new Date());
                connectOutput.setLastUpdateUser(username);
            }else{
                throw new ToolException("当前连接"+ baseName +"已经存在,你无权限更改");
            }
        }else{
            connectOutput = new ConnectOutput(Math.round(Math.random() * 1000), connectInput, username, new Date(), new Date(), 0, module + "/" + baseName);
            connectOutputMap.put(baseName,connectOutput);
        }

        // 填充内容
        final File connectBase = fileManager.mkConfigDir("connectBase");
        final File file = new File(connectBase, connectOutput.getPath());
        file.getParentFile().mkdirs();
        FileUtils.writeStringToFile(file,connectInput.getContent(),StandardCharsets.UTF_8);

        // 不放太多的字符串在内存
        connectInput.setContent(null);

        applicationContext.publishEvent(new UpdateSecurityConnectEvent(connectInput));
    }

    @Override
    @SerializerToFile
    public void deleteConnect(String module, String baseName) {
        // 删除内存中连接
        final ConnectOutput connect = findConnect(module, baseName);
        connectInfoMap.get(module).remove(baseName);

        // 删除文件信息
        final File connectBase = fileManager.mkConfigDir("connectBase");
        final File file = new File(connectBase, connect.getPath());
        if (file.exists()){
            final boolean delete = file.delete();
            if (!delete){
	            LogUtil.warn("文件删除失败:{}",file.toPath());
            }
        }

        // 发布删除通知
        applicationContext.publishEvent(new DeleteSecurityConnectEvent(connect));
    }

    @Override
    protected void checkAccess(String module, String connName) {
        if (userService == null){
            LogUtil.debug("不需要权限验证,当前用户有连接{}:{}的访问权限",module,connName);
            return ;
        }
        final ConnectOutput connect = findConnect(module, connName);
        final String group = connect.getConnectInput().getGroup();
        final Path connectGroupPath = Paths.get(group);
        final List<String> accessGroups = userService.queryAccessGroups();
        for (String accessGroup : accessGroups) {
            final Path userGroupPath = Paths.get(accessGroup);
            if (connectGroupPath.startsWith(userGroupPath)){
                // 找到路径, 当前用户有访问权限
                return ;
            }
        }
        throw SystemMessage.ACCESS_DENIED_ARGS.exception("您没有权限操作此数据:"+connName);
    }


    @Override
    public ConnectOutput findConnect(String module, String baseName) {
        final Map<String, ConnectOutput> connectOutputMap = connectInfoMap.get(module);
        if (connectOutputMap != null){
            return connectOutputMap.get(baseName);
        }
        throw new ToolException("不存在的连接配置:"+module+"/"+baseName);
    }

    /**
     * 使用连接中的组织信息进行权限过滤
     * @return 所有可用的连接列表
     */
    @Override
    protected List<ConnectOutput> connectsInternal() {
        List<ConnectOutput> outputs = new ArrayList<>();

        final List<String> groups = userService != null ? userService.user().getGroups() : new ArrayList<>();

        for (Map<String, ConnectOutput> next : connectInfoMap.values()) {
            final Collection<ConnectOutput> values = next.values();
            A: for (ConnectOutput value : values) {
                final ConnectInput connectInput = value.getConnectInput();
                final String group = connectInput.getGroup();
                final Path connectGroupPath = Paths.get(group);
                if (userService != null) {
                    for (String userGroup : groups) {
                        final Path userGroupPath = Paths.get(userGroup);
                        if (connectGroupPath.startsWith(userGroupPath)) {
                            outputs.add(value);
                            continue A;
                        }
                    }
                } else {
                    outputs.add(value);
                }
            }
        }
        return outputs;
    }

    @Override
    public void visitConnect(String module, String baseName) {
        final Map<String, ConnectOutput> connectOutputMap = connectInfoMap.get(module);
        if (connectOutputMap != null){
            final ConnectOutput connectOutput = connectOutputMap.get(baseName);
            if (connectOutput != null){
                connectOutput.setLastAccessTime(new Date());
            }
        }
    }

    @Override
    public String loadContent(String module, String baseName) throws IOException {
        final Map<String, ConnectOutput> connectOutputMap = connectInfoMap.get(module);
        if (connectOutputMap != null){
            final ConnectOutput connectOutput = connectOutputMap.get(baseName);
            if (connectOutput != null){
                if (userService != null) {
                    // 检查数据权限
                    final ConnectInput connectInput = connectOutput.getConnectInput();
                    final String group = connectInput.getGroup();
                    final List<String> groups = userService.user().getGroups();
                    boolean hasSecurity = false;
                    for (String userGroup : groups) {
                        if (group.startsWith(userGroup)){
                            hasSecurity = true;
                            break;
                        }
                    }

                    if (!hasSecurity){
                        throw new ToolException("您没有权限访问这个连接数据");
                    }
                }

                final String path = connectOutput.getPath();
                final File connectBase = fileManager.mkConfigDir("connectBase");
                final File file = new File(connectBase, path);
                return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            }
        }
        throw new ToolException("连接信息不存在或已被删除:"+module+"/"+baseName);
    }

    /**
     * 序列化连接元数据到文件
     */
    public void serializer() throws IOException {
        final File connectBase = fileManager.mkConfigDir("connectBase");
        final Iterator<Map.Entry<String, Map<String, ConnectOutput>>> moduleEntryIterator = connectInfoMap.entrySet().iterator();
        List<String> lines = new ArrayList<>();
        while (moduleEntryIterator.hasNext()){
            final Map.Entry<String, Map<String, ConnectOutput>> topEntry = moduleEntryIterator.next();
            final String module = topEntry.getKey();
            for (Map.Entry<String, ConnectOutput> entry : topEntry.getValue().entrySet()) {
                final String baseName = entry.getKey();
                final ConnectOutput connectOutput = entry.getValue();
                final ConnectInput connectInput = connectOutput.getConnectInput();
                final String[] fields = {connectOutput.getId() + "", module, baseName, connectInput.getConfigTypeName(), connectInput.getGroup(),
                        connectOutput.getLastUpdateUser(), connectOutput.getLastUpdateTime().getTime() + "", connectOutput.getLastAccessTime().getTime() + "",
                        connectOutput.getLinkErrorCount() + "", connectOutput.getPath()};
                String line = StringUtils.join(fields, ":");

                lines.add(line);
            }
        }
        final File info = new File(connectBase, "info");
        FileUtils.writeLines(info,lines,false);
    }


    /**
     * $configDir[DIR]
     *   connectBase[Dir]
     *     info[File] 存储连接元数据
     *     $module1[Dir]
     *       $baseName => content
     *     $module2[Dir]
     *
     *  info 格式
     *  id:模块:基础名:类加载器名称:类名:配置类型:分组:上次更新人:上次访问时间:连接失败次数:相对路径
     *  id:module:baseName:configTypeName:group:lastUpdateUser:lastUpdateTime:lastAccessTime:linkErrorCount:path
     *  会根据上次访问时间进行连接顺序排序
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // 加载之前的连接元数据
        final File connectBase = fileManager.mkConfigDir("connectBase");
        final File info = new File(connectBase, "info");
        if (!info.exists()) {
            info.createNewFile();
            return ;
        }
        final List<String> connectLines = FileUtils.readLines(info, StandardCharsets.UTF_8);
        for (String connectLine : connectLines) {
            final String[] split = StringUtils.splitPreserveAllTokens(connectLine, ":", 11);
            final ConnectInput connectInput = new ConnectInput(split[1], split[2], split[3],split[4]);
            final ConnectOutput connectOutput = new ConnectOutput(NumberUtils.toLong(split[0]), connectInput, split[5], new Date(NumberUtils.toLong(split[6])), new Date(NumberUtils.toLong(split[7])), NumberUtils.toInt(split[8]), split[9]);
            final Map<String, ConnectOutput> connectOutputMap = connectInfoMap.computeIfAbsent(connectInput.getModule(),value -> new ConcurrentHashMap<>(16));
            connectOutputMap.put(connectInput.getBaseName(), connectOutput);
        }

        // 加载可用的模块, 每个模块自己注册连接模板, 只有注册了连接模板的模块才能创建连接 connect.[模板名].template.[文件格式]
        final Resource[] resources = applicationContext.getResources("classpath*:connect.*.template*");
        for (Resource resource : resources) {
            final String filename = resource.getFilename();
            final String[] filenameSplit = StringUtils.splitPreserveAllTokens(filename, '.');
            final ConnectTemplate connectTemplate = new ConnectTemplate(filenameSplit[1], resource, filenameSplit[3]);
            // 读取资源
            final String template = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
            connectTemplate.setContent(template);
            connectTemplates.put(connectTemplate.getModule(),connectTemplate);
        }
    }
}
