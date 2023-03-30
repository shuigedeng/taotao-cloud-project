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

package com.taotao.cloud.media.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/11/12 17:10
 */
@Schema(name = "FileVO", description = "文件VO")
public class FileVO implements Serializable {

    private static final long serialVersionUID = 5126530068827085130L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "业务ID")
    private Long bizId;

    @Schema(description = "业务类型")
    private String bizType;

    @Schema(description = "数据类型")
    private String dataType;

    @Schema(description = "原始文件名")
    private String originalFileName;

    @Schema(description = "文件访问链接")
    private String url;

    @Schema(description = "文件md5值")
    private String fileMd5;

    @Schema(description = "文件上传类型")
    private String contextType;

    @Schema(description = "唯一文件名")
    private String filename;

    @Schema(description = "后缀(没有.)")
    private String ext;

    @Schema(description = "大小")
    private Long size;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "最后修改时间")
    private LocalDateTime lastModifiedTime;

    @Override
    public String toString() {
        return "FileVO{"
                + "id="
                + id
                + ", bizId="
                + bizId
                + ", bizType='"
                + bizType
                + '\''
                + ", dataType='"
                + dataType
                + '\''
                + ", originalFileName='"
                + originalFileName
                + '\''
                + ", url='"
                + url
                + '\''
                + ", fileMd5='"
                + fileMd5
                + '\''
                + ", contextType='"
                + contextType
                + '\''
                + ", filename='"
                + filename
                + '\''
                + ", ext='"
                + ext
                + '\''
                + ", size="
                + size
                + ", createTime="
                + createTime
                + ", lastModifiedTime="
                + lastModifiedTime
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FileVO fileVO = (FileVO) o;
        return Objects.equals(id, fileVO.id)
                && Objects.equals(bizId, fileVO.bizId)
                && Objects.equals(bizType, fileVO.bizType)
                && Objects.equals(dataType, fileVO.dataType)
                && Objects.equals(originalFileName, fileVO.originalFileName)
                && Objects.equals(url, fileVO.url)
                && Objects.equals(fileMd5, fileVO.fileMd5)
                && Objects.equals(contextType, fileVO.contextType)
                && Objects.equals(filename, fileVO.filename)
                && Objects.equals(ext, fileVO.ext)
                && Objects.equals(size, fileVO.size)
                && Objects.equals(createTime, fileVO.createTime)
                && Objects.equals(lastModifiedTime, fileVO.lastModifiedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                bizId,
                bizType,
                dataType,
                originalFileName,
                url,
                fileMd5,
                contextType,
                filename,
                ext,
                size,
                createTime,
                lastModifiedTime);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBizId() {
        return bizId;
    }

    public void setBizId(Long bizId) {
        this.bizId = bizId;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    public String getContextType() {
        return contextType;
    }

    public void setContextType(String contextType) {
        this.contextType = contextType;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(LocalDateTime lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public FileVO() {}

    public FileVO(
            Long id,
            Long bizId,
            String bizType,
            String dataType,
            String originalFileName,
            String url,
            String fileMd5,
            String contextType,
            String filename,
            String ext,
            Long size,
            LocalDateTime createTime,
            LocalDateTime lastModifiedTime) {
        this.id = id;
        this.bizId = bizId;
        this.bizType = bizType;
        this.dataType = dataType;
        this.originalFileName = originalFileName;
        this.url = url;
        this.fileMd5 = fileMd5;
        this.contextType = contextType;
        this.filename = filename;
        this.ext = ext;
        this.size = size;
        this.createTime = createTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    public static FileVOBuilder builder() {
        return new FileVOBuilder();
    }

    public static final class FileVOBuilder {

        private Long id;
        private Long bizId;
        private String bizType;
        private String dataType;
        private String originalFileName;
        private String url;
        private String fileMd5;
        private String contextType;
        private String filename;
        private String ext;
        private Long size;
        private LocalDateTime createTime;
        private LocalDateTime lastModifiedTime;

        private FileVOBuilder() {}

        public static FileVOBuilder aFileVO() {
            return new FileVOBuilder();
        }

        public FileVOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public FileVOBuilder bizId(Long bizId) {
            this.bizId = bizId;
            return this;
        }

        public FileVOBuilder bizType(String bizType) {
            this.bizType = bizType;
            return this;
        }

        public FileVOBuilder dataType(String dataType) {
            this.dataType = dataType;
            return this;
        }

        public FileVOBuilder originalFileName(String originalFileName) {
            this.originalFileName = originalFileName;
            return this;
        }

        public FileVOBuilder url(String url) {
            this.url = url;
            return this;
        }

        public FileVOBuilder fileMd5(String fileMd5) {
            this.fileMd5 = fileMd5;
            return this;
        }

        public FileVOBuilder contextType(String contextType) {
            this.contextType = contextType;
            return this;
        }

        public FileVOBuilder filename(String filename) {
            this.filename = filename;
            return this;
        }

        public FileVOBuilder ext(String ext) {
            this.ext = ext;
            return this;
        }

        public FileVOBuilder size(Long size) {
            this.size = size;
            return this;
        }

        public FileVOBuilder createTime(LocalDateTime createTime) {
            this.createTime = createTime;
            return this;
        }

        public FileVOBuilder lastModifiedTime(LocalDateTime lastModifiedTime) {
            this.lastModifiedTime = lastModifiedTime;
            return this;
        }

        public FileVO build() {
            FileVO fileVO = new FileVO();
            fileVO.setId(id);
            fileVO.setBizId(bizId);
            fileVO.setBizType(bizType);
            fileVO.setDataType(dataType);
            fileVO.setOriginalFileName(originalFileName);
            fileVO.setUrl(url);
            fileVO.setFileMd5(fileMd5);
            fileVO.setContextType(contextType);
            fileVO.setFilename(filename);
            fileVO.setExt(ext);
            fileVO.setSize(size);
            fileVO.setCreateTime(createTime);
            fileVO.setLastModifiedTime(lastModifiedTime);
            return fileVO;
        }
    }
}
