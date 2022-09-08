package com.taotao.cloud.data.mybatisplus.datascope.perm.handler;

import com.taotao.cloud.common.utils.aop.AopUtils;
import com.taotao.cloud.common.utils.common.SecurityUtils;
import com.taotao.cloud.data.mybatisplus.datascope.perm.NestedPermission;
import com.taotao.cloud.data.mybatisplus.datascope.perm.Permission;
import com.taotao.cloud.data.mybatisplus.datascope.perm.local.DataPermContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
* 忽略权限控制切面处理类
*/
@Aspect
@Component
public class DataPermAspectHandler {

    /**
     * 数据权限注解切面
     */
    @Around("@annotation(permission)||@within(permission)")
    public Object doAround(ProceedingJoinPoint pjp, Permission permission) throws Throwable {
        Object obj = null;
        // 如果方法和类同时存在, 以方法上的注解为准
        Permission methodAnnotation = AopUtils.getAnnotation(pjp, Permission.class);
        if (Objects.nonNull(methodAnnotation)){
            DataPermContextHolder.putPermission(methodAnnotation);
        } else {
            DataPermContextHolder.putPermission(permission);
        }
        DataPermContextHolder.putUserDetail(SecurityUtils.getCurrentUserWithNull());
        try {
            obj = pjp.proceed();
        } finally {
            DataPermContextHolder.clearUserAndPermission();
        }
        return obj;
    }

    @Around("@annotation(nestedPermission)||@within(nestedPermission)")
    public Object doAround(ProceedingJoinPoint pjp, NestedPermission nestedPermission) throws Throwable {
        Object obj = null;
        // 如果方法和类同时存在, 以方法上的注解为准
        NestedPermission methodAnnotation = AopUtils.getAnnotation(pjp, NestedPermission.class);
        if (Objects.nonNull(methodAnnotation)){
            DataPermContextHolder.putNestedPermission(methodAnnotation);
        } else {
            DataPermContextHolder.putNestedPermission(nestedPermission);
        }
        DataPermContextHolder.putUserDetail(SecurityUtils.getCurrentUserWithNull());
        try {
            obj = pjp.proceed();
        } finally {
            DataPermContextHolder.clearNestedPermission();
        }
        return obj;
    }
}
