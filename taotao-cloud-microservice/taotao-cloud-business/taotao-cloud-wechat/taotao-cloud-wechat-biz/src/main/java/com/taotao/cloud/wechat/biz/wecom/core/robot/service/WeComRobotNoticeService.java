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

package com.taotao.cloud.wechat.biz.wecom.core.robot.service;

import static cn.bootx.starter.wecom.code.WeComCode.ROBOT_UPLOAD_URL;

import cn.bootx.common.core.exception.DataNotExistException;
import cn.bootx.starter.wecom.core.robot.dao.WecomRobotConfigManager;
import cn.bootx.starter.wecom.core.robot.domin.UploadMedia;
import cn.bootx.starter.wecom.core.robot.entity.WecomRobotConfig;
import cn.bootx.starter.wecom.core.robot.executor.RobotMediaFileUploadRequestExecutor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.cp.api.WxCpGroupRobotService;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.article.NewArticle;
import org.springframework.stereotype.Service;

/**
 * 企微机器人消息通知
 *
 * @author xxm
 * @since 2022/7/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeComRobotNoticeService {
    private final WxCpService wxCpService;
    private final WecomRobotConfigManager robotConfigManager;

    /** 发送文本消息 */
    @SneakyThrows
    public void sendTextNotice(String code, String content, List<String> mentionedList, List<String> mobileList) {
        WecomRobotConfig robotConfig =
                robotConfigManager.findByCode(code).orElseThrow(() -> new DataNotExistException("企业微信机器人配置未找到"));
        WxCpGroupRobotService robotService = wxCpService.getGroupRobotService();
        robotService.sendText(robotConfig.toWebhookUrl(), content, mentionedList, mobileList);
    }

    /** 发送markdown消息 */
    @SneakyThrows
    public void sendMarkdownNotice(String code, String content) {
        WecomRobotConfig robotConfig =
                robotConfigManager.findByCode(code).orElseThrow(() -> new DataNotExistException("企业微信机器人配置未找到"));
        WxCpGroupRobotService robotService = wxCpService.getGroupRobotService();
        robotService.sendMarkdown(robotConfig.toWebhookUrl(), content);
    }

    /** 发送图片消息 */
    @SneakyThrows
    public void sendImageNotice(String code, String imageBase64, String md5) {
        WecomRobotConfig robotConfig =
                robotConfigManager.findByCode(code).orElseThrow(() -> new DataNotExistException("企业微信机器人配置未找到"));
        WxCpGroupRobotService robotService = wxCpService.getGroupRobotService();
        robotService.sendImage(robotConfig.toWebhookUrl(), imageBase64, md5);
    }

    /** 发送图文消息 */
    @SneakyThrows
    public void sendNewsNotice(String code, List<NewArticle> articleList) {
        WecomRobotConfig robotConfig =
                robotConfigManager.findByCode(code).orElseThrow(() -> new DataNotExistException("企业微信机器人配置未找到"));
        WxCpGroupRobotService robotService = wxCpService.getGroupRobotService();
        robotService.sendNews(robotConfig.toWebhookUrl(), articleList);
    }

    /** 发送文件消息 */
    @SneakyThrows
    public void sendFIleNotice(String code, String mediaId) {
        WecomRobotConfig robotConfig =
                robotConfigManager.findByCode(code).orElseThrow(() -> new DataNotExistException("企业微信机器人配置未找到"));
        WxCpGroupRobotService robotService = wxCpService.getGroupRobotService();
        robotService.sendFile(robotConfig.toWebhookUrl(), mediaId);
    }

    /** 机器人临时文件上传 */
    @SneakyThrows
    public String updatedMedia(String code, InputStream inputStream) {
        byte[] bytes = IoUtil.readBytes(inputStream);
        String fileType = FileTypeUtil.getType(new ByteArrayInputStream(bytes));
        UploadMedia uploadMedia = new UploadMedia()
                .setFileType(fileType)
                .setFilename(IdUtil.getSnowflakeNextIdStr())
                .setInputStream(new ByteArrayInputStream(bytes));
        WecomRobotConfig robotConfig =
                robotConfigManager.findByCode(code).orElseThrow(() -> new DataNotExistException("企业微信机器人配置未找到"));
        String url = StrUtil.format(ROBOT_UPLOAD_URL, robotConfig.getWebhookKey());
        WxMediaUploadResult result = wxCpService.execute(new RobotMediaFileUploadRequestExecutor(), url, uploadMedia);
        return result.getMediaId();
    }

    /** 机器人临时文件上传 */
    @SneakyThrows
    public String updatedMedia(String code, InputStream inputStream, String filename) {
        byte[] bytes = IoUtil.readBytes(inputStream);
        String fileType = FileTypeUtil.getType(new ByteArrayInputStream(bytes), filename);
        UploadMedia uploadMedia = new UploadMedia()
                .setFileType(fileType)
                .setFilename(FileNameUtil.mainName(filename))
                .setInputStream(new ByteArrayInputStream(bytes));
        WecomRobotConfig robotConfig =
                robotConfigManager.findByCode(code).orElseThrow(() -> new DataNotExistException("企业微信机器人配置未找到"));
        String url = StrUtil.format(ROBOT_UPLOAD_URL, robotConfig.getWebhookKey());
        WxMediaUploadResult result = wxCpService.execute(new RobotMediaFileUploadRequestExecutor(), url, uploadMedia);
        return result.getMediaId();
    }
}
