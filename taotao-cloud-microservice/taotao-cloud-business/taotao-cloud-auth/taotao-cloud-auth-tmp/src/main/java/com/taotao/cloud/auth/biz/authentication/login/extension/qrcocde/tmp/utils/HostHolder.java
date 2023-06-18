package com.taotao.cloud.auth.biz.authentication.login.extension.qrcocde.tmp.utils;

import com.taotao.cloud.auth.biz.authentication.login.extension.qrcocde.tmp.entity.User;
import org.springframework.stereotype.Component;

@Component
public class HostHolder {

    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}
