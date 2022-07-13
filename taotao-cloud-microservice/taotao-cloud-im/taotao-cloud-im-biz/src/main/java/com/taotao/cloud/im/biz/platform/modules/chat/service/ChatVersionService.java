package com.taotao.cloud.im.biz.platform.modules.chat.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatVersion;
import com.platform.modules.chat.vo.VersionVo;

/**
 * <p>
 * 版本 服务层
 * q3z3
 * </p>
 */
public interface ChatVersionService extends BaseService<ChatVersion> {

    /**
     * 用户协议
     */
    String getAgreement();

    /**
     * 获取版本
     */
    VersionVo getVersion(String version, String device);

}
