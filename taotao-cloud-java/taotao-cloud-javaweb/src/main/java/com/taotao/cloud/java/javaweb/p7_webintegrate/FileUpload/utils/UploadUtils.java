package com.taotao.cloud.java.javaweb.p7_webintegrate.FileUpload.utils;

import java.io.File;
import java.util.UUID;

public class UploadUtils {

    public static String newFileName(String filename){
        return UUID.randomUUID().toString().replaceAll("-","")+"_"+filename;
    }
    //生成二级、三级目录
    public static String newFilePath(String basepath,String filename){
        int hashCode = filename.hashCode();
        int path1 = hashCode & 15;//二级目录  与运算 0~15
        int path2 = (hashCode>>4) & 15;//三级目录  0~15
        String newPath = basepath+"\\"+path1+"\\"+path2;
        File file = new File(newPath);
        if(!file.exists()){
            file.mkdirs();
        }
        return newPath;
    }

}
