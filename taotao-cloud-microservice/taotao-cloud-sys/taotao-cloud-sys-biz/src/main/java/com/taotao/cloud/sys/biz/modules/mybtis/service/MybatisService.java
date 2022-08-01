package com.taotao.cloud.sys.biz.modules.mybtis.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.sanri.tools.modules.database.service.JdbcDataService;
import com.sanri.tools.modules.database.service.connect.ConnDatasourceAdapter;
import com.sanri.tools.modules.database.service.dtos.data.DynamicQueryDto;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.sanri.tools.modules.classloader.ClassloaderService;
import com.sanri.tools.modules.core.service.file.FileManager;

import com.sanri.tools.modules.mybatis.dtos.BoundSqlCallParam;
import com.sanri.tools.modules.mybatis.dtos.BoundSqlResponse;
import com.sanri.tools.modules.mybatis.dtos.ProjectDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MybatisService {
    @Autowired
    private FileManager fileManager;

    @Autowired
    private ClassloaderService classloaderService;

    public static final String MODULE = "mybatis";

    @Autowired
    private ConnDatasourceAdapter connDatasourceAdapter;

    /**
     * projectName => Configuration
     */
    private Map<String, Configuration> projectConfigurationMap = new ConcurrentHashMap<>();

    /**
     * projectName ==> classloaderName 类加载器绑定, 主要用于下次重新加载的时候能拿到是哪个类加载器
     */
    private Map<String,String> classloaderBind = new ConcurrentHashMap<>();

    /**
     * 拿到所有的项目,和绑定的类加载器
     */
    public List<ProjectDto> projects(){
        List<ProjectDto> projectDtos = new ArrayList<>();
        Iterator<String> iterator = projectConfigurationMap.keySet().iterator();
        while (iterator.hasNext()){
            String project = iterator.next();
            String classloaderName = classloaderBind.get(project);
            ProjectDto projectDto = new ProjectDto(project, classloaderName);
            projectDtos.add(projectDto);
        }
        return projectDtos;
    }

    /**
     * 上传一个新的 mapper 文件到指定项目
     * @param project
     * @param file
     */
    public void newProjectFile(String project,String classloaderName, MultipartFile file) throws IOException {
        File projectDir = fileManager.mkTmpDir(MODULE + "/" + project);
        final File destFile = new File(projectDir, file.getOriginalFilename());
//        file.transferTo(new File(projectDir,file.getOriginalFilename()));
        FileCopyUtils.copy(file.getInputStream(),new FileOutputStream(destFile));

        loadMapperFile(project,file.getOriginalFilename(),classloaderName);
    }

    /**
     * 加载指定项目 mapper 文件
     * @param project
     * @param fileName
     * @throws IOException
     */
    public void loadMapperFile(String project,String fileName,String classloaderName) throws IOException {
        // 得到类加载器
        ClassLoader classloader = classloaderService.getClassloader(classloaderName);
        Resources.setDefaultClassLoader(classloader);

        Resource resource = fileManager.relativeResource(MODULE + "/" + project + "/" + fileName);
        InputStream inputStream = resource.getInputStream();
        Configuration configuration = projectConfigurationMap.computeIfAbsent(project, k -> {
            classloaderBind.put(k,classloaderName);
            serializer();
            return new Configuration();
        });

        try {
            XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, resource.getFilename(),configuration.getSqlFragments());
            mapperParser.parse();
        }finally {
            IOUtils.closeQuietly(inputStream);
        }

        Resources.setDefaultClassLoader(ClassLoader.getSystemClassLoader());
    }

    /**
     * 将类加载器绑定序列化到文件,方便下次读取
     */
    private void serializer() {
        String collect = classloaderBind.entrySet().stream().map(entry -> StringUtils.join(Arrays.asList(entry.getKey(), entry.getValue()), ':')).collect(Collectors.joining("\n"));
        File moduleDir = fileManager.mkTmpDir(MODULE);
        File bindClassloader = new File(moduleDir, "bindClassloader");
        try {
            FileUtils.writeStringToFile(bindClassloader, collect, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("MybatisService serializer error : {}",e.getMessage(),e);
        }
    }

    /**
     * 获取当前项目所有可执行的 sqlId
     * @param project
     * @return
     */
    public List<String> statementIds(String project){
        Configuration configuration = projectConfigurationMap.computeIfAbsent(project, k -> new Configuration());
        Collection<MappedStatement> mappedStatements = configuration.getMappedStatements();
        List<String> collect = new ArrayList<>();
        for (Object mappedStatement : mappedStatements) {
            if (mappedStatement instanceof MappedStatement){
                collect.add(((MappedStatement) mappedStatement).getId());
            }
        }
        return collect;
    }

    /**
     * 获取 statement 需要填的参数
     * @param project
     * @param statementId
     * @return
     */
    public List<ParameterMapping> statemenetParams(String project, String statementId){
        Configuration configuration = projectConfigurationMap.computeIfAbsent(project, k -> new Configuration());
        MappedStatement mappedStatement = configuration.getMappedStatement(statementId);

        BoundSql boundSql1 = mappedStatement.getBoundSql(new HashMap<>());
        List<ParameterMapping> parameterMappings = boundSql1.getParameterMappings();
        return parameterMappings;
    }

    /**
     * 获取绑定的 sql 语句
     * @return
     */
    public BoundSqlResponse boundSql(BoundSqlCallParam boundSqlParam) throws ClassNotFoundException, IOException, SQLException {
        String project = boundSqlParam.getProject();
        String statementId = boundSqlParam.getStatementId();
        Configuration configuration = projectConfigurationMap.computeIfAbsent(project, k -> new Configuration());
        MappedStatement mappedStatement = configuration.getMappedStatement(statementId);

        String classloaderName = boundSqlParam.getClassloaderName();
        ClassLoader classloader = classloaderService.getClassloader(classloaderName);
        if (classloader == null) {
            classloader = ClassLoader.getSystemClassLoader();
        }

        JSONObject arg = boundSqlParam.getArg();
        String className = boundSqlParam.getClassName();
        Class<?> clazz = classloader.loadClass(className);
        BoundSql boundSql = null;
        if (clazz.isPrimitive() || clazz == String.class){
            String value = arg.getString("value");
            boundSql= mappedStatement.getBoundSql(arg);
        }else {
            Object parameterObject = arg.getObject("value", clazz);
            boundSql = mappedStatement.getBoundSql(parameterObject);
        }

        // 获取 sql 语句, 需要依赖于数据库连接
        Connection connection = connDatasourceAdapter.connection(boundSqlParam.getConnName());

        PreparedStatement statement = null;ResultSet resultSet = null;
        BoundSqlResponse boundSqlResponse;
        try {
            DefaultParameterHandler defaultParameterHandler = new DefaultParameterHandler(mappedStatement,boundSql.getParameterObject(),boundSql);
            statement = connection.prepareStatement(boundSql.getSql());
            defaultParameterHandler.setParameters(statement);

            // 如果为 select 语句 ,则执行得出查询结果,否则告诉前端 sql 语句
            boundSqlResponse = null;
            SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
            if (sqlCommandType == SqlCommandType.SELECT){
                resultSet = statement.executeQuery();
                DynamicQueryDto dynamicQueryDto = JdbcDataService.dynamicQueryProcessor.handle(resultSet);
                dynamicQueryDto.setSql(statement.toString());
                boundSqlResponse = new BoundSqlResponse(sqlCommandType,dynamicQueryDto);
            }else{
                DynamicQueryDto dynamicQueryDto = new DynamicQueryDto(statement.toString());
                boundSqlResponse = new BoundSqlResponse(sqlCommandType,dynamicQueryDto);
            }
        } finally {
            DbUtils.closeQuietly(connection,statement,resultSet);
        }

        return boundSqlResponse;
    }

    /**
     * 当重启项目后,需要手动重新加载所有的 mybatis 配置文件
     * 不做启动加载是因为会延长加载时间,但这个功能用到的可能性并不是太高
     */
    public void reload() throws IOException {
        projectConfigurationMap.clear();
        File moduleDir = fileManager.mkTmpDir(MODULE);
        // 获取类加载器绑定
        File bind = new File(moduleDir, "bindClassloader");
        List<String> lines = FileUtils.readLines(bind, StandardCharsets.UTF_8);
        this.classloaderBind = lines.stream().map(line -> StringUtils.split(line, ':')).collect(Collectors.toMap(arr -> arr[0], arr -> arr[1]));

        File[] files = moduleDir.listFiles();
        for (File project : files) {
            if (project.isFile()){
                continue;
            }
            String projectName = project.getName();
            String classloaderName = classloaderBind.get(projectName);
            File[] mapperFiles = project.listFiles();
            for (File mapperFile : mapperFiles) {
                try {
                    loadMapperFile(projectName,mapperFile.getName(),classloaderName);
                } catch (IOException e) {
                    log.error("MybatisService reload.loadMapperFile error : {}",e.getMessage(),e);
                }
            }
        }
    }
}
