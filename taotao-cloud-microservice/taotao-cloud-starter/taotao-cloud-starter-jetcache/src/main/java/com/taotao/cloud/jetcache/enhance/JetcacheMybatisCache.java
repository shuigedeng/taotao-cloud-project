// /*
//  * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
//  *
//  * Licensed under the Apache License, Version 2.0 (the "License");
//  * you may not use this file except in compliance with the License.
//  * You may obtain a copy of the License at
//  *
//  *      https://www.apache.org/licenses/LICENSE-2.0
//  *
//  * Unless required by applicable law or agreed to in writing, software
//  * distributed under the License is distributed on an "AS IS" BASIS,
//  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  * See the License for the specific language governing permissions and
//  * limitations under the License.
//  */
//
//
// package com.taotao.cloud.jetcache.enhance;
//
// import cn.hutool.extra.spring.SpringUtil;
// import org.apache.ibatis.cache.Cache;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
//
// import java.util.concurrent.atomic.AtomicInteger;
//
// /**
//  * <p>Description: 扩展的Mybatis二级缓存 </p>
//  *
//  * @author shuigedeng
//  * @version 2022.07
//  * @since 2022-07-12 09:13:56
//  */
// public class JetcacheMybatisCache implements Cache {
//
//     private static final Logger log = LoggerFactory.getLogger(JetcacheMybatisCache.class);
//
//     private final String id;
//     private final com.alicp.jetcache.Cache<Object, Object> cache;
//     private final AtomicInteger counter = new AtomicInteger(0);
//
//     public JetcacheMybatisCache(String id) {
//         this.id = id;
//         JetCacheBuilder jetCacheBuilder = SpringUtil.getBean("jetcacheBuilder");
//         this.cache = jetCacheBuilder.create(this.id);
//     }
//
//     @Override
//     public String getId() {
//         return this.id;
//     }
//
//     @Override
//     public void putObject(Object key, Object value) {
//         cache.put(key, value);
//         counter.incrementAndGet();
//         log.debug("CACHE - Put data into Mybatis Cache, with key: [{}]", key);
//     }
//
//     @Override
//     public Object getObject(Object key) {
//         Object obj = cache.get(key);
//         log.debug("CACHE - Get data from Mybatis Cache, with key: [{}]", key);
//         return obj;
//     }
//
//     @Override
//     public Object removeObject(Object key) {
//         Object obj = cache.remove(key);
//         counter.decrementAndGet();
//         log.debug("CACHE - Remove data from Mybatis Cache, with key: [{}]", key);
//         return obj;
//     }
//
//     @Override
//     public void clear() {
//         cache.close();
//         log.debug("CACHE - Clear Mybatis Cache.");
//     }
//
//     @Override
//     public int getSize() {
//         return counter.get();
//     }
// }
