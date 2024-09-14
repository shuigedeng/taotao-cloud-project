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

package com.taotao.cloud.auth.infrastructure.extension.qrcocde.tmp1;

import com.taotao.boot.cache.redis.repository.RedisRepository;
import com.taotao.boot.common.exception.BaseException;
import com.taotao.boot.common.utils.log.LogUtils;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QrCodeLoginService {

    @Autowired
    private RedisRepository redisRepository;

    public String getScanUUID(String uuid) {
        return "QR_CODE_UUID:" + uuid;
    }

    /**
     * 生成uuid
     */
    public String generateUUID() {
        try {
            String uuid = UUID.randomUUID().toString();
            redisRepository
                    .opsForValue()
                    .set(getScanUUID(uuid), CodeUtil.getUnusedCodeInfo(), 30 * 1000, TimeUnit.MINUTES);
            return uuid;
        } catch (Exception e) {
            LogUtils.warn("redis二维码生成异常{}", e.getMessage());
            throw new BaseException("二维码生成异常");
        }
    }

    /**
     * uuid状态信息
     */
    public String getInfoUUID(String uuid) {
        Object object = redisRepository.opsForValue().get(getScanUUID(uuid));
        return (String) object;
    }

    /**
     * 扫描登录，去确认二维码
     */
    public CodeData scanQrLogin(String uuid, String account) {
        try {
            Object o = redisRepository.opsForValue().get(getScanUUID(uuid));
            if (null == o) {
                throw new BaseException(400, "二维码异常，请重新扫描");
            }

            CodeData codeData = (CodeData) o;
            // 获取状态
            CodeStatusEnum codeStatus = codeData.getCodeStatus();
            // 如果未使用
            if (codeStatus == CodeStatusEnum.UNUSED) {
                redisRepository
                        .opsForValue()
                        .set(getScanUUID(uuid), CodeUtil.getConfirmingCodeInfo(), 30 * 10000, TimeUnit.MINUTES);
                // 你的逻辑

                // return new CommonResult<>("请确认登录", 200, null);
            }
        } catch (Exception e) {
            LogUtils.warn("二维码异常{}", e.getMessage());
            // return new CommonResult<>("内部错误", 500);
        }
        // return new CommonResult<>("二维码异常，请重新扫描", 400);
        return null;
    }

    /**
     * 确认登录，返回学生token以及对应信息
     *
     * @param uuid
     * @param id   学生id
     * @return
     */
    public boolean confirmQrLogin(String uuid, String id) {
        try {
            CodeData codeData = (CodeData) redisRepository.opsForValue().get(getScanUUID(uuid));
            if (null == codeData) {
                throw new BaseException(400, "二维码已经失效，请重新扫描");
            }

            // 获取状态
            CodeStatusEnum codeStatus = codeData.getCodeStatus();
            // 如果正在确认中,查询学生信息
            if (codeStatus == CodeStatusEnum.CONFIRMING) {
                // 你的逻辑

                // 生成token
                // String token = TokenUtil.token(studentLoginVO.getAccount());

                // redis二维码状态修改，PC可以获取到
                // redisTemplate.opsForValue().set(RedisKeyUtil.getScanUUID(uuid),
                // 	CodeUtil.getConfirmedCodeInfo(token), RedisKeyUtil.getTimeOut(),
                // TimeUnit.MINUTES);

                // return new CommonResult<>("登陆成功", 200);
            }
            // return new CommonResult<>("二维码异常，请重新扫描", 400);
        } catch (Exception e) {
            LogUtils.error("确认二维码异常{}", e);
            // return new CommonResult<>("内部错误", 500);
        }
        return false;
    }
}
