// package com.taotao.cloud.schedule.lhz.utils;
//
// import com.taotao.cloud.schedule.lhz.config.TaskRedisLuaConfig;
// import com.taotao.cloud.schedule.lhz.task.TaskManager;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.data.redis.core.StringRedisTemplate;
// import org.springframework.stereotype.Component;
//
// import javax.annotation.Resource;
// import java.util.concurrent.TimeUnit;
//
// /**
//  * @Author: LiHuaZhi
//  * @Date: 2020/9/25 23:48
//  * @Description:
//  **/
// @Component
// public class RedisUtils {
//     @Resource
//     private StringRedisTemplate stringRedisTemplate;
//
//     @Resource
//     private TaskRedisLuaConfig taskRedisLuaConfig;
//
//     private final Logger log = LoggerFactory.getLogger(TaskManager.class);
//
//     public boolean setLock(String lockKey, String value, long time, TimeUnit timeUnit) {
//         return stringRedisTemplate.opsForValue().setIfAbsent(lockKey, value, time, timeUnit);
//     }
//
//
//     public void deleteLock(String lockKey, String value) {
//
//         boolean script = taskRedisLuaConfig.runLuaScript(lockKey, value);
//         if (script) {
//             log.info("===>>>解锁成功");
//         } else {
//             log.info("===>>>解锁失败");
//         }
//     }
// }
