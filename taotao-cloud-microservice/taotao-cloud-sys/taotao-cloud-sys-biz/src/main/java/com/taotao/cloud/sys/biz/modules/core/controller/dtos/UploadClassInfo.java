package com.taotao.cloud.sys.biz.modules.core.controller.dtos;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 上传多文件到类加载器, 并附带一个默认的 pom
 */
@Data
public class UploadClassInfo {
    private String classloaderName;
    @JSONField(serialize = false)
    private MultipartFile [] files;
    private String pomContent;
    private String settings;
}
