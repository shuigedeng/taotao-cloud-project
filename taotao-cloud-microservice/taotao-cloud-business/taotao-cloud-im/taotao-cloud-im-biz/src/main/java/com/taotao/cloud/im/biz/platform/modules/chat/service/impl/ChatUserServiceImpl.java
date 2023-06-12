/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.im.biz.platform.modules.chat.service.impl;

import com.platform.common.config.PlatformConfig;
import com.platform.common.constant.ApiConstant;
import com.platform.common.constant.HeadConstant;
import com.platform.common.enums.GenderEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.exception.LoginException;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.shiro.utils.Md5Utils;
import com.platform.common.utils.ServletUtils;
import com.platform.common.utils.redis.GeoHashUtils;
import com.platform.common.utils.redis.RedisUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.auth.service.TokenService;
import com.platform.modules.auth.vo.AuthVo01;
import com.platform.modules.chat.dao.ChatUserDao;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.service.ChatUserService;
import com.platform.modules.chat.vo.MyVo09;
import com.platform.modules.push.service.ChatPushService;
import jakarta.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/** 用户表 服务层实现 q3z3 */
@Slf4j
@Service("chatUserService")
public class ChatUserServiceImpl extends BaseServiceImpl<ChatUser> implements ChatUserService {

    @Resource
    private ChatUserDao chatUserDao;

    @Resource
    private TokenService tokenService;

    @Resource
    private ChatPushService chatPushService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private GeoHashUtils geoHashUtils;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatUserDao);
    }

    @Override
    public List<ChatUser> queryList(ChatUser t) {
        List<ChatUser> dataList = chatUserDao.queryList(t);
        return dataList;
    }

    @Override
    public void register(AuthVo01 authVo) {
        String phone = authVo.getPhone();
        String password = authVo.getPassword();
        String nickName = authVo.getNickName();
        String msg = "此手机号码已注册过，请勿重复注册";
        // 验证手机号是否注册过
        if (this.queryCount(new ChatUser().setPhone(phone)) > 0) {
            throw new BaseException(msg);
        }
        String salt = RandomUtil.randomString(4);
        String chatNo = IdUtil.simpleUUID();
        ChatUser cu = new ChatUser()
                .setNickName(nickName)
                .setChatNo(chatNo)
                .setGender(GenderEnum.MALE)
                .setPortrait(ApiConstant.DEFAULT_PORTRAIT)
                .setSalt(salt)
                .setPhone(phone)
                .setPassword(Md5Utils.credentials(password, salt))
                .setStatus(YesOrNoEnum.YES)
                .setCreateTime(DateUtil.date());
        try {
            this.add(cu);
        } catch (org.springframework.dao.DuplicateKeyException e) {
            throw new BaseException(msg);
        }
    }

    @Override
    public ChatUser queryByPhone(String phone) {
        return this.queryOne(new ChatUser().setPhone(phone));
    }

    @Override
    public void resetPass(Long userId, String password) {
        String salt = RandomUtil.randomString(4);
        ChatUser chatUser =
                new ChatUser().setUserId(userId).setSalt(salt).setPassword(Md5Utils.credentials(password, salt));
        this.updateById(chatUser);
    }

    @Override
    public void editPass(String password, String pwd) {
        // 当前用户
        ChatUser cu = getById(ShiroUtils.getUserId());
        if (!Md5Utils.credentials(password, cu.getSalt()).equalsIgnoreCase(cu.getPassword())) {
            throw new BaseException("旧密码不正确");
        }
        String salt = RandomUtil.randomString(4);
        ChatUser chatUser =
                new ChatUser().setUserId(cu.getUserId()).setSalt(salt).setPassword(Md5Utils.credentials(pwd, salt));
        this.updateById(chatUser);
    }

    @Override
    public void editChatNo(String chatNo) {
        Long userId = ShiroUtils.getUserId();
        String errMsg = "微聊号已被占用，请重新输入";
        // 校验
        ChatUser cu = this.queryOne(new ChatUser().setChatNo(chatNo));
        if (cu != null && !userId.equals(cu.getUserId())) {
            throw new BaseException(errMsg);
        }
        try {
            // 更新
            this.updateById(new ChatUser().setUserId(userId).setChatNo(chatNo));
        } catch (org.springframework.dao.DuplicateKeyException e) {
            throw new BaseException(errMsg);
        }
    }

    @Override
    public MyVo09 getInfo() {
        // 当前用户
        ChatUser cu = findById(ShiroUtils.getUserId());
        return BeanUtil.toBean(cu, MyVo09.class).setPhone(DesensitizedUtil.mobilePhone(cu.getPhone()));
    }

    @Override
    public String getQrCode() {
        String key = ApiConstant.REDIS_QR_CODE + ShiroUtils.getUserId();
        if (redisUtils.hasKey(key)) {
            return redisUtils.get(key);
        }
        return resetQrCode();
    }

    @Override
    public String resetQrCode() {
        Long userId = ShiroUtils.getUserId();
        ChatUser chatUser = getById(userId);
        String content = ApiConstant.QR_CODE_USER + userId;
        File original = HttpUtil.downloadFileFromUrl(
                chatUser.getPortrait() + ApiConstant.IMAGE_PARAM, FileUtil.file(PlatformConfig.UPLOAD_PATH));
        QrConfig qrConfig = QrConfig.create()
                .setWidth(ApiConstant.QR_CODE_SIZE)
                .setHeight(ApiConstant.QR_CODE_SIZE)
                .setImg(original);
        byte[] data = QrCodeUtil.generatePng(content, qrConfig);
        // 删除临时图片
        FileUtil.del(original);
        String value = ApiConstant.BASE64_PREFIX.concat(Base64.encode(data));
        redisUtils.set(ApiConstant.REDIS_QR_CODE + userId, value, ApiConstant.REDIS_QR_CODE_TIME, TimeUnit.DAYS);
        return value;
    }

    @Transactional
    @Override
    public void deleted() {
        // 移除缓存
        removeCache();
        // 更新用户
        ChatUser cu = new ChatUser().setUserId(ShiroUtils.getUserId()).setDeletedTime(DateUtil.date());
        this.updateById(cu);
    }

    @Transactional
    @Override
    public Dict doLogin(AuthenticationToken authenticationToken, String cid) {
        String msg = null;
        try {
            ShiroUtils.getSubject().login(authenticationToken);
        } catch (LoginException e) {
            msg = e.getMessage();
        } catch (AuthenticationException e) {
            msg = "手机号或密码不正确";
        } catch (Exception e) {
            msg = "未知异常";
            log.error(msg, e);
        }
        if (!StringUtils.isEmpty(msg)) {
            throw new BaseException(msg);
        }
        Long userId = ShiroUtils.getUserId();
        String token = this.getById(userId).getToken();
        if (!StringUtils.isEmpty(token)) {
            tokenService.deleteToken(token);
        }
        // 生成新TOKEN
        token = tokenService.generateToken();
        // 更新token
        String version = ServletUtils.getRequest().getHeader(HeadConstant.VERSION);
        this.updateById(
                new ChatUser().setCid(cid).setToken(token).setUserId(userId).setVersion(version));
        // 注册别名
        chatPushService.setAlias(userId, cid);
        // 拉取离线消息
        chatPushService.pullOffLine(userId);
        return Dict.create().set("token", token);
    }

    @Override
    public void logout() {
        try {
            // 移除缓存
            removeCache();
            // 执行退出
            ShiroUtils.getSubject().logout();
            log.info("退出成功。。。。");
        } catch (Exception ex) {
            log.error("退出异常", ex);
        }
    }

    @Override
    public void bindCid(String cid) {
        Long userId = ShiroUtils.getUserId();
        String version = ServletUtils.getRequest().getHeader(HeadConstant.VERSION);
        ChatUser chatUser = getById(userId);
        if (!cid.equalsIgnoreCase(chatUser.getCid())) {
            // 更新cid
            this.updateById(new ChatUser().setCid(cid).setUserId(userId).setVersion(version));
            // 注册别名
            chatPushService.setAlias(userId, cid);
        } else if (!version.equalsIgnoreCase(chatUser.getVersion())) {
            // 更新版本
            this.updateById(new ChatUser().setVersion(version).setUserId(userId));
        }
        // 拉取离线消息
        chatPushService.pullOffLine(userId);
    }

    /** 移除缓存 */
    private void removeCache() {
        Long userId = ShiroUtils.getUserId();
        ChatUser chatUser = this.getById(userId);
        if (chatUser != null) {
            // 清理token
            String token = chatUser.getToken();
            if (!StringUtils.isEmpty(token)) {
                tokenService.deleteToken(token);
            }
            // 清理cid
            String cid = chatUser.getCid();
            if (!StringUtils.isEmpty(cid)) {
                chatPushService.delAlias(userId, cid);
            }
        }
        String userStr = NumberUtil.toStr(userId);
        // 附近的人
        geoHashUtils.remove(ApiConstant.REDIS_NEAR, userStr);
        // 摇一摇
        redisUtils.lRemove(ApiConstant.REDIS_SHAKE, 0, userStr);
    }
}
