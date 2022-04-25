/**
 * Project Name: projects
 * Package Name: com.taotao.cloud.reactive.uc.repository
 * Descroption:
 * Date: 2020/9/10 14:07
 * Author: shuigedeng
 */
package com.taotao.cloud.reactive.uc.repository;

import com.taotao.cloud.reactive.uc.entity.User;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

/**
 * 〈〉<br>
 *
 * @author shuigedeng
 * 
 * @create 2020/9/10 14:07
 * @see
 * @since v1.0.0
 */
public interface ReactiveUserSortingRepository extends ReactiveSortingRepository<User, String> {
}
