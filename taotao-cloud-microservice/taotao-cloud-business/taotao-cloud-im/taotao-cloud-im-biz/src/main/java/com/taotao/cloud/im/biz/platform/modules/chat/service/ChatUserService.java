package com.taotao.cloud.im.biz.platform.modules.chat.service;

import cn.hutool.core.lang.Dict;
import com.platform.common.web.service.BaseService;
import com.platform.modules.auth.vo.AuthVo01;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.vo.MyVo09;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * <p>
 * 用户表 服务层
 * q3z3
 * </p>
 */
public interface ChatUserService extends BaseService<ChatUser> {

    /**
     * 通过手机+密码注册
     */
    void register(AuthVo01 authVo);

    /**
     * 通过手机号查询
     */
    ChatUser queryByPhone(String phone);

    /**
     * 重置密码
     *
     * @return
     */
    void resetPass(Long userId, String password);

    /**
     * 修改密码
     */
    void editPass(String password, String pwd);

    /**
     * 修改微聊号
     */
    void editChatNo(String chatNo);

    /**
     * 获取基本信息
     */
    MyVo09 getInfo();

    /**
     * 获取二维码
     */
    String getQrCode();

    /**
     * 获取二维码
     */
    String resetQrCode();

    /**
     * 用户注销
     */
    void deleted();

    /**
     * 执行登录/返回token
     */
    Dict doLogin(AuthenticationToken authenticationToken, String cid);

    /**
     * 退出登录
     */
    void logout();

    /**
     * 绑定cid
     */
    void bindCid(String cid);
}
