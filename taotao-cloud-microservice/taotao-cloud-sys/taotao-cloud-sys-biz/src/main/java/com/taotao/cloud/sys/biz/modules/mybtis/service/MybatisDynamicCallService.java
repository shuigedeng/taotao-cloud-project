package com.taotao.cloud.sys.biz.modules.mybtis.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.sys.biz.modules.classloader.ClassloaderService;
import com.taotao.cloud.sys.biz.modules.core.exception.ToolException;
import com.taotao.cloud.sys.biz.modules.mybtis.dto.BoundSqlParam;
import com.taotao.cloud.sys.biz.modules.mybtis.dto.StatementIdInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeAliasRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Service;


import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MybatisDynamicCallService {
    @Autowired
    private ClassloaderService classloaderService;
    @Autowired
    private MybatisXmlFileManager mybatisXmlFileManager;

    /**
     * projectName => Configuration
     */
    private Map<String, Configuration> projectConfigurationMap = new ConcurrentHashMap<>();

    private ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    /**
     * 获取某一个 xml 文件中的 statementId 列表; 实时加载, 应该也不会太慢
     * @param xmlFile xml 文件
     * @param project 项目名
     * @param classloaderName 类加载器名称
     * @return
     */
    public List<StatementIdInfo> xmlFileStatementIds(String project, String fileName, String classloaderName) throws IOException {
        final ClassLoader classloader = classloaderService.getClassloader(classloaderName);
        final Configuration configuration = getConfiguration(project, fileName, classloader);

        final Collection<MappedStatement> mappedStatements = configuration.getMappedStatements();

        List<StatementIdInfo> statementInfos = new ArrayList<>();
        for (MappedStatement mappedStatement : mappedStatements) {
            final String id = mappedStatement.getId();
            final StatementIdInfo statementInfo = new StatementIdInfo(id);
            statementInfos.add(statementInfo);

            final List<StatementIdInfo.ParameterInfo> statementParameters = findStatementParameters(mappedStatement, classloader);
            statementInfo.setParameterInfos(statementParameters);
        }

        return statementInfos;
    }

    /**
     * 使用参数获取绑定的 sql 信息
     * @param project
     * @param fileName
     * @param classloaderName
     * @return
     */
    public String boundSql(BoundSqlParam boundSqlParam) throws IOException, ClassNotFoundException {
        final ClassLoader classloader = classloaderService.getClassloader(boundSqlParam.getClassloaderName());

        final Configuration configuration = getConfiguration(boundSqlParam.getProject(), boundSqlParam.getFileName(), classloader);
        final TypeAliasRegistry typeAliasRegistry = configuration.getTypeAliasRegistry();
        final MappedStatement mappedStatement = configuration.getMappedStatement(boundSqlParam.getStatementId());
        final List<BoundSqlParam.ValueObject> params = boundSqlParam.getParams();
        Map<String,Object> paramValues = new HashMap<>();
        if (CollectionUtils.isNotEmpty(params)) {
            for (BoundSqlParam.ValueObject param : params) {
                final StatementIdInfo.ParameterInfo parameterInfo = param.getParameterInfo();
                final String type = parameterInfo.getType();
                Class<?> clazz = null;
                try {
                    clazz = classloader.loadClass(type);
                }catch (ClassNotFoundException e){
                    // class 有可能是取的别名,试试别名能找到不
                    clazz = typeAliasRegistry.resolveAlias(type);
                }

                if (clazz != null) {
                    Object object = JSON.parseObject(param.getValue(), clazz);

                    if(object != null) {
                        // 如果是 List 类型, 进行内部数据转换
                        if (List.class.isAssignableFrom(clazz) && CollectionUtils.isNotEmpty(parameterInfo.getParameterTypes())) {
                            final List<String> parameterTypes = parameterInfo.getParameterTypes();
                            final Class<?> parameterTypeClazz = classloader.loadClass(parameterTypes.get(0));
                            List<JSONObject> list = (List<JSONObject>) object;
                            List actualList = new ArrayList(list.size());
                            for (JSONObject jsonObject : list) {
                                final Object innerObject = JSON.parseObject(jsonObject.toJSONString(), parameterTypeClazz);
                                actualList.add(innerObject);
                            }
                            object = actualList;
                        }

                        // 如果是 Map 类型,进行 Map 数据转换 , 只支持 key 为 String 类型
                        if (Map.class.isAssignableFrom(clazz) && CollectionUtils.isNotEmpty(parameterInfo.getParameterTypes())) {
                            final List<String> parameterTypes = parameterInfo.getParameterTypes();
                            final Class<?> parameterTypeKeyClazz = classloader.loadClass(parameterTypes.get(0));
                            final Class<?> parameterTypeValueClazz = classloader.loadClass(parameterTypes.get(1));

                            if (parameterTypeKeyClazz == String.class) {
                                Map<String, JSONObject> map = (Map<String, JSONObject>) object;
                                Map actualMap = new HashMap<>(map.size());
                                for (Map.Entry<String, JSONObject> entry : map.entrySet()) {
                                    final JSONObject value = entry.getValue();
                                    final Object innerObject = JSON.parseObject(value.toJSONString(), parameterTypeValueClazz);
                                    actualMap.put(entry.getKey(), entry.getValue());
                                }
                                object = actualMap;
                            } else {
                                log.warn("key 不为 String 类型的 map 不支持转换");
                            }
                        }
                    }
                    paramValues.put(parameterInfo.getName(), object);
                }else {
                    log.error("查找类型: {} 时失败,未找到该类型",type);
                    throw new ToolException("未找到对应类型: "+type);
                }
            }
        }
        final BoundSql boundSql = mappedStatement.getBoundSql(paramValues);
        return boundSql.getSql();
    }

    /**
     * 使用 mybatis 解析当前文件
     * @param project
     * @param fileName
     * @param classloader
     * @return
     * @throws IOException
     */
    private Configuration getConfiguration(String project, String fileName, ClassLoader classloader) throws IOException {
        Resources.setDefaultClassLoader(classloader);

        final Configuration configuration = new Configuration();
        final File file = mybatisXmlFileManager.xmlFile(project, fileName);
        try (final FileInputStream fileInputStream = new FileInputStream(file)){
            XMLMapperBuilder mapperParser = new XMLMapperBuilder(fileInputStream, configuration, file.getName(),configuration.getSqlFragments());
            mapperParser.parse();
        }

        Resources.setDefaultClassLoader(ClassLoader.getSystemClassLoader());
        return configuration;
    }

    /**
     * 查询语句中的参数信息
     * @param mappedStatement
     * @param classloader
     * @param id
     * @param statementInfo
     */
    private List<StatementIdInfo.ParameterInfo> findStatementParameters(MappedStatement mappedStatement, ClassLoader classloader) {
        List<StatementIdInfo.ParameterInfo> parameterInfos = new ArrayList<>();

        final String id = mappedStatement.getId();
        final String namespace = id.substring(0, id.lastIndexOf('.'));
        final String name = id.substring(id.lastIndexOf('.')+1);
        // 默认情况下,将接口名称当成是命名空间; sqlId 就是方法名
        final String className = namespace;
        final String methodName = name;

        // 去类加器中查找是否有这个类,如果有,则去找方法的参数列表
        try {
            final Class<?> clazz = classloader.loadClass(className);
//            final Method[] allDeclaredMethods = ReflectionUtils.getAllDeclaredMethods(clazz);
            final Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                final String currentMethodName = method.getName();
                if (currentMethodName.equals(methodName)){
                    // 大多数情况下, mybatis 中不会写同名方法, 这里只考虑 80% 的情况, 其它情况不管
                    final Class<?>[] parameterTypes = method.getParameterTypes();
                    final Parameter[] parameters = method.getParameters();
                    final Type[] genericParameterTypes = method.getGenericParameterTypes();
                    final String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
                    for (int i = 0; i < parameterTypes.length; i++) {
                        final Class<?> parameterType = parameterTypes[i];
                        String paramName = parameterNames != null ? parameterNames[i] : parameters[i].getName();
                        // 如果有加 mybatis 的 @param ,则名字取注解中的名称
                        final Param paramAnnotation = parameterType.getAnnotation(Param.class);
                        if (paramAnnotation != null){
                            paramName = paramAnnotation.value();
                        }

                        final StatementIdInfo.ParameterInfo parameterInfo = new StatementIdInfo.ParameterInfo(paramName, parameterType,genericParameterTypes[i]);
                        parameterInfos.add(parameterInfo);
                    }
                    break;
                }
            }
        } catch (ClassNotFoundException e) {
            // 如果没有这个类, 则取语句中填写的参数信息
            final ParameterMap parameterMap = mappedStatement.getParameterMap();
            final List<ParameterMapping> parameterMappings = parameterMap.getParameterMappings();
            for (ParameterMapping parameterMapping : parameterMappings) {
                final String property = parameterMapping.getProperty();
                final Class<?> javaType = parameterMapping.getJavaType();
                final StatementIdInfo.ParameterInfo parameterInfo = new StatementIdInfo.ParameterInfo(property, javaType);
                parameterInfos.add(parameterInfo);
            }
        }

        return parameterInfos;
    }
}
