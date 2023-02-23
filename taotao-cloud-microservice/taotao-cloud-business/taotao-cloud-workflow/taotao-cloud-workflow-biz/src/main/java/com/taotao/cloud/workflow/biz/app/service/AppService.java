package com.taotao.cloud.workflow.biz.app.service;

import jnpf.model.AppUserInfoVO;
import jnpf.model.AppUsersVO;

/**
 * app用户信息
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021-08-08
 */
public interface AppService {

    /**
     * app用户信息
     * @return
     */
    AppUsersVO userInfo();

    /**
     * 通讯录
     * @return
     */
    AppUserInfoVO getInfo(String id);

}
