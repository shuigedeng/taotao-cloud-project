package com.taotao.cloud.stock.biz.domain.user.repository;

import com.taotao.cloud.stock.biz.domain.model.user.Mobile;
import com.taotao.cloud.stock.biz.domain.model.user.User;
import com.taotao.cloud.stock.biz.domain.model.user.UserId;
import com.taotao.cloud.stock.biz.domain.user.model.vo.Mobile;
import com.taotao.cloud.stock.biz.domain.user.model.entity.User;
import com.taotao.cloud.stock.biz.domain.user.model.vo.UserId;
import java.util.List;

/**
 * 用户-Repository接口
 *
 * @author haoxin
 * @date 2021-02-02
 **/
public interface UserRepository {

    /**
     * 通过用户ID获取用户
     *
     * @param userId
     * @return
     */
    com.taotao.cloud.stock.biz.domain.model.user.User find(
		    com.taotao.cloud.stock.biz.domain.model.user.UserId userId);

    /**
     * 根据手机号获取账号
     *
     * @param mobile
     * @return
     */
    List<com.taotao.cloud.stock.biz.domain.model.user.User> find(Mobile mobile);

    /**
     * 保存
     *
     * @param user
     */
    com.taotao.cloud.stock.biz.domain.model.user.UserId store(User user);

    /**
     * 删除
     *
     * @param userIds
     */
    void remove(List<UserId> userIds);
}
