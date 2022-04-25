package com.taotao.cloud.standalone.system.modules.security.handle;

import com.taotao.cloud.standalone.security.PreSecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

/**
 * @Classname PreAuthencationSuccessListener
 * @Description 用户登录成功监听器事件
 * @Author shuigedeng
 * @since 2019/12/19 5:28 下午
 * 
 */
@Slf4j
@Component
public class PreAuthencationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        PreSecurityUser xytSecurityUser = (PreSecurityUser) event.getAuthentication().getPrincipal();;
        log.info("用户名:{},成功登录", xytSecurityUser.getUsername());
    }

}
