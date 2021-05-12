package com.taotao.cloud.oauth2.api.tmp;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuthUserOauth2 {
    private Integer id;
    private String clientRegistrationId;
    private String principalName;
    private String nickname;
    private String avatar;
    private String userId;
    private LocalDateTime createdAt;
}
