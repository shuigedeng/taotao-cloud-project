package com.taotao.cloud.stock.api.common.util.mybatis;

import com.xtoon.boot.common.util.UserContext;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

/**
 * mybatis拦截器，自动注入创建人、创建时间、修改人、修改时间
 *
 * @author shuigedeng
 * @date 2021-01-25
 **/
@Slf4j
@Component
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class MybatisInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
//        String sqlId = mappedStatement.getId();
//        log.debug("------sqlId------" + sqlId);
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        Object parameter = invocation.getArgs()[1];
//        log.debug("------sqlCommandType------" + sqlCommandType);

        if (parameter == null) {
            return invocation.proceed();
        }
        if (SqlCommandType.INSERT == sqlCommandType) {
            String userId = this.getLoginUser();
            Field[] fields = getAllFields(parameter);
            for (Field field : fields) {
//                log.debug("------field.name------" + field.getName());
                try {
                    if ("createdBy".equals(field.getName())) {
                        field.setAccessible(true);
                        Object local_createBy = field.get(parameter);
                        field.setAccessible(false);
                        if (local_createBy == null || local_createBy.equals("")) {
                            if (userId != null) {
                                // 登录人账号
                                field.setAccessible(true);
                                field.set(parameter, userId);
                                field.setAccessible(false);
                            }
                        }
                    }
                    // 注入创建时间
                    if ("createdTime".equals(field.getName())) {
                        field.setAccessible(true);
                        Object local_createDate = field.get(parameter);
                        field.setAccessible(false);
                        if (local_createDate == null || local_createDate.equals("")) {
                            field.setAccessible(true);
                            field.set(parameter, new Date());
                            field.setAccessible(false);
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        if (SqlCommandType.UPDATE == sqlCommandType) {
            String userId = this.getLoginUser();
            Field[] fields = null;
            if (parameter instanceof MapperMethod.ParamMap) {
                MapperMethod.ParamMap<?> p = (MapperMethod.ParamMap<?>) parameter;
                //update-begin-author:scott date:20190729 for:批量更新报错issues/IZA3Q--
                if (p.containsKey("et")) {
                    parameter = p.get("et");
                } else {
                    parameter = p.get("param1");
                }
                //update-end-author:scott date:20190729 for:批量更新报错issues/IZA3Q-

                //update-begin-author:scott date:20190729 for:更新指定字段时报错 issues/#516-
                if (parameter == null) {
                    return invocation.proceed();
                }
                //update-end-author:scott date:20190729 for:更新指定字段时报错 issues/#516-

                fields = getAllFields(parameter);
            } else {
                fields = getAllFields(parameter);
            }

            for (Field field : fields) {
//                log.debug("------field.name------" + field.getName());
                try {
                    if ("updatedBy".equals(field.getName())) {
                        //获取登录用户信息
                        if (userId != null) {
                            // 登录账号
                            field.setAccessible(true);
                            field.set(parameter, userId);
                            field.setAccessible(false);
                        }
                    }
                    if ("updatedTime".equals(field.getName())) {
                        field.setAccessible(true);
                        field.set(parameter, new Date());
                        field.setAccessible(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // TODO Auto-generated method stub
    }

    private String getLoginUser() {
        String userId = null;
        try {
            userId = UserContext.getUserId();
        } catch (Exception e) {
            //e.printStackTrace();
            userId = null;
        }
        return userId;
    }

    /**
     * 获取类的所有属性，包括父类
     *
     * @param object
     * @return
     */
    private Field[] getAllFields(Object object) {
        Class<?> clazz = object.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }

}

