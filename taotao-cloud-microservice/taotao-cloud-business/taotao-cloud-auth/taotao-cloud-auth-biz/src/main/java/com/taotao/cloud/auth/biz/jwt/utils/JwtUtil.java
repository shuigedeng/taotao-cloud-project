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

package com.taotao.cloud.auth.biz.jwt.utils;

import com.taotao.boot.common.constant.StrPool;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.utils.date.DateUtils;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.auth.biz.jwt.model.ContextConstants;
import com.taotao.cloud.auth.biz.jwt.model.ExceptionCode;
import com.taotao.cloud.auth.biz.jwt.model.Token;
import io.jsonwebtoken.*;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import javax.crypto.spec.SecretKeySpec;
import org.dromara.hutool.core.text.StrUtil;

/**
 * Secure工具类
 */
public final class JwtUtil {

    private JwtUtil() {}

    /**
     * 将 签名（JWT_SIGN_KEY） 编译成BASE64编码
     */
    private static final String BASE64_SECURITY =
            Base64.getEncoder()
                    .encodeToString(ContextConstants.JWT_SIGN_KEY.getBytes(StandardCharsets.UTF_8));

    /**
     * authorization: Basic clientId:clientSec 解析请求头中存储的 client 信息
     *
     * <p>Basic clientId:clientSec -截取-> clientId:clientSec后调用 extractClient 解码
     *
     * @param basicHeader Basic clientId:clientSec
     * @return clientId:clientSec
     */
    public static String[] getClient(String basicHeader) {
        if (StrUtil.isEmpty(basicHeader)
                || !basicHeader.startsWith(ContextConstants.BASIC_HEADER_PREFIX)) {
            // throw BizException.wrap(ExceptionCode.JWT_BASIC_INVALID);
            throw new BusinessException(ExceptionCode.JWT_BASIC_INVALID.name());
        }

        String decodeBasic =
                StrUtil.subAfter(basicHeader, ContextConstants.BASIC_HEADER_PREFIX, false);
        return extractClient(decodeBasic);
    }

    /**
     * 解析请求头中存储的 client 信息 clientId:clientSec 解码
     */
    public static String[] extractClient(String client) {
        String token = base64Decoder(client);
        int index = token.indexOf(StrPool.COLON);
        if (index == -1) {
            // throw BizException.wrap(ExceptionCode.JWT_BASIC_INVALID);
            throw new BusinessException(ExceptionCode.JWT_BASIC_INVALID.name());
        } else {
            return new String[] {token.substring(0, index), token.substring(index + 1)};
        }
    }

    /**
     * 使用 Base64 解码
     *
     * @param val 参数
     * @return 解码后的值
     */
    public static String base64Decoder(String val) {
        byte[] decoded = Base64.getDecoder().decode(val.getBytes(StandardCharsets.UTF_8));
        return new String(decoded, StandardCharsets.UTF_8);
    }

    /**
     * 创建令牌
     *
     * @param user   user
     * @param expire 过期时间（秒)
     * @return jwt
     */
    public static Token createJwt(Map<String, String> user, long expire) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // 生成签名密钥
        byte[] apiKeySecretBytes = Base64.getDecoder().decode(BASE64_SECURITY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        // 添加构成JWT的类
        JwtBuilder builder =
                Jwts.builder()
                        .setHeaderParam("typ", "JsonWebToken")
                        .signWith(signatureAlgorithm, signingKey);

        // 设置JWT参数
        user.forEach(builder::claim);

        // 添加Token过期时间
        // allowedClockSkewMillis
        long expMillis = nowMillis + expire * 1000;
        Date exp = new Date(expMillis);
        builder
                // 发布时间
                .setIssuedAt(now)
                // token从时间什么开始生效
                .setNotBefore(now)
                // token从什么时间截止生效
                .setExpiration(exp);

        // 组装Token信息
        Token tokenInfo = new Token();
        tokenInfo.setToken(builder.compact());
        tokenInfo.setExpire(expire);
        tokenInfo.setExpiration(DateUtils.date2LocalDateTime(exp));
        return tokenInfo;
    }

    /**
     * 解析jwt
     *
     * @param jsonWebToken            待解析token
     * @param allowedClockSkewSeconds 允许的时间差
     * @return Claims
     */
    public static Claims parseJwt(String jsonWebToken, long allowedClockSkewSeconds) {
        try {
            // return Jwts.builder().
            // .signWith(Base64.getDecoder().decode(BASE64_SECURITY))
            // .setAllowedClockSkewSeconds(allowedClockSkewSeconds)
            // .build()
            // .parseClaimsJws(jsonWebToken)
            // .getBody();
            return null;
        } catch (ExpiredJwtException ex) {
            LogUtils.error("token=[{}], 过期", jsonWebToken, ex);
            // 过期
            // throw new BizException(ExceptionCode.JWT_TOKEN_EXPIRED.getCode(),
            //		ExceptionCode.JWT_TOKEN_EXPIRED.getMsg(), ex);
            throw new BusinessException(
                    ExceptionCode.JWT_TOKEN_EXPIRED.getCode(),
                    ExceptionCode.JWT_TOKEN_EXPIRED.getMsg());
        } catch (SignatureException ex) {
            LogUtils.error("token=[{}] 签名错误", jsonWebToken, ex);
            // 签名错误
            // throw new BizException(ExceptionCode.JWT_SIGNATURE.getCode(),
            //		ExceptionCode.JWT_SIGNATURE.getMsg(), ex);
            throw new BusinessException(
                    ExceptionCode.JWT_SIGNATURE.getCode(), ExceptionCode.JWT_SIGNATURE.getMsg());
        } catch (IllegalArgumentException ex) {
            LogUtils.error("token=[{}] 为空", jsonWebToken, ex);
            // token 为空
            // throw new BizException(ExceptionCode.JWT_ILLEGAL_ARGUMENT.getCode(),
            //		ExceptionCode.JWT_ILLEGAL_ARGUMENT.getMsg(), ex);
            throw new BusinessException(
                    ExceptionCode.JWT_ILLEGAL_ARGUMENT.getCode(),
                    ExceptionCode.JWT_ILLEGAL_ARGUMENT.getMsg());
        } catch (Exception e) {
            LogUtils.error(
                    "token=[{}] errCode:{}, message:{}",
                    jsonWebToken,
                    ExceptionCode.JWT_PARSER_TOKEN_FAIL.getCode(),
                    e.getMessage(),
                    e);
            // throw new BizException(JWT_PARSER_TOKEN_FAIL.getCode(),
            // JWT_PARSER_TOKEN_FAIL.getMsg(),
            //		e);
            throw new BusinessException(
                    ExceptionCode.JWT_PARSER_TOKEN_FAIL.getCode(),
                    ExceptionCode.JWT_PARSER_TOKEN_FAIL.getMsg());
        }
    }

    public static String getToken(String token) {
        if (token == null) {
            // throw BizException.wrap(ExceptionCode.JWT_PARSER_TOKEN_FAIL);
            throw new BusinessException(ExceptionCode.JWT_PARSER_TOKEN_FAIL.name());
        }
        if (token.startsWith(ContextConstants.BEARER_HEADER_PREFIX)) {
            return StrUtil.subAfter(token, ContextConstants.BEARER_HEADER_PREFIX, false);
        }
        LogUtils.info("jsonWebToken={}", token);
        // throw BizException.wrap(ExceptionCode.JWT_PARSER_TOKEN_FAIL);
        throw new BusinessException(ExceptionCode.JWT_PARSER_TOKEN_FAIL.name());
    }

    /**
     * 获取Claims
     *
     * @param token                   待解析token
     * @param allowedClockSkewSeconds 允许存在的时间差
     */
    public static Claims getClaims(String token, long allowedClockSkewSeconds) {
        if (token == null) {
            // throw BizException.wrap(ExceptionCode.JWT_PARSER_TOKEN_FAIL);
            throw new BusinessException(ExceptionCode.JWT_PARSER_TOKEN_FAIL.name());
        }
        if (token.startsWith(ContextConstants.BEARER_HEADER_PREFIX)) {
            String headStr = StrUtil.subAfter(token, ContextConstants.BEARER_HEADER_PREFIX, false);
            return parseJwt(headStr, allowedClockSkewSeconds);
        }
        LogUtils.info("jsonWebToken={}", token);
        // throw BizException.wrap(ExceptionCode.JWT_PARSER_TOKEN_FAIL);
        throw new BusinessException(ExceptionCode.JWT_PARSER_TOKEN_FAIL.name());
    }
}
