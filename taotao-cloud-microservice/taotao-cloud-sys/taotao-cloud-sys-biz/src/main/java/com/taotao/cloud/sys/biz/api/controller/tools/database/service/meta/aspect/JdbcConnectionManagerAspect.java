package com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.aspect;

import com.alibaba.druid.pool.DruidDataSource;
import com.taotao.cloud.sys.biz.api.controller.tools.core.exception.ToolException;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.connect.ConnDatasourceAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.NamedThreadLocal;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class JdbcConnectionManagerAspect {

//    @Pointcut("execution(public * com.sanri.tools.modules.database.service.meta.*DatabaseMetaDataLoad.*(..))")
//    public void pointcut(){}

    @Autowired
    private ConnDatasourceAdapter connDatasourceAdapter;

    @Pointcut("@annotation(com.sanri.tools.modules.database.service.meta.aspect.JdbcConnection)")
    public void pointcut(){}

    public static final ThreadLocal<Map<DruidDataSource,ConnectionHolder>> connectionThreadLocal = new NamedThreadLocal<>("Connection Holder");

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        final Method method = signature.getMethod();
        final Class<?> targetClass = proceedingJoinPoint.getTarget().getClass();
        final String methodIdentification = methodIdentification(method, targetClass);

        if (proceedingJoinPoint.getArgs()[0] == null){
            throw new ToolException("如需要获取数据库连接, 第一个参数必须是连接名connName或者数据源DruidDatasource或者实体类里面有 connName 字段");
        }

        DruidDataSource druidDataSource = getDruidDataSource(proceedingJoinPoint.getArgs()[0]);

        try {
            createJdbcConnectionIfNecessary(methodIdentification,druidDataSource);

            // 执行方法调用
            return proceedingJoinPoint.proceed();

        } catch (Throwable throwable) {
            // 出现异常, 直接关闭连接
            final Map<DruidDataSource, ConnectionHolder> druidDataSourceConnectionHolderMap = connectionThreadLocal.get();
            for (ConnectionHolder connectionHolder : druidDataSourceConnectionHolderMap.values()) {
                if (connectionHolder != null) {
                    final Connection connection = connectionHolder.getConnection();
                    if (connection != null && !connection.isClosed()) {
                        connection.close();
                    }
                }else{
                    log.error("connectionHolder 为空");
                }
            }
            throw throwable;
        }finally {
            Map<DruidDataSource, ConnectionHolder> druidDataSourceConnectionHolderMap = connectionThreadLocal.get();
            if (druidDataSourceConnectionHolderMap == null){
                druidDataSourceConnectionHolderMap = new HashMap<>();
                connectionThreadLocal.set(druidDataSourceConnectionHolderMap);
            }
            ConnectionHolder connectionHolder = druidDataSourceConnectionHolderMap.get(druidDataSource);

            // 是否要关闭连接
            if (connectionHolder != null && connectionHolder.getConnectionStatus().isNewConnection()){
                log.info("回收连接: {}",methodIdentification);
                try{
                    if (connectionHolder.getConnection() != null) {
                        connectionHolder.getConnection().close();
                    }
                    druidDataSourceConnectionHolderMap.put(druidDataSource,null);
                } catch (SQLException throwables) {
                    log.error("关闭连接时异常:{}, {}",throwables.getErrorCode(),throwables.getMessage(),throwables);
                }
            }
            // 恢复之前的连接状态信息
            connectionHolder.setConnectionStatus(connectionHolder.getConnectionStatus().getOldConnectionStatus());
        }
    }

    /**
     * 创建 jdbc 连接
     * @param methodIdentification
     * @throws SQLException
     */
    @Value("${sanri.webui.package.prefix:com.sanri.tools}")
    protected String packagePrefix;

    /**
     * 创建指定数据源的连接
     * @param methodIdentification
     * @param pointArg
     * @throws SQLException
     * @throws IOException
     */
    private void createJdbcConnectionIfNecessary(String methodIdentification,DruidDataSource druidDataSource) throws SQLException, IOException {
        // 先看当前线程是否有连接, 没有则获取一个
        Map<DruidDataSource, ConnectionHolder> druidDataSourceConnectionHolderMap = connectionThreadLocal.get();
        if (druidDataSourceConnectionHolderMap == null){
            druidDataSourceConnectionHolderMap = new HashMap<>();
            connectionThreadLocal.set(druidDataSourceConnectionHolderMap);
        }

        ConnectionHolder connectionHolder = druidDataSourceConnectionHolderMap.get(druidDataSource);
        if (connectionHolder != null && !connectionHolder.getConnection().isClosed()){
            final ConnectionHolder.ConnectionStatus connectionStatus = new ConnectionHolder.ConnectionStatus(methodIdentification, false);
            connectionStatus.setOldConnectionStatus(connectionHolder.getConnectionStatus());
            connectionHolder.setConnectionStatus(connectionStatus);
            return ;
        }

        // 否则新建连接
        log.info("新建连接:{}", methodIdentification);
        connectionHolder = new ConnectionHolder();
        connectionHolder.setConnection(druidDataSource.getConnection());
        connectionHolder.setConnectionStatus(new ConnectionHolder.ConnectionStatus(methodIdentification,true));
        druidDataSourceConnectionHolderMap.put(druidDataSource,connectionHolder);
    }

    private DruidDataSource getDruidDataSource(Object pointArg) throws IOException, SQLException {
        DruidDataSource druidDataSource = null;
        // 支持第一个字段名称是连接名
        if (pointArg instanceof String){
            String connName = (String) pointArg;
            druidDataSource = connDatasourceAdapter.poolDataSource(connName);
        }

        // 支持第一个参数是 DruidDatasource
        if (pointArg instanceof DruidDataSource) {
            druidDataSource = (DruidDataSource) pointArg;
        }

        // 支持参数对象字段中有 connName
        final Field connNameField = FieldUtils.getDeclaredField(pointArg.getClass(), "connName", true);
        if (connNameField != null){
            String connName = (String) ReflectionUtils.getField(connNameField, pointArg);
            druidDataSource = connDatasourceAdapter.poolDataSource(connName);
        }else {
            // 支持参数方法中有 getConnName
            final Method getConnName = MethodUtils.getAccessibleMethod(pointArg.getClass(), "getConnName");
            if (getConnName != null){
                final String connName = (String) ReflectionUtils.invokeMethod(getConnName, pointArg);
                druidDataSource = connDatasourceAdapter.poolDataSource(connName);
            }
        }

        if (druidDataSource == null){
            final StackTraceElement[] stackTraceElements = Thread.getAllStackTraces().get(Thread.currentThread());
            StringBuffer showMessage = new StringBuffer();
            for (StackTraceElement stackTraceElement : stackTraceElements) {
                String className = stackTraceElement.getClassName();
                int lineNumber = stackTraceElement.getLineNumber();
                if (className.startsWith(packagePrefix)) {
                    showMessage.append(className + "(" + lineNumber + ")\n");
                }
            }

            throw new ToolException("不支持的连接获取, 第一个参数必须是数据源或者连接名:"+ pointArg + "\n 执行堆栈为 : "+showMessage );
        }
        return druidDataSource;
    }

    private String methodIdentification(Method method, @Nullable Class<?> targetClass) {
        return ClassUtils.getQualifiedMethodName(method,  targetClass);
    }

    /**
     * 获取线程绑定的连接
     * @return
     */
    public static Connection threadBoundConnection(DruidDataSource druidDataSource) throws IOException, SQLException {
        Map<DruidDataSource, ConnectionHolder> druidDataSourceConnectionHolderMap = connectionThreadLocal.get();
        if (druidDataSourceConnectionHolderMap == null){
            druidDataSourceConnectionHolderMap = new HashMap<>();
            connectionThreadLocal.set(druidDataSourceConnectionHolderMap);
        }
        final ConnectionHolder connectionHolder = druidDataSourceConnectionHolderMap.get(druidDataSource);
        if (connectionHolder != null){
            return connectionHolder.getConnection();
        }
        throw new ToolException("没有连接绑定到当前线程");
    }
}
