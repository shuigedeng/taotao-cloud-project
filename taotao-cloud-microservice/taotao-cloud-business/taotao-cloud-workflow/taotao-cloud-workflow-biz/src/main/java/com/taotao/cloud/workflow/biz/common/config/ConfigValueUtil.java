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

package com.taotao.cloud.workflow.biz.common.config;

import java.io.File;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** */
@Data
@Component
@ConfigurationProperties(prefix = "config")
public class ConfigValueUtil {

    /** 环境路径 */
    @Value("${config.Path}")
    private String path;
    /** 数据库备份文件路径 */
    private String dataBackupFilePath;
    /** 临时文件存储路径 */
    private String temporaryFilePath;
    /** 系统文件存储路径 */
    private String systemFilePath;
    /** 文件模板存储路径 */
    private String templateFilePath;
    /** 代码模板存储路径 */
    private String templateCodePath;
    /** 邮件文件存储路径 */
    private String emailFilePath;
    /** 大屏图片存储目录 */
    private String biVisualPath;
    /** 文档管理存储路径 */
    private String documentFilePath;
    /** 文件在线预览存储pdf */
    private String documentPreviewPath;
    /** 用户头像存储路径 */
    private String userAvatarFilePath;
    /** IM聊天图片+语音存储路径 */
    private String imContentFilePath;
    /** 允许上传文件类型 */
    @Value("${config.AllowUploadFileType}")
    private String allowUploadFileType;
    /** 允许图片类型 */
    @Value("${config.AllowUploadImageType}")
    private String allowUploadImageType;

    /** 允许预览类型 */
    @Value("${config.AllowPreviewFileType}")
    private String allowPreviewFileType;

    /** 预览方式 */
    @Value("${config.PreviewType}")
    private String previewType;

    /** 预览方式 */
    @Value("${config.kkFileUrl}")
    private String kkFileUrl;

    /** 前端文件目录 */
    private String serviceDirectoryPath;
    /** 代码生成器命名空间 */
    @Value("${config.CodeAreasName}")
    private String codeAreasName;

    /** 前端附件文件目录 */
    private String webAnnexFilePath;

    /** 是否开启接口鉴权 */
    @Value("${config.EnablePreAuth:false}")
    private Boolean enablePreAuth;

    public String getServiceDirectoryPath() {
        String folder =
                StringUtil.isNotEmpty(serviceDirectoryPath) ? serviceDirectoryPath : ConfigConst.CODE_TEMP_FOLDER;
        return getXssPath(path + folder + File.separator);
    }

    public String getDataBackupFilePath() {
        String folder = StringUtil.isNotEmpty(dataBackupFilePath) ? dataBackupFilePath : ConfigConst.DATA_BACKUP_FOLDER;
        return getXssPath(path + folder + File.separator);
    }

    public String getTemporaryFilePath() {
        String folder = StringUtil.isNotEmpty(temporaryFilePath) ? temporaryFilePath : ConfigConst.TEMPORARY_FOLDER;
        return getXssPath(path + folder + File.separator);
    }

    public String getSystemFilePath() {
        String folder = StringUtil.isNotEmpty(systemFilePath) ? systemFilePath : ConfigConst.SYSTEM_FOLDER;
        return getXssPath(path + folder + File.separator);
    }

    public String getTemplateFilePath() {
        String folder = StringUtil.isNotEmpty(templateFilePath) ? templateFilePath : ConfigConst.TEMPLATE_FOLDER;
        return getXssPath(path + folder + File.separator);
    }

    public String getTemplateCodePath() {
        String folder = StringUtil.isNotEmpty(templateCodePath) ? templateCodePath : ConfigConst.TEMPLATE_CODE_FOLDER;
        return getXssPath(path + folder + File.separator);
    }

    public String getEmailFilePath() {
        String folder = StringUtil.isNotEmpty(emailFilePath) ? emailFilePath : ConfigConst.EMAIL_FOLDER;
        return getXssPath(path + folder + File.separator);
    }

    public String getDocumentPreviewPath() {
        String folder =
                StringUtil.isNotEmpty(documentPreviewPath) ? documentPreviewPath : ConfigConst.DOCUMENT_PREVIEW_FOLDER;
        return getXssPath(path + folder + File.separator);
    }

    public String getUserAvatarFilePath() {
        String folder = StringUtil.isNotEmpty(userAvatarFilePath) ? userAvatarFilePath : ConfigConst.USER_AVATAR_FOLDER;
        return getXssPath(path + folder + File.separator);
    }

    public String getImContentFilePath() {
        String folder = StringUtil.isNotEmpty(imContentFilePath) ? imContentFilePath : ConfigConst.IM_CONTENT_FOLDER;
        return getXssPath(path + folder + File.separator);
    }

    public String getDocumentFilePath() {
        String folder = StringUtil.isNotEmpty(documentFilePath) ? documentFilePath : ConfigConst.DOCUMENT_FOLDER;
        return getXssPath(path + folder + File.separator);
    }

    public String getWebAnnexFilePath() {
        String folder = StringUtil.isNotEmpty(webAnnexFilePath) ? webAnnexFilePath : ConfigConst.WEB_ANNEX_FOLDER;
        return getXssPath(path + folder + File.separator);
    }

    public String getBiVisualPath() {
        String folder = StringUtil.isNotEmpty(biVisualPath) ? biVisualPath : ConfigConst.BI_VISUAL_FOLDER;
        return getXssPath(path + folder + File.separator);
    }

    private String getXssPath(String path) {
        String xssPath = XSSEscape.escapePath(path);
        return xssPath;
    }

    /** 软件的错误报告 */
    @Value("${config.ErrorReport}")
    private String errorReport;
    /** 软件的错误报告发给谁 */
    @Value("${config.ErrorReportTo}")
    private String errorReportTo;
    /** 系统日志启用：true、false */
    @Value("${config.RecordLog}")
    private String recordLog;
    /** 多租户启用：true、false */
    @Value("${config.MultiTenancy}")
    private String multiTenancy;
    /** 版本 */
    @Value("${config.SoftVersion}")
    private String softVersion;
    /** 推送是否启动：false、true */
    @Value("${config.IgexinEnabled}")
    private String igexinEnabled;
    /** APPID */
    @Value("${config.IgexinAppid}")
    private String igexinAppid;
    /** APPKEY */
    @Value("${config.IgexinAppkey}")
    private String igexinAppkey;
    /** MASTERSECRET */
    @Value("${config.IgexinMastersecret}")
    private String igexinMastersecret;

    @Value("${config.AppUpdateContent}")
    private String appUpdateContent;

    @Value("${config.AppVersion}")
    private String appVersion;

    /** -------------租户库配置----------- */

    /** -------------跨域配置----------- */
    //    @Value("${config.Origins}")
    //    private String origins;
    //    @Value("${config.Methods}")
    //    private String methods;

    /** -------------是否开启测试环境，admin账户可以无限登陆，并且无法修改密码----------- */
    @Value("${config.TestVersion}")
    private String testVersion;

    /** 当前存储类型 */
    @Value("${config.fileType}")
    private String fileType;
}
