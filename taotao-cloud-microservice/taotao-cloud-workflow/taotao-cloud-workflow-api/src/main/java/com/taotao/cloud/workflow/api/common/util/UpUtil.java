package com.taotao.cloud.workflow.api.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

public class UpUtil {

    /**
     * 获取上传文件
     */
    public static List<MultipartFile> getFileAll(){
        MultipartResolver resolver = new StandardServletMultipartResolver();
        MultipartHttpServletRequest mRequest = resolver.resolveMultipart(ServletUtil.getRequest());
        Map<String, MultipartFile> fileMap = mRequest.getFileMap();
        List<MultipartFile> list = new ArrayList<>();
        for(Map.Entry<String,MultipartFile> map : fileMap.entrySet()){
            list.add(map.getValue());
        }
        return list;
    }

    /**
     * 获取文件类型
     */
    public static String getFileType(MultipartFile multipartFile){
        if (multipartFile.getContentType()!=null) {
            String[] split = multipartFile.getOriginalFilename().split("\\.");
            if (split.length>1) {
                return split[split.length-1];
            }
        }
        return "";
    }

}
