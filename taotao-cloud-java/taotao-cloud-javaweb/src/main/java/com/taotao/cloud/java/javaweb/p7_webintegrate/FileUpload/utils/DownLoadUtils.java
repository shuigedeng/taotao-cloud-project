package com.taotao.cloud.java.javaweb.p7_webintegrate.FileUpload.utils;

import java.io.File;
import java.util.HashMap;

public class DownLoadUtils {
    public static void getFileList(File file, HashMap<String,String> fileMap){
        File[] files = file.listFiles();

        for(File f : files){
            if(f.isDirectory()){
                getFileList(f,fileMap);
            }else{
                String filename = f.getName();
                int index = filename.indexOf("_");
                String realName = filename.substring(index + 1);
                fileMap.put(filename,realName);
            }
        }
    }
}
