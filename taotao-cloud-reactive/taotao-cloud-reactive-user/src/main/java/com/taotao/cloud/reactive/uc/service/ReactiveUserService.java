/**
 * Project Name: projects
 * Package Name: com.taotao.cloud.reactive.uc.service
 * Descroption:
 * Date: 2020/9/10 14:14
 * Author: shuigedeng
 */
package com.taotao.cloud.reactive.uc.service;

import com.taotao.cloud.reactive.uc.entity.User;
import reactor.core.publisher.Mono;

/**
 * 〈〉<br>
 *
 * @author shuigedeng
 * @since v1.0.0
 * 
 * @see
 * @create 2020/9/10 14:14
 */
public interface ReactiveUserService {
    Mono<User> findById(String userId);

    Mono<User> saveUser(User user);
}
