package com.taotao.cloud.standalone.system.modules.security.social;

import lombok.Data;

/**
 * @Classname SocialUserInfo
 * @Description
 * @Author Created by Lihaodong (alias:小东啊) lihaodongmail@163.com
 * @Date 2019-07-08 21:49
 * @Version 1.0
 */
@Data
public class SocialUserInfo {

    private String providerId;
    private String providerUserId;
    private String nickname;
    private String headImg;
}
