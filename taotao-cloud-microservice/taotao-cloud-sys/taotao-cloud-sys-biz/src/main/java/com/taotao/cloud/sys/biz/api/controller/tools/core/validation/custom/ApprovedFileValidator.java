package com.taotao.cloud.sys.biz.api.controller.tools.core.validation.custom;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ApprovedFileValidator implements ConstraintValidator<ApprovedFile, MultipartFile> {
    private List<MimeType> allowFileTypes = new ArrayList<>();
    private List<MimeType> denyFileTypes = new ArrayList<>();
    private long maxFileSize;
    private boolean checkMagic;

    @Override
    public void initialize(ApprovedFile approvedFile) {
        String[] allowFileTypes = approvedFile.allowFileTypes();
        String[] denyFileTypes = approvedFile.denyFileTypes();
        for (String allowFileType : allowFileTypes) {
            this.allowFileTypes.add(MimeTypeUtils.parseMimeType(allowFileType));
        }
        for (String denyFileType : denyFileTypes) {
            this.denyFileTypes.add(MimeTypeUtils.parseMimeType(denyFileType));
        }
        this.checkMagic = approvedFile.checkMagic();
        maxFileSize = approvedFile.maxFileSize();
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
        if(multipartFile == null) {
            return true;
        }
        // 先判断文件大小，和后缀，然后读字节来判断文件类型
        long size = multipartFile.getSize();
        if(size > maxFileSize && maxFileSize != -1){
//            throw SystemMessage.FILE_TOO_LARGE.exception(size,maxFileSize);
            return false;
        }

        //验证文件类型,使用 contentType
        String contentType = multipartFile.getContentType();
        MimeType fileMimeType = MimeTypeUtils.parseMimeType(contentType);
        // 允许的文件
        if(!CollectionUtils.isEmpty(allowFileTypes)) {
            for (MimeType allowFileType : allowFileTypes) {
                if (allowFileType.isCompatibleWith(fileMimeType)) {
                    //再次检查魔术数字是否满足
                    if(checkMagic && !checkMagicNumber(fileMimeType,multipartFile)){continue;};
                    return true;
                }
            }
        }

        //不允许的文件
        if(!CollectionUtils.isEmpty(denyFileTypes)) {
            for (MimeType denyFileType : denyFileTypes) {
                if (denyFileType.isCompatibleWith(fileMimeType)) {
                    return false;
                }
            }
        }

        //再次检查魔术数字是否满足
        if(checkMagic && !checkMagicNumber(fileMimeType,multipartFile) ){return false;};
        return true;
    }

    public static final Map<MimeType,String> mimeTypeMagic = new ConcurrentHashMap<>();
    static {
        mimeTypeMagic.put(MimeTypeUtils.parseMimeType("image/png"),"89504e47");
        mimeTypeMagic.put(MimeTypeUtils.parseMimeType("image/vnd.dwg"),"41433130313500000000");
        mimeTypeMagic.put(MimeTypeUtils.parseMimeType("image/tif"),"49492a00227105008037");
        mimeTypeMagic.put(MimeTypeUtils.parseMimeType("image/bmp"),"424d8e1b030000000000");
        mimeTypeMagic.put(MimeTypeUtils.parseMimeType("image/gif"),"4749463839");
        mimeTypeMagic.put(MimeTypeUtils.parseMimeType("image/bmp"),"424d228c010000000000");
        mimeTypeMagic.put(MimeTypeUtils.parseMimeType("image/bmp"),"424d8240090000000000");
        mimeTypeMagic.put(MimeTypeUtils.parseMimeType("image/jpg"),"ffd8ff");
        mimeTypeMagic.put(MimeTypeUtils.parseMimeType("image/gif"),"4749463837");
    }

    /**
     * 判断是否真的是图片文件
     * @param fileMimeType
     * @param multipartFile
     * @return
     */
    private boolean checkMagicNumber(MimeType fileMimeType, MultipartFile multipartFile) {
        try {
            InputStream inputStream = multipartFile.getInputStream();
            Iterator<Map.Entry<MimeType, String>> iterator = mimeTypeMagic.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<MimeType, String> next = iterator.next();
                MimeType key = next.getKey();
                String value = next.getValue();

                if(key.isCompatibleWith(fileMimeType)){
                    byte [] buffer28 = new byte[28];
                    try {
                       inputStream.read(buffer28);
                       char[] chars = Hex.encodeHex(buffer28);
                        boolean startsWith = new String(chars).startsWith(value);
                        if(!startsWith){
                            String fileName = multipartFile.getOriginalFilename();
                            log.error("魔术数字检查失败，当前文件 {} 的魔术数字 {} 不是目标类型 {} ",fileName,new String(chars),key);
                        }
                        return startsWith;
                    }catch (IOException e){ log.error("读前 28 位流出错:"+e.getMessage(),e);}
                    finally {IOUtils.closeQuietly(inputStream);}
                }
            }

            //可能当前文件类型不受解析支持，但有可能是正确的
            log.warn("可能当前文件类型[{}]不受解析支持，但有可能是正确的",fileMimeType);
            return true;
        } catch (IOException e) {
            log.error("解析文件类型时读流出错:"+e.getMessage(),e);
        }

        return false;
    }

}
