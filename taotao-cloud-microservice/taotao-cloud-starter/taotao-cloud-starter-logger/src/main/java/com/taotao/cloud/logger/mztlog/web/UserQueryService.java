package com.taotao.cloud.logger.mztlog.web;


import org.springframework.security.core.userdetails.User;

import java.util.List;

public interface UserQueryService {
    List<User> getUserList(List<String> userIds);
}
