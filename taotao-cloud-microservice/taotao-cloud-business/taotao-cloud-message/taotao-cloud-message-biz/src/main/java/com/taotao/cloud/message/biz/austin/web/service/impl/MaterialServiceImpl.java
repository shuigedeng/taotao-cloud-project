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

package com.taotao.cloud.message.biz.austin.web.service.impl;

import org.dromara.hutoolcore.util.IdUtil;
import org.dromara.hutoolcore.util.StrUtil;
import org.dromara.hutoolhttp.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMediaUploadRequest;
import com.dingtalk.api.response.OapiMediaUploadResponse;
import com.google.common.base.Throwables;
import com.taobao.api.FileItem;
import com.taotao.cloud.message.biz.austin.common.constant.CommonConstant;
import com.taotao.cloud.message.biz.austin.common.constant.SendAccountConstant;
import com.taotao.cloud.message.biz.austin.common.dto.account.EnterpriseWeChatRobotAccount;
import com.taotao.cloud.message.biz.austin.common.enums.FileType;
import com.taotao.cloud.message.biz.austin.common.enums.RespStatusEnum;
import com.taotao.cloud.message.biz.austin.common.vo.BasicResultVO;
import com.taotao.cloud.message.biz.austin.handler.domain.wechat.robot.EnterpriseWeChatRootResult;
import com.taotao.cloud.message.biz.austin.support.utils.AccountUtils;
import com.taotao.cloud.message.biz.austin.web.service.MaterialService;
import com.taotao.cloud.message.biz.austin.web.utils.SpringFileUtils;
import com.taotao.cloud.message.biz.austin.web.vo.UploadResponseVo;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 3y
 */
@Slf4j
@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AccountUtils accountUtils;

    private static final String DING_DING_URL = "https://oapi.dingtalk.com/media/upload";
    private static final String ENTERPRISE_WE_CHAT_ROBOT_URL =
            "https://qyapi.weixin.qq.com/cgi-bin/webhook/upload_media?key=<KEY>&type=<TYPE>";

    @Override
    public BasicResultVO dingDingMaterialUpload(MultipartFile file, String sendAccount, String fileType) {
        OapiMediaUploadResponse rsp;
        try {
            String accessToken =
                    redisTemplate.opsForValue().get(SendAccountConstant.DING_DING_ACCESS_TOKEN_PREFIX + sendAccount);
            DingTalkClient client = new DefaultDingTalkClient(DING_DING_URL);
            OapiMediaUploadRequest req = new OapiMediaUploadRequest();
            FileItem item = new FileItem(
                    new StringBuilder()
                            .append(IdUtil.fastSimpleUUID())
                            .append(file.getOriginalFilename())
                            .toString(),
                    file.getInputStream());
            req.setMedia(item);
            req.setType(FileType.getNameByCode(fileType));
            rsp = client.execute(req, accessToken);
            if (rsp.getErrcode() == 0L) {
                return new BasicResultVO(
                        RespStatusEnum.SUCCESS,
                        UploadResponseVo.builder().id(rsp.getMediaId()).build());
            }
            log.error("MaterialService#dingDingMaterialUpload fail:{}", rsp.getErrmsg());
        } catch (Exception e) {
            log.error("MaterialService#dingDingMaterialUpload fail:{}", Throwables.getStackTraceAsString(e));
        }
        return BasicResultVO.fail("未知错误，联系管理员");
    }

    @Override
    public BasicResultVO enterpriseWeChatRootMaterialUpload(
            MultipartFile multipartFile, String sendAccount, String fileType) {
        try {
            EnterpriseWeChatRobotAccount weChatRobotAccount =
                    accountUtils.getAccountById(Integer.valueOf(sendAccount), EnterpriseWeChatRobotAccount.class);
            String key = weChatRobotAccount
                    .getWebhook()
                    .substring(weChatRobotAccount.getWebhook().indexOf(CommonConstant.EQUAL_STRING) + 1);
            String url = ENTERPRISE_WE_CHAT_ROBOT_URL.replace("<KEY>", key).replace("<TYPE>", "file");
            String response = HttpRequest.post(url)
                    .form(IdUtil.fastSimpleUUID(), SpringFileUtils.getFile(multipartFile))
                    .execute()
                    .body();
            EnterpriseWeChatRootResult result = JSON.parseObject(response, EnterpriseWeChatRootResult.class);
            if (result.getErrcode() == 0) {
                return new BasicResultVO(
                        RespStatusEnum.SUCCESS,
                        UploadResponseVo.builder().id(result.getMediaId()).build());
            }
            log.error("MaterialService#enterpriseWeChatRootMaterialUpload fail:{}", result.getErrmsg());
        } catch (Exception e) {
            log.error(
                    "MaterialService#enterpriseWeChatRootMaterialUpload fail:{}", Throwables.getStackTraceAsString(e));
        }
        return BasicResultVO.fail("未知错误，联系管理员");
    }

    @Override
    public BasicResultVO enterpriseWeChatMaterialUpload(
            MultipartFile multipartFile, String sendAccount, String fileType) {
        try {
            WxCpDefaultConfigImpl accountConfig =
                    accountUtils.getAccountById(Integer.valueOf(sendAccount), WxCpDefaultConfigImpl.class);
            WxCpServiceImpl wxCpService = new WxCpServiceImpl();
            wxCpService.setWxCpConfigStorage(accountConfig);
            WxMediaUploadResult result = wxCpService
                    .getMediaService()
                    .upload(FileType.getNameByCode(fileType), SpringFileUtils.getFile(multipartFile));
            if (StrUtil.isNotBlank(result.getMediaId())) {
                return new BasicResultVO(
                        RespStatusEnum.SUCCESS,
                        UploadResponseVo.builder().id(result.getMediaId()).build());
            }
            log.error("MaterialService#enterpriseWeChatMaterialUpload fail:{}", JSON.toJSONString(result));
        } catch (Exception e) {
            log.error("MaterialService#enterpriseWeChatMaterialUpload fail:{}", Throwables.getStackTraceAsString(e));
        }
        return BasicResultVO.fail("未知错误，联系管理员");
    }
}
