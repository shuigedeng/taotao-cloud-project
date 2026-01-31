package com.taotao.cloud.sys.biz.supports.slicefile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.taotao.boot.common.utils.log.LogUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * UploadController
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@RestController
@RequestMapping("/file/slice")
@Slf4j
public class UploadController {

    /**
     * 文件临时路径
     */
    private static final String FILE_UPLOAD_TEMP_DIR = "temp";
    /**
     * 文件保存路径
     */
    private static final String FILE_UPLOAD_DIR = "file";
    /**
     * 临时文件扩展名
     */
    private static final String FILE_TEMP_EXT = ".tem";

    //@ApiOperation("大文件分片上传接口")
    @RequestMapping(value = "/upload/slice", method = RequestMethod.POST)
    @ResponseBody
    public String uploadSlice( HttpServletRequest req, HttpServletResponse resp ) {
        // 允许跨域，测试用
        resp.addHeader("Access-Control-Allow-Origin", "*");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) req;
        // 读取数据
        MultipartFile file = multipartRequest.getFile("data");
        // 当前分片，关键是它的序号，后边合并时需要
        int index = Integer.parseInt(multipartRequest.getParameter("index"));
        int total = Integer.parseInt(multipartRequest.getParameter("total"));
        // 分片文件的唯一标识
        String uuid = multipartRequest.getParameter("uuid");
        try {
            File uploadFile = new File(FILE_UPLOAD_TEMP_DIR + "/" + uuid, index + FILE_TEMP_EXT);
            if (!uploadFile.getParentFile().exists()) {
                uploadFile.getParentFile().mkdirs();
            }
            // 分片数量不对应时，跳过
            if (index <= total) {
                file.transferTo(uploadFile.getAbsoluteFile());
                return "200";
            } else {
                return "500";
            }
        } catch (Exception ex) {
            log.error("异常", ex);
            LogUtils.info(ex.getMessage());
            return "500";
        }
    }

    //@ApiOperation("上传完成，服务端合并文件")
    @RequestMapping(value = "/merge", method = RequestMethod.GET)
    public String merge( HttpServletRequest req, HttpServletResponse resp ) {
        // 允许跨域，测试用
        resp.addHeader("Access-Control-Allow-Origin", "*");
        try {
            String uuid = req.getParameter("uuid");
            String newFileName = req.getParameter("newFileName");
            File dirFile = new File(FILE_UPLOAD_TEMP_DIR + "/" + uuid);
            if (!dirFile.exists()) {
                return "500";
            }
            // 利用排序算法，使得分片顺序排列合并
            String[] fileNames = dirFile.list();
            Arrays.sort(fileNames, ( o1, o2 ) -> {
                int i1 = Integer.parseInt(o1.replace(FILE_TEMP_EXT, ""));
                int i2 = Integer.parseInt(o2.replace(FILE_TEMP_EXT, ""));
                return i1 - i2;
            });
            File targetFile = new File(FILE_UPLOAD_DIR, newFileName);
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            RandomAccessFile writeFile = new RandomAccessFile(targetFile, "rw");
            long position = 0;
            for (String fileName : fileNames) {
                File sourceFile = new File(FILE_UPLOAD_TEMP_DIR + "/" + uuid, fileName);
                RandomAccessFile readFile = new RandomAccessFile(sourceFile, "rw");
                int chunksize = 1024 * 3;
                byte[] buf = new byte[chunksize];
                writeFile.seek(position);
                int byteCount;
                while (( byteCount = readFile.read(buf) ) != -1) {
                    if (byteCount != chunksize) {
                        byte[] tempBytes = new byte[byteCount];
                        System.arraycopy(buf, 0, tempBytes, 0, byteCount);
                        buf = tempBytes;
                    }
                    writeFile.write(buf);
                    position = position + byteCount;
                }
                readFile.close();
            }
            writeFile.close();
            // 删除临时文件夹
            FileUtils.deleteQuietly(new File(FILE_UPLOAD_TEMP_DIR + "/" + uuid));
            return "200";
        } catch (IOException ex) {
            log.error("异常", ex);
            return "500";
        }
    }
}
