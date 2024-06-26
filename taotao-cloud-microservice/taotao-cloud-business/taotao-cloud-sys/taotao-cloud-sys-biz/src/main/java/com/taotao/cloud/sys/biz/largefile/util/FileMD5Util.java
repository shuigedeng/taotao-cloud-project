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

package com.taotao.cloud.sys.biz.largefile.util;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.sys.biz.file.src.main.java.com.taotao.cloud.file.biz.largefile.util.FileUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

/** 计算文件MD5工具类 */
public class FileMD5Util {

    private static final Logger logger = LoggerFactory.getLogger(FileMD5Util.class);

    public static String getFileMD5(File file) throws FileNotFoundException {

        String value = null;
        FileInputStream in = new FileInputStream(file);
        MappedByteBuffer byteBuffer = null;
        try {
            byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
            if (value.length() < 32) {
                value = "0" + value;
            }
        } catch (Exception e) {
            LogUtils.error(e);
        } finally {
            FileUtil.close(in, byteBuffer);
        }
        return value;
    }

    public static String getFileMD5(MultipartFile file) {

        try {
            byte[] uploadBytes = file.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(uploadBytes);
            String hashString = new BigInteger(1, digest).toString(16);
            return hashString;
        } catch (IOException e) {
            logger.error("get file md5 error!!!", e);
        } catch (NoSuchAlgorithmException e) {
            logger.error("get file md5 error!!!", e);
        }
        return null;
    }

    public static void main(String[] args) throws Exception {

        long start = System.currentTimeMillis();
        String filePath = "F:\\desktop\\uploads\\461ad6106d8253b94bd00546a4a1a8e4\\pycharm-professional-2019.1.3.exe";
        File file = new File(filePath);
        String md5 = FileMD5Util.getFileMD5(file);
        long end = System.currentTimeMillis();
        LogUtils.info("cost:" + (end - start) + "ms, md5:" + md5);
    }
}
