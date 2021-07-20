package com.taotao.cloud.standalone.system.modules.security.handle;

import com.taotao.cloud.standalone.common.exception.PreBaseException;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationFailureCredentialsExpiredEvent;
import org.springframework.security.authentication.event.AuthenticationFailureDisabledEvent;
import org.springframework.security.authentication.event.AuthenticationFailureExpiredEvent;
import org.springframework.security.authentication.event.AuthenticationFailureLockedEvent;
import org.springframework.security.authentication.event.AuthenticationFailureProviderNotFoundEvent;
import org.springframework.security.authentication.event.AuthenticationFailureProxyUntrustedEvent;
import org.springframework.security.authentication.event.AuthenticationFailureServiceExceptionEvent;
import org.springframework.stereotype.Component;

/**
 * @Classname XytAuthencationFailureListener
 * @Description 用户登录失败监听器事件
 * @Author shuigedeng
 * @since 2019/12/19 5:24 下午
 * @Version 1.0
 */
@Component
public class PreAuthencationFailureListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {

    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        if (event instanceof AuthenticationFailureBadCredentialsEvent) {
            //提供的凭据是错误的，用户名或者密码错误
            throw new PreBaseException("用户名或者密码错误", 402);
        } else if (event instanceof AuthenticationFailureCredentialsExpiredEvent) {
            //验证通过，但是密码过期
            throw new PreBaseException("密码过期", 402);
        } else if (event instanceof AuthenticationFailureDisabledEvent) {
            //验证过了但是账户被禁用
            throw new PreBaseException("账户被禁用", 402);
        } else if (event instanceof AuthenticationFailureExpiredEvent) {
            //验证通过了，但是账号已经过期
            throw new PreBaseException("账号已经过期", 402);
        } else if (event instanceof AuthenticationFailureLockedEvent) {
            //账户被锁定
            throw new PreBaseException("账户被锁定", 402);
        } else if (event instanceof AuthenticationFailureProviderNotFoundEvent) {
            //配置错误，没有合适的AuthenticationProvider来处理登录验证
            throw new PreBaseException("配置错误", 402);
        } else if (event instanceof AuthenticationFailureProxyUntrustedEvent) {
            //代理不受信任，用于Oauth、CAS这类三方验证的情形，多属于配置错误
            throw new PreBaseException("代理不受信任", 402);
        } else if (event instanceof AuthenticationFailureServiceExceptionEvent) {
            //其他任何在AuthenticationManager中内部发生的异常都会被封装成此类
            throw new PreBaseException("内部发生错误", 402);
        }
    }

}
