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

package com.taotao.cloud.workflow.biz.common.util.file;

import com.taotao.cloud.workflow.biz.common.util.context.SpringContext;
import io.minio.messages.Item;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.taotao.boot.common.utils.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/** 上传文件工具类 */
public class UploadUtil {

    private static MinioUploadUtil minioUploadUtil = SpringContext.getBean(MinioUploadUtil.class);

    /**
     * 上传文件
     *
     * @param type 文件储存类型
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @param multipartFile 文件
     * @param multipartFile 本地存储时的文件路径
     */
    public static void uploadFile(
            String type, String bucketName, String objectName, MultipartFile multipartFile, String filePath) {
        switch (type) {
                // Minio存储
            case StorageType.MINIO:
                minioUploadUtil.uploadFile(multipartFile, bucketName.toLowerCase(), objectName);
                break;
                // 本地存储
            default:
                // 上传文件
                FileUtil.upFile(multipartFile, filePath, objectName);
                break;
        }
    }

    /**
     * 上传文件
     *
     * @param type 文件储存类型
     * @param filePath 文件路径
     * @param bucketName 存储空间
     * @param fileName 文件名
     */
    public static void uploadFile(String type, String filePath, String bucketName, String fileName) throws IOException {
        switch (type) {
                // Minio存储
            case StorageType.MINIO:
                minioUploadUtil.uploadFiles(filePath, bucketName.toLowerCase(), fileName);
                break;
                // 本地存储
            default:
                break;
        }
    }

    /**
     * 删除一个对象
     *
     * @param type 文件储存类型
     * @param bucketName 存储空间
     * @param fileName 文件名
     */
    public static void removeFile(String type, String bucketName, String fileName) {
        switch (type) {
                // Minio存储
            case StorageType.MINIO:
                minioUploadUtil.removeFile(bucketName.toLowerCase(), fileName);
                break;
                // 本地存储
            default:
                break;
        }
    }

    /**
     * 下载文件
     *
     * @param type 文件存储类型
     * @param fileName 文件名
     * @param bucketName 存储空间
     * @param filePath 文件路径
     */
    public static void downFile(String type, String fileName, String bucketName, String filePath, String downName) {
        switch (type) {
                // Minio存储
            case StorageType.MINIO:
                minioUploadUtil.downFile(fileName, bucketName.toLowerCase(), downName);
                break;
                // 本地存储
            default:
                if (StringUtil.isNotEmpty(filePath)) {
                    DownUtil.dowloadFile(filePath + fileName, fileName, downName);
                }
                break;
        }
    }

    /**
     * 直接在页面上显示图片
     *
     * @param type 文件存储类型
     * @param fileName 文件名
     * @param bucketName 存储空间
     * @param filePath 文件路径
     */
    public static void writeImage(String type, String fileName, String bucketName, String filePath) {
        switch (type) {
                // Minio存储
            case StorageType.MINIO:
                minioUploadUtil.dowloadMinioFile(fileName, bucketName.toLowerCase());
                break;
                // 本地存储
            default:
                if (StringUtil.isNotEmpty(filePath)) {
                    DownUtil.dowloadFile(filePath + fileName);
                }
                break;
        }
    }

    /**
     * 通过流下载文件
     *
     * @param type 文件存储类型
     * @param fileName 文件名
     * @param bucketName 存储空间
     * @param filePath 文件路径
     */
    public static void streamToDown(String type, String fileName, String bucketName, String filePath) {
        switch (type) {
                // Minio存储
            case StorageType.MINIO:
                minioUploadUtil.streamToDown(bucketName.toLowerCase(), filePath, fileName);
                break;
                // 本地存储
            default:
                break;
        }
    }

    /**
     * 下载代码
     *
     * @param type 文件存储类型
     * @param fileName 文件名
     * @param bucketName 存储空间
     * @param filePath 文件路径
     */
    public static boolean downCode(String type, String fileName, String bucketName, String filePath, String zipFile) {
        boolean exists = true;
        switch (type) {
                // Minio存储
            case StorageType.MINIO:
                minioUploadUtil.downFile(fileName, bucketName.toLowerCase(), null);
                break;
                // 本地存储
            default:
                if (FileUtil.fileIsExists(filePath)) {
                    // 调用压缩方法
                    FileUtil.toZip(zipFile, true, filePath);
                    DownUtil.dowloadFile(zipFile, fileName, null);
                } else {
                    exists = false;
                }
                break;
        }
        return exists;
    }

    /**
     * 上传文件夹
     *
     * @param type 文件存储类型
     * @param folderName 文件夹名称
     * @param bucketName 存储空间
     * @param filePath 文件夹路径
     */
    public static void uploadFolder(String type, String folderName, String bucketName, String filePath) {
        switch (type) {
                // Minio存储
            case StorageType.MINIO:
                // 读取路径下的文件
                minioUploadUtil.putFolder(filePath + folderName, bucketName, folderName);
                break;
                // 本地存储
            default:
                break;
        }
    }

    /**
     * 获取所有文件列表
     *
     * @param type
     * @param bucketName
     * @param path
     * @param keyWord
     * @return
     */
    public static List getFileList(String type, String bucketName, String path, String keyWord, boolean flag) {
        List<FileModel> data = new ArrayList<>();
        switch (type) {
                // Minio存储
            case StorageType.MINIO:
                // 读取路径下的文件
                List fileList = minioUploadUtil.getFileList(bucketName);
                if (fileList != null) {
                    for (int i = 0; i < fileList.size(); i++) {
                        FileModel fileModel = new FileModel();
                        fileModel.setFileId(i + "");
                        Item item = (Item) fileList.get(i);
                        String objectName = item.objectName();
                        fileModel.setFileName(objectName);
                        fileModel.setFileType(FileUtil.getFileType(objectName));
                        fileModel.setFileSize(FileUtil.getSize(String.valueOf(item.size())));
                        fileModel.setFileTime(
                                item.lastModified() != null
                                        ? DateUtil.getZonedDateTimeToString(item.lastModified())
                                        : "");
                        data.add(fileModel);
                    }
                    if (flag) {
                        data = getFileModels(data);
                    }
                    if (keyWord != null && !StringUtils.isEmpty(keyWord)) {
                        data = data.stream()
                                .filter(t -> t.getFileName().contains(keyWord))
                                .toList();
                    }
                }
                break;
                // 本地存储
            default:
                File filePath = new File(XSSEscape.escapePath(path));
                List<File> files = FileUtil.getFile(filePath);
                if (files != null) {
                    for (int i = 0; i < files.size(); i++) {
                        File item = files.get(i);
                        FileModel fileModel = new FileModel();
                        fileModel.setFileId(i + "");
                        fileModel.setFileName(item.getName());
                        fileModel.setFileType(FileUtil.getFileType(item));
                        fileModel.setFileSize(FileUtil.getSize(String.valueOf(item.length())));
                        fileModel.setFileTime(FileUtil.getCreateTime(path + item.getName()));
                        data.add(fileModel);
                    }
                    data = getFileModels(data);
                    if (keyWord != null && !StringUtils.isEmpty(keyWord)) {
                        data = data.stream()
                                .filter(t -> t.getFileName().contains(keyWord))
                                .toList();
                    }
                }
                break;
        }
        return data;
    }

    /**
     * 过滤文件
     *
     * @param data
     * @return
     */
    private static List<FileModel> getFileModels(List<FileModel> data) {
        data = data.stream()
                .filter(m -> "xlsx".equals(m.getFileType())
                        || "xls".equals(m.getFileType())
                        || "docx".equals(m.getFileType())
                        || "doc".equals(m.getFileType())
                        || "pptx".equals(m.getFileType())
                        || "ppt".equals(m.getFileType())
                        || "pdf".equals(m.getFileType()))
                .toList();
        return data;
    }

    /**
     * 专属大屏方法
     *
     * @param fileType
     * @param bucketName
     * @param path
     * @param type
     * @return
     */
    public static List getFileList(String fileType, String bucketName, String path, String type) {
        String newPath = XSSEscape.escape(path);
        String newType = XSSEscape.escape(type);
        List<ImageVO> vo = new ArrayList<>();
        switch (fileType) {
                // Minio存储
            case StorageType.MINIO:
                // 读取路径下的文件
                List fileList = minioUploadUtil.getFileList(bucketName, newType);
                for (int i = 0; i < fileList.size(); i++) {
                    ImageVO imageVO = new ImageVO();
                    Item item = (Item) fileList.get(i);
                    String[] objectName = item.objectName().split("/");
                    String fileName = objectName[1];
                    // 读取路径下的文件
                    imageVO.setLink(VisusalImgUrl.url + newType + "/" + fileName);
                    imageVO.setOriginalName(fileName);
                    vo.add(imageVO);
                }
                break;
                // 本地存储
            default:
                List<File> fileLists = FileUtil.getFile(new File(newPath));
                for (File file : fileLists) {
                    ImageVO imageVO = new ImageVO();
                    String fileName = file.getName();
                    imageVO.setLink(VisusalImgUrl.url + newType + "/" + fileName);
                    imageVO.setOriginalName(fileName);
                    vo.add(imageVO);
                }
                break;
        }
        return vo;
    }

    /**
     * 拷贝文件(邮件使用)
     *
     * @param type
     * @param filePath
     * @param fileName
     * @param copyToFilePath
     * @param copyToFileName
     */
    public static void copyObject(
            String type, String filePath, String fileName, String copyToFilePath, String copyToFileName) {
        switch (type) {
                // Minio存储
            case StorageType.MINIO:
                minioUploadUtil.copyObject(FileTypeEnum.TEMPORARY, fileName, FileTypeEnum.MAIL, copyToFileName);
                break;
                // 本地存储
            default:
                FileUtil.copyFile(filePath + fileName, copyToFilePath + copyToFileName);
                break;
        }
    }

    /**
     * 通过流下载文件到本地
     *
     * @param type 文件存储类型
     * @param fileName 文件名
     * @param bucketName 存储空间
     * @param filePath 文件路径
     */
    public static void downToLocal(String type, String fileName, String bucketName, String filePath) {
        switch (type) {
                // Minio存储
            case StorageType.MINIO:
                minioUploadUtil.downToLocal(bucketName.toLowerCase(), filePath, fileName);
                break;
                // 本地存储
            default:
                break;
        }
    }
}
