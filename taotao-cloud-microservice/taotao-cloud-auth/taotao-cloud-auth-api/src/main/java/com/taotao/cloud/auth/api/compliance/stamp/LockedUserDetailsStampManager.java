//
// package com.taotao.cloud.auth.api.compliance.stamp;
//
// import cn.herodotus.engine.cache.jetcache.stamp.AbstractStampManager;
// import cn.herodotus.engine.oauth2.core.constants.OAuth2Constants;
// import cn.herodotus.engine.oauth2.core.properties.OAuth2ComplianceProperties;
// import cn.hutool.core.util.IdUtil;
// import com.alicp.jetcache.Cache;
// import com.alicp.jetcache.anno.CacheType;
// import com.alicp.jetcache.anno.CreateCache;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;
//
// /**
//  * <p>Description: 锁定账户签章管理 </p>
//  *
//  * @author : gengwei.zheng
//  * @date : 2022/7/8 21:27
//  */
// @Component
// public class LockedUserDetailsStampManager extends AbstractStampManager<String, String> {
//
//     private final OAuth2ComplianceProperties complianceProperties;
//
//     @CreateCache(name = OAuth2Constants.CACHE_NAME_TOKEN_LOCKED_USER_DETAIL, cacheType = CacheType.BOTH)
//     protected Cache<String, String> cache;
//
//     @Autowired
//     public LockedUserDetailsStampManager(OAuth2ComplianceProperties complianceProperties) {
//         this.complianceProperties = complianceProperties;
//     }
//
//     @Override
//     protected Cache<String, String> getCache() {
//         return this.cache;
//     }
//
//     @Override
//     public String nextStamp(String key) {
//         return IdUtil.fastSimpleUUID();
//     }
//
//     @Override
//     public void afterPropertiesSet() throws Exception {
//         super.setExpire(complianceProperties.getSignInFailureLimited().getExpire());
//     }
// }
