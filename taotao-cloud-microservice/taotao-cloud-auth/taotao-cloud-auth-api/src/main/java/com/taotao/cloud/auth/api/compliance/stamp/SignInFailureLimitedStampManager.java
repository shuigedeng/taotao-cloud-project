//
// package com.taotao.cloud.auth.api.compliance.stamp;
//
// import cn.herodotus.engine.cache.jetcache.stamp.AbstractCountStampManager;
// import cn.herodotus.engine.oauth2.compliance.dto.SignInErrorStatus;
// import cn.herodotus.engine.oauth2.core.constants.OAuth2Constants;
// import cn.herodotus.engine.oauth2.core.properties.OAuth2ComplianceProperties;
// import cn.hutool.crypto.SecureUtil;
// import com.alicp.jetcache.Cache;
// import com.alicp.jetcache.anno.CacheType;
// import com.alicp.jetcache.anno.CreateCache;
// import org.apache.commons.lang3.ObjectUtils;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;
//
// /**
//  * <p>Description: 登录失败次数限制签章管理 </p>
//  *
//  * @author : gengwei.zheng
//  * @date : 2022/7/6 23:36
//  */
// @Component
// public class SignInFailureLimitedStampManager extends AbstractCountStampManager {
//
//     private final OAuth2ComplianceProperties complianceProperties;
//
//     @CreateCache(name = OAuth2Constants.CACHE_NAME_TOKEN_SIGN_IN_FAILURE_LIMITED, cacheType = CacheType.BOTH)
//     protected Cache<String, Long> cache;
//
//     @Autowired
//     public SignInFailureLimitedStampManager(OAuth2ComplianceProperties complianceProperties) {
//         this.complianceProperties = complianceProperties;
//     }
//
//     @Override
//     protected Cache<String, Long> getCache() {
//         return cache;
//     }
//
//     @Override
//     public Long nextStamp(String key) {
//         return 1L;
//     }
//
//     @Override
//     public void afterPropertiesSet() throws Exception {
//         super.setExpire(complianceProperties.getSignInFailureLimited().getExpire());
//     }
//
//     public OAuth2ComplianceProperties getComplianceProperties() {
//         return complianceProperties;
//     }
//
//     public SignInErrorStatus errorStatus(String username) {
//         int maxTimes = complianceProperties.getSignInFailureLimited().getMaxTimes();
//         Long storedTimes = get(SecureUtil.md5(username));
//
//         int errorTimes = 0;
//         if (ObjectUtils.isNotEmpty(storedTimes)) {
//             errorTimes = storedTimes.intValue();
//         }
//
//         int remainTimes = maxTimes;
//         if (errorTimes != 0) {
//             remainTimes =  maxTimes - errorTimes;
//         }
//
//         boolean isLocked = false;
//         if (errorTimes == maxTimes) {
//             isLocked = true;
//         }
//
//         SignInErrorStatus status = new SignInErrorStatus();
//         status.setErrorTimes(errorTimes);
//         status.setRemainTimes(remainTimes);
//         status.setLocked(isLocked);
//
//         return status;
//     }
// }
