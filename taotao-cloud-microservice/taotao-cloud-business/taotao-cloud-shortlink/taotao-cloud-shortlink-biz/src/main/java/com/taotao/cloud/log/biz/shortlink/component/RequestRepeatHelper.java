package com.taotao.cloud.log.biz.shortlink.component;//package com.zc.shortlink.component;
//
//import com.alibaba.fastjson.JSONObject;
//import com.zc.shortlink.utils.CommonBizUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.Collection;
//import java.util.Optional;
//import java.util.concurrent.TimeUnit;
//
///**
// * This is Description
// *
// * 
// * @since 2022/05/06
// */
//@Slf4j
//@Component
//public class RequestRepeatHelper {
//
//    private static final String CACHE_PREFIX = "short.link.platform";
//
//    @Resource
//    private StringRedisTemplate redisTemplate;
//
//    public boolean checkRepeat(final long mills, String methodName, String bizId) {
//        Optional<String> bizMd5Opt = CommonBizUtil.MD5(bizId);
//        if (bizMd5Opt.isPresent()) {
//            String KEY = CACHE_PREFIX + "dedup:M=" + " + methodName + " + methodName + "Biz=" + bizMd5Opt.get();
//            if (Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(KEY, "", mills, TimeUnit.MILLISECONDS))) {
//                return true;
//            }
//
//            log.warn("重复请求： method -> {}, bizId -> {}", methodName, bizId);
//            return false;
//        }
//        return false;
//    }
//
//    public boolean checkRepeat(final long mills, Long userId, String methodName, Collection<Object> params) {
//
//        Optional<String> paramsJson = CommonBizUtil.MD5(JSONObject.toJSONString(params));
//        if (paramsJson.isPresent()) {
//            String KEY = CACHE_PREFIX + "dedup:U=" + userId + "M=" + methodName + "P=" + paramsJson.get();
//            if (Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(KEY, "", mills, TimeUnit.MILLISECONDS))) {
//                return true;
//            }
//
//            log.warn("重复请求： userId -> {},method -> {}", userId, methodName);
//            return false;
//        }
//        return false;
//    }
//
//}
