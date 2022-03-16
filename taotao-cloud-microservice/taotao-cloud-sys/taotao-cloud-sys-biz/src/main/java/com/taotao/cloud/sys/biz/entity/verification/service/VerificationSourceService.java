package com.taotao.cloud.sys.biz.entity.verification.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.common.enums.CachePrefix;
import com.taotao.cloud.sys.biz.entity.verification.entity.dos.VerificationSource;
import com.taotao.cloud.sys.biz.entity.verification.entity.dto.VerificationDTO;

/**
 * 验证码资源维护 业务层
 */
public interface VerificationSourceService extends IService<VerificationSource> {

    /**
     * 缓存
     */
    String VERIFICATION_CACHE = CachePrefix.VERIFICATION.getPrefix();


    /**
     * 初始化缓存
     *
     * @return
     */
    VerificationDTO initCache();

    /**
     * 获取验证缓存
     *
     * @return 验证码
     */
    VerificationDTO getVerificationCache();
}
