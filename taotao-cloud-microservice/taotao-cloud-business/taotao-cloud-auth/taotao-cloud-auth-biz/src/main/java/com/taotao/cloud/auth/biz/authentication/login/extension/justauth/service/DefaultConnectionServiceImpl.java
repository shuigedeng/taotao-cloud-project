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

package com.taotao.cloud.auth.biz.authentication.login.extension.justauth.service;

import static java.util.Objects.nonNull;

import com.taotao.cloud.auth.biz.authentication.login.extension.justauth.entity.ConnectionDto;
import com.taotao.cloud.auth.biz.authentication.login.extension.justauth.entity.ConnectionKey;
import com.taotao.cloud.auth.biz.authentication.login.extension.justauth.properties.Auth2Properties;
import com.taotao.cloud.auth.biz.authentication.login.extension.justauth.properties.HttpConfigProperties;
import com.taotao.cloud.auth.biz.authentication.login.extension.justauth.repository.UsersConnectionRepository;
import com.taotao.cloud.auth.biz.authentication.login.extension.justauth.repository.UsersConnectionTokenRepository;
import com.taotao.cloud.auth.biz.authentication.login.extension.justauth.repository.exception.UpdateConnectionException;
import com.taotao.cloud.auth.biz.exception.RegisterUserFailureException;
import com.taotao.cloud.auth.biz.exception.UnBindingException;
import com.taotao.cloud.auth.biz.uaa.enums.ErrorCodeEnum;
import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.security.justauth.justauth.AuthTokenPo;
import com.taotao.cloud.security.justauth.justauth.ConnectionData;
import com.taotao.cloud.security.justauth.justauth.request.Auth2DefaultRequest;
import com.taotao.cloud.security.justauth.justauth.util.JustAuthUtil;
import com.xkcoding.http.config.HttpConfig;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * 默认的第三方授权登录时自动注册处理器。<br>
 * {@link #signUp(AuthUser, String, String)} 功能：第三方登录自动注册时, 根据 第三方的 authUser 注册为本地账户的用户,
 * 用户名的生成规则由 {@link UmsUserDetailsService#generateUsernames(AuthUser)} 定义, 默认规则为:
 * username 或 username + "_" + providerId 或 username + "_" + providerId + "_" + providerUserId.
 * 如需自定义用户名生成逻辑, 重新实现 {@link UmsUserDetailsService#generateUsernames(AuthUser)} 方法即可
 *
 * @author YongWu zheng
 * @version V2.0  Created by 2020/5/14 22:32
 * @see ConnectionService
 */
@Slf4j
public class DefaultConnectionServiceImpl implements ConnectionService {
    /**
     * {@link HttpConfig#getTimeout()}, 单位毫秒,
     * 返回用户设置的超时时间{@link HttpConfigProperties#getTimeout()}，单位毫秒.
     */
    private final Integer timeout;

    private final UmsUserDetailsService userDetailsService;
    private final String defaultAuthorities;
    private final UsersConnectionRepository usersConnectionRepository;
    private final UsersConnectionTokenRepository usersConnectionTokenRepository;
    private final Auth2StateCoder auth2StateCoder;

    public DefaultConnectionServiceImpl(
            UmsUserDetailsService userDetailsService,
            Auth2Properties auth2Properties,
            UsersConnectionRepository usersConnectionRepository,
            @Autowired(required = false) UsersConnectionTokenRepository usersConnectionTokenRepository,
            Auth2StateCoder auth2StateCoder) {
        this.userDetailsService = userDetailsService;
        this.defaultAuthorities = auth2Properties.getDefaultAuthorities();
        this.usersConnectionRepository = usersConnectionRepository;
        this.usersConnectionTokenRepository = usersConnectionTokenRepository;
        this.timeout = auth2Properties.getProxy().getHttpConfig().getTimeout();
        this.auth2StateCoder = auth2StateCoder;
    }

    @Override
    @NonNull
    @Transactional(
            rollbackFor = {Exception.class},
            propagation = Propagation.REQUIRES_NEW)
    public UserDetails signUp(@NonNull AuthUser authUser, @NonNull String providerId, @NonNull String encodeState)
            throws RegisterUserFailureException {
        // 这里为第三方登录自动注册时调用，所以这里不需要实现对用户信息的注册，可以在用户登录完成后提示用户修改用户信息。
        String username = authUser.getUsername();
        String[] usernames = userDetailsService.generateUsernames(authUser);

        try {
            // 重名检查
            username = null;
            final List<Boolean> existedByUserIds = userDetailsService.existedByUsernames(usernames);
            for (int i = 0, len = existedByUserIds.size(); i < len; i++) {
                if (!existedByUserIds.get(i)) {
                    username = usernames[i];
                    break;
                }
            }
            // 用户重名, 自动注册失败
            if (username == null) {
                throw new RegisterUserFailureException(ErrorCodeEnum.USERNAME_USED, authUser.getUsername());
            }

            // 解密 encodeState  https://gitee.com/pcore/just-auth-spring-security-starter/issues/I22JC7
            String decodeState;
            if (this.auth2StateCoder != null) {
                decodeState = this.auth2StateCoder.decode(encodeState);
            } else {
                decodeState = encodeState;
            }
            // 注册到本地账户
            UserDetails userDetails =
                    userDetailsService.registerUser(authUser, username, defaultAuthorities, decodeState);
            // 第三方授权登录信息绑定到本地账号, 且添加第三方授权登录信息到 user_connection 与 auth_token
            registerConnection(providerId, authUser, userDetails);

            return userDetails;
        } catch (Exception e) {
            log.error(
                    String.format(
                            "OAuth2自动注册失败: error=%s, username=%s, authUser=%s",
                            e.getMessage(), username, JsonUtils.toJson(authUser)),
                    e);
            throw new RegisterUserFailureException(ErrorCodeEnum.USER_REGISTER_FAILURE, username);
        }
    }

    @Override
    @Transactional(
            rollbackFor = {Exception.class},
            propagation = Propagation.REQUIRED)
    public void updateUserConnectionAndAuthToken(@NonNull AuthUser authUser, @NonNull ConnectionData data)
            throws UpdateConnectionException {
        ConnectionData connectionData = null;
        try {
            // 获取 AuthTokenPo
            AuthToken token = authUser.getToken();
            AuthTokenPo authToken = JustAuthUtil.getAuthTokenPo(token, data.getProviderId(), this.timeout);
            authToken.setId(data.getTokenId());
            // 有效期转时间戳
            Auth2DefaultRequest.expireIn2Timestamp(this.timeout, token.getExpireIn(), authToken);

            // 获取最新的 ConnectionData
            connectionData =
                    JustAuthUtil.getConnectionData(data.getProviderId(), authUser, data.getUserId(), authToken);
            connectionData.setUserId(data.getUserId());
            connectionData.setTokenId(data.getTokenId());

            // 更新 connectionData
            usersConnectionRepository.updateConnection(connectionData);
            // 更新 AuthTokenPo
            if (nonNull(usersConnectionTokenRepository)) {
                usersConnectionTokenRepository.updateAuthToken(authToken);
            }
        } catch (Exception e) {
            log.error("更新第三方用户信息异常: " + e.getMessage());
            throw new UpdateConnectionException(ErrorCodeEnum.UPDATE_CONNECTION_DATA_FAILURE, connectionData, e);
        }
    }

    @Override
    @Transactional(
            rollbackFor = {Exception.class},
            propagation = Propagation.REQUIRED)
    public void binding(@NonNull UserDetails principal, @NonNull AuthUser authUser, @NonNull String providerId) {
        // 第三方授权登录信息绑定到本地账号, 且添加第三方授权登录信息到 user_connection 与 auth_token
        registerConnection(providerId, authUser, principal);
    }

    @Override
    @Transactional(
            rollbackFor = {Exception.class},
            propagation = Propagation.REQUIRED)
    public void unbinding(@NonNull String userId, @NonNull String providerId, @NonNull String providerUserId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isCurrentUserAndValid = authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.getName().equals(userId);
        // 用户未登录或不是当前用户
        if (!isCurrentUserAndValid) {
            log.warn(
                    "用户 {} 进行解绑操作时, 用户未登录或不是当前用户; userId: {}, providerId: {}, providerUserId: {}",
                    authentication.getName(),
                    userId,
                    providerId,
                    providerUserId);
            throw new UnBindingException(ErrorCodeEnum.UN_BINDING_ERROR, userId);
        }
        // 解除绑定(第三方)
        usersConnectionRepository.removeConnection(userId, new ConnectionKey(providerId, providerUserId));
    }

    @Override
    @Nullable
    public List<ConnectionData> findConnectionByProviderIdAndProviderUserId(
            @NonNull String providerId, @NonNull String providerUserId) {
        return usersConnectionRepository.findConnectionByProviderIdAndProviderUserId(providerId, providerUserId);
    }

    @NonNull
    @Override
    public MultiValueMap<String, ConnectionDto> listAllConnections(@NonNull String userId) {
        MultiValueMap<String, ConnectionData> allConnections = usersConnectionRepository.findAllConnections(userId);
        Set<Map.Entry<String, List<ConnectionData>>> entrySet = allConnections.entrySet();
        MultiValueMap<String, ConnectionDto> connectionMap = new LinkedMultiValueMap<>(allConnections.size());
        for (Map.Entry<String, List<ConnectionData>> entry : entrySet) {
            List<ConnectionDto> connectionDtoList = entry.getValue().stream()
                    .map(data -> ConnectionDto.builder()
                            .tokenId(data.getTokenId())
                            .providerId(data.getProviderId())
                            .providerUserId(data.getProviderUserId())
                            .build())
                    .collect(Collectors.toList());
            connectionMap.put(entry.getKey(), connectionDtoList);
        }

        return connectionMap;
    }

    /**
     * 第三方授权登录信息绑定到本地账号, 且添加第三方授权登录信息到 user_connection 与 auth_token
     *
     * @param providerId 第三方服务商
     * @param authUser   {@link AuthUser}
     * @throws RegisterUserFailureException 注册失败
     */
    private void registerConnection(
            @NonNull String providerId, @NonNull AuthUser authUser, @NonNull UserDetails userDetails)
            throws RegisterUserFailureException {

        // 注册第三方授权登录信息到 user_connection 与 auth_token
        AuthToken token = authUser.getToken();
        AuthTokenPo authToken = JustAuthUtil.getAuthTokenPo(token, providerId, this.timeout);
        // 有效期转时间戳
        Auth2DefaultRequest.expireIn2Timestamp(this.timeout, token.getExpireIn(), authToken);

        try {
            // 添加 token
            if (nonNull(usersConnectionTokenRepository)) {
                usersConnectionTokenRepository.saveAuthToken(authToken);
            }

            // 添加到 第三方登录记录表
            addConnectionData(providerId, authUser, userDetails.getUsername(), authToken);
        } catch (Exception e) {
            String msg;
            if (authToken.getId() == null) {
                try {
                    // 再次添加 token
                    if (nonNull(usersConnectionTokenRepository)) {
                        usersConnectionTokenRepository.saveAuthToken(authToken);
                    }
                    // 再次添加到 第三方登录记录表
                    addConnectionData(providerId, authUser, userDetails.getUsername(), authToken);
                } catch (Exception ex) {
                    msg = String.format(
                            "第三方授权登录自动注册时: 本地账户注册成功, %s, 添加第三方授权登录信息失败: %s", userDetails, JsonUtils.toJson(authUser));
                    log.error(msg, e);
                    throw new RegisterUserFailureException(
                            ErrorCodeEnum.USER_REGISTER_OAUTH2_FAILURE, ex, userDetails.getUsername());
                }
            } else {
                try {
                    // authToken 保存成功, authUser保存失败, 再次添加到 第三方登录记录表
                    addConnectionData(providerId, authUser, userDetails.getUsername(), authToken);
                } catch (Exception exception) {
                    msg = String.format(
                            "第三方授权登录自动注册时: 本地账户注册成功, %s, 添加第三方授权登录信息失败: %s, 但 AuthToken 能成功执行 sql, 但已回滚: " + "%s",
                            userDetails, authUser.getRawUserInfo(), JsonUtils.toJson(authToken));
                    log.error(msg, e);
                    throw new RegisterUserFailureException(
                            ErrorCodeEnum.USER_REGISTER_OAUTH2_FAILURE, userDetails.getUsername());
                }
            }
        }
    }

    /**
     * 添加到 第三方登录记录表
     *
     * @param providerId 第三方服务商
     * @param authUser   authUser
     * @param userId     本地账户用户 Id
     * @param authToken  authToken
     */
    private void addConnectionData(
            @NonNull String providerId,
            @NonNull AuthUser authUser,
            @NonNull String userId,
            @NonNull AuthTokenPo authToken) {
        ConnectionData connectionData = JustAuthUtil.getConnectionData(providerId, authUser, userId, authToken);
        usersConnectionRepository.addConnection(connectionData);
    }
}
