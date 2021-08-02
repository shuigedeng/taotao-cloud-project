package com.taotao.cloud.portal.util;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *  
 * </p>
 *
 * @author xiaoming
 * @since 2020-03-10
 */
public class User implements Serializable {

    private static final long serialVersionUID=1L;

    private Long id;

    private String username;

    private String password;

    private String phone;

    private String email;

    private String token;

    private String openid;

    private Date createTime;

    private Date updateTime;

    private Boolean deleted;

    private Integer version;
}
