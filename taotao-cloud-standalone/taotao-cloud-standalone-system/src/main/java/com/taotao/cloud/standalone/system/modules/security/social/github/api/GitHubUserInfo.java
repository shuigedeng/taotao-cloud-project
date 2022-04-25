package com.taotao.cloud.standalone.system.modules.security.social.github.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Classname GiteeAdapter
 * @Description github 用户信息
 * @Author shuigedeng
 * @since 2019-07-08 21:49
 * 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GitHubUserInfo {
    private long id;
    private String name;
    private String username;
    private String location;
    private String company;
    private String blog;
    private String email;
    private Date createdDate;
    private String profileImageUrl;
    private String avatarUrl;
}
