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

package com.taotao.cloud.workflow.biz.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/** */
@Slf4j
public class FileUtil {

    /**
     * 判断文件夹是否存在
     *
     * @param filePath 文件地址
     * @return
     */
    public static boolean fileIsExists(String filePath) {
        File f = new File(XSSEscape.escapePath(filePath));
        if (!f.exists()) {
            return false;
        }
        return true;
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean fileIsFile(String filePath) {
        File f = new File(XSSEscape.escapePath(filePath));
        if (!f.isFile()) {
            return false;
        }
        return true;
    }

    /**
     * 创建文件
     *
     * @param filePath 文件地址
     * @param fileName 文件名
     * @return
     */
    public static boolean createFile(String filePath, String fileName) {
        String strFilePath = XSSEscape.escapePath(filePath + fileName);
        ;
        File file = new File(XSSEscape.escapePath(filePath));
        if (!file.exists()) {
            /** 注意这里是 mkdirs()方法 可以创建多个文件夹 */
            file.mkdirs();
        }
        File subfile = new File(strFilePath);
        if (!subfile.exists()) {
            try {
                boolean b = subfile.createNewFile();
                return b;
            } catch (IOException e) {
                LogUtils.error(e);
            }
        } else {
            return true;
        }
        return false;
    }

    /**
     * 创建文件夹
     *
     * @param filePath 文件夹地址
     * @return
     */
    public static void createDirs(String filePath) {
        File file = new File(XSSEscape.escapePath(filePath));
        if (!file.exists()) {
            /** 注意这里是 mkdirs()方法 可以创建多个文件夹 */
            file.mkdirs();
        }
    }

    /**
     * 遍历文件夹下当前文件
     *
     * @param file 地址
     */
    public static List<File> getFile(File file) {
        List<File> list = new ArrayList<>();
        File[] fileArray = file.listFiles();
        if (fileArray == null) {
            return list;
        } else {
            for (File f : fileArray) {
                if (f.isFile()) {
                    list.add(0, f);
                }
            }
        }
        return list;
    }

    /**
     * 遍历文件夹下所有文件
     *
     * @param file 地址
     */
    public static List<File> getFile(File file, List<File> list) {
        File[] fileArray = file.listFiles();
        if (fileArray == null) {
            return list;
        } else {
            for (File f : fileArray) {
                if (f.isFile()) {
                    list.add(0, f);
                } else {
                    getFile(f, list);
                }
            }
        }
        return list;
    }

    /**
     * 删除文件或文件夹以及子文件夹和子文件等 【注意】请谨慎调用该方法，避免删除重要文件
     *
     * @param file
     */
    public static void deleteFileAll(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                // 文件
                log.info(file.getAbsolutePath() + " 删除中...");
                file.delete();
                log.info("删除成功！");
                return;
            } else {
                // 文件夹
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFileAll(files[i]);
                }
                file.delete();
            }
        } else {
            log.info(file.getAbsolutePath() + " 文件不存在！");
        }
    }

    /**
     * 删除单个文件
     *
     * @param filePath 文件路径
     */
    public static void deleteFile(String filePath) {
        File file = new File(XSSEscape.escapePath(filePath));
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }

    /**
     * 删除空文件夹、空的子文件夹
     *
     * @param file
     */
    public static void deleteEmptyDirectory(File file) {
        if (file != null && file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    deleteEmptyDirectory(files[i]);
                }
                // 子文件夹里的删除完后，重新获取。判断空的子文件删除后，该文件夹是否为空
                files = file.listFiles();
            }
            if (files == null || files.length == 0) {
                String absolutePath = file.getAbsolutePath();
                file.delete();
                log.info("删除空文件夹！路径：" + absolutePath);
            }
        }
    }

    /**
     * 删除tmp文件
     *
     * @param multipartFile
     * @return
     */
    public static boolean deleteTmp(MultipartFile multipartFile) {
        try {
            CommonsMultipartFile commonsMultipartFile = (CommonsMultipartFile) multipartFile;
            DiskFileItem diskFileItem = (DiskFileItem) commonsMultipartFile.getFileItem();
            File storeLocation = diskFileItem.getStoreLocation();
            FileUtil.deleteEmptyDirectory(storeLocation);
            return true;
        } catch (Exception e) {
            log.error("删除tmp文件失败,错误：" + e.getMessage());
            return false;
        }
    }

    /**
     * 打开目录
     *
     * @param path
     */
    public static void open(String path) {
        // 打开输出目录
        try {
            String osName = System.getProperty("os.name");
            if (osName != null) {
                if (osName.contains("Mac")) {
                    Runtime.getRuntime().exec("open " + path);
                } else if (osName.contains("Windows")) {
                    Runtime.getRuntime().exec("cmd /c start " + path);
                } else {
                    log.debug("文件输出目录:" + path);
                }
            }
        } catch (IOException e) {
            LogUtils.error(e);
        }
    }

    /**
     * 向文件中添加内容
     *
     * @param strcontent 内容
     * @param filePath 地址
     * @param fileName 文件名
     */
    public static void writeToFile(String strcontent, String filePath, String fileName) {
        // 生成文件夹之后，再生成文件，不然会出错
        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        File subfile = new File(XSSEscape.escapePath(strFilePath));
        RandomAccessFile raf = null;
        try {
            /** 构造函数 第二个是读写方式 */
            raf = new RandomAccessFile(subfile, "rw");
            /** 将记录指针移动到该文件的最后 */
            raf.seek(subfile.length());
            /** 向文件末尾追加内容 */
            raf.write(strcontent.getBytes());
            raf.close();
        } catch (IOException e) {
            LogUtils.error(e);
        }
    }

    /**
     * 修改文件内容（覆盖或者添加）
     *
     * @param path 文件地址
     * @param content 覆盖内容
     * @param append 指定了写入的方式，是覆盖写还是追加写(true=追加)(false=覆盖)
     */
    public static void modifyFile(String path, String content, boolean append) {
        try {
            @Cleanup FileWriter fileWriter = new FileWriter(path, append);
            @Cleanup BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.append(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            LogUtils.error(e);
        }
    }

    /**
     * 读取文件内容
     *
     * @param filePath 地址
     * @param filename 名称
     * @return 返回内容
     */
    public static String getString(String filePath, String filename) {
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            @Cleanup
            FileInputStream inputStream = new FileInputStream(new File(XSSEscape.escapePath(filePath + filename)));
            @Cleanup InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Constants.UTF8);
            @Cleanup BufferedReader reader = new BufferedReader(inputStreamReader);
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (Exception e) {
            LogUtils.error(e);
        }
        return sb.toString();
    }

    /**
     * 重命名文件
     *
     * @param oldPath 原来的文件地址
     * @param newPath 新的文件地址
     */
    public static void renameFile(String oldPath, String newPath) {
        File oleFile = new File(oldPath);
        File newFile = new File(newPath);
        // 执行重命名
        oleFile.renameTo(newFile);
    }

    /**
     * 复制文件
     *
     * @param fromFile 要复制的文件目录
     * @param toFile 要粘贴的文件目录
     * @return 是否复制成功
     */
    public static boolean copy(String fromFile, String toFile) {
        // 要复制的文件目录
        File[] currentFiles;
        File root = new File(fromFile);
        // 如同判断SD卡是否存在或者文件是否存在
        // 如果不存在则 return出去
        if (!root.exists()) {
            return false;
        }
        // 如果存在则获取当前目录下的全部文件 填充数组
        currentFiles = root.listFiles();
        // 目标目录
        File targetDir = new File(toFile);
        // 创建目录
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        // 遍历要复制该目录下的全部文件
        for (int i = 0; i < currentFiles.length; i++) {
            if (currentFiles[i].isDirectory()) {
                // 如果当前项为子目录 进行递归
                copy(currentFiles[i].getPath() + "/", toFile + currentFiles[i].getName() + "/");
            } else {
                // 如果当前项为文件则进行文件拷贝
                copyFile(currentFiles[i].getPath(), toFile + currentFiles[i].getName());
            }
        }
        return true;
    }

    /**
     * 文件拷贝 要复制的目录下的所有非子目录(文件夹)文件拷贝
     *
     * @param fromFile
     * @param toFile
     * @return
     */
    public static boolean copyFile(String fromFile, String toFile) {
        try {
            @Cleanup InputStream fosfrom = new FileInputStream(XSSEscape.escapePath(fromFile));
            @Cleanup OutputStream fosto = new FileOutputStream(XSSEscape.escapePath(toFile));
            byte[] bt = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 文件拷贝
     *
     * @param fromFile
     * @param toFile
     * @param fileName
     * @return
     */
    public static boolean copyFile(String fromFile, String toFile, String fileName) {
        try {
            // 目标目录
            File targetDir = new File(toFile);
            // 创建目录
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }
            @Cleanup InputStream fosfrom = new FileInputStream(fromFile);
            @Cleanup OutputStream fosto = new FileOutputStream(toFile + fileName);
            byte[] bt = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 保存文件
     *
     * @param inputStream
     * @param path
     * @param fileName
     */
    public static void write(InputStream inputStream, String path, String fileName) {
        OutputStream os = null;
        long dateStr = System.currentTimeMillis();
        try {
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流保存到本地文件
            File tempFile = new File(XSSEscape.escapePath(path));
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            String newFileName = tempFile.getPath() + File.separator + fileName;
            log.info("保存文件：" + newFileName);
            os = new FileOutputStream(XSSEscape.escapePath(newFileName));
            // 开始读取
            while ((len = inputStream.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
        } catch (IOException e) {
            log.error("生成excel失败");
        } catch (Exception e) {
            log.error("生成excel失败");
        } finally {
            // 完毕，关闭所有链接
            try {
                if (os != null) {
                    os.close();
                }
                inputStream.close();
            } catch (IOException e) {
                log.error("关闭链接失败" + e.getMessage());
            }
        }
    }

    /**
     * 写入文件
     *
     * @param inputStream
     * @param path
     * @param fileName
     */
    public static void writeFile(InputStream inputStream, String path, String fileName) {
        OutputStream os = null;
        try {
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流保存到本地文件
            File tempFile = new File(XSSEscape.escapePath(path));
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            String newFileName = tempFile.getPath() + File.separator + fileName;
            log.info("保存文件：" + newFileName);
            os = new FileOutputStream(XSSEscape.escapePath(newFileName));
            // 开始读取
            while ((len = inputStream.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
        } catch (IOException e) {
            log.error("生成excel失败");
        } catch (Exception e) {
            log.error("生成excel失败");
        } finally {
            // 完毕，关闭所有链接
            try {
                if (os != null) {
                    os.close();
                }
                inputStream.close();
            } catch (IOException e) {
                log.error("关闭链接失败" + e.getMessage());
            }
        }
    }

    /**
     * 上传文件
     *
     * @param file 文件
     * @param filePath 保存路径
     * @param fileName 保存名称
     */
    public static void upFile(MultipartFile file, String filePath, String fileName) {
        try {
            String escapeFilePath = XSSEscape.escape(filePath);
            String escapeFileName = XSSEscape.escape(fileName);
            // 输出的文件流保存到本地文件
            File tempFile = new File(escapeFilePath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            File f = new File(escapeFilePath, escapeFileName);
            // 将上传的文件存储到指定位置
            file.transferTo(f);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /** 读取文件修改时间 */
    public static String getCreateTime(String filePath) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        File file = new File(XSSEscape.escapePath(filePath));
        // 毫秒数
        long modifiedTime = file.lastModified();
        // 通过毫秒数构造日期 即可将毫秒数转换为日期
        Date date = new Date(modifiedTime);
        String dateString = format.format(date);
        return dateString;
    }

    /** 获取文件类型 */
    public static String getFileType(File file) {
        if (file.isFile()) {
            String fileName = file.getName();
            String fileTyle = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            return fileTyle;
        }
        return null;
    }

    /** 获取文件类型 */
    public static String getFileType(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".") + 1;
        // 获取文件的后缀名 jpg
        String suffix = fileName.substring(lastIndexOf);
        return suffix;
    }

    /**
     * 获取文件大小
     *
     * @param data
     * @return
     */
    public static String getSize(String data) {
        String size = "";
        if (data != null && !StringUtil.isEmpty(data)) {
            long fileS = Long.parseLong(data);
            DecimalFormat df = new DecimalFormat("#.00");
            if (fileS < 1024) {
                size = df.format((double) fileS) + "BT";
            } else if (fileS < 1048576) {
                size = df.format((double) fileS / 1024) + "KB";
            } else if (fileS < 1073741824) {
                size = df.format((double) fileS / 1048576) + "MB";
            } else {
                size = df.format((double) fileS / 1073741824) + "GB";
            }
        } else {
            size = "0BT";
        }
        return size;
    }

    private static final int BUFFER_SIZE = 2 * 1024;

    /**
     * 压缩文件夹
     *
     * @param srcDir 压缩文件夹路径
     * @param outDir 压缩文件路径
     * @param keepDirStructure 是否保留原来的目录结构, true:保留目录结构;
     *     false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static void toZip(String outDir, boolean keepDirStructure, String... srcDir) {
        try {
            @Cleanup OutputStream out = new FileOutputStream(new File(XSSEscape.escapePath(outDir)));
            @Cleanup ZipOutputStream zos = null;
            try {
                zos = new ZipOutputStream(out);
                List<File> sourceFileList = new ArrayList<File>();
                for (String dir : srcDir) {
                    File sourceFile = new File(XSSEscape.escapePath(dir));
                    sourceFileList.add(sourceFile);
                }
                compress(sourceFileList, zos, keepDirStructure);
            } catch (Exception e) {
                throw new RuntimeException("zip error from ZipUtils", e);
            } finally {
                if (zos != null) {
                    try {
                        zos.close();
                    } catch (IOException e) {
                        LogUtils.error(e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("压缩失败:{}", e.getMessage());
        }
    }

    /**
     * 递归压缩方法
     *
     * @param sourceFile 源文件
     * @param zos zip输出流
     * @param name 压缩后的名称
     * @param keepDirStructure 是否保留原来的目录结构, true:保留目录结构;
     *     false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws Exception
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean keepDirStructure)
            throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            zos.putNextEntry(new ZipEntry(name));
            int len;
            @Cleanup FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                if (keepDirStructure) {
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    if (keepDirStructure) {
                        compress(file, zos, name + "/" + file.getName(), keepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), keepDirStructure);
                    }
                }
            }
        }
    }

    private static void compress(List<File> sourceFileList, ZipOutputStream zos, boolean keepDirStructure)
            throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        for (File sourceFile : sourceFileList) {
            String name = sourceFile.getName();
            if (sourceFile.isFile()) {
                zos.putNextEntry(new ZipEntry(name));
                int len;
                @Cleanup FileInputStream in = new FileInputStream(sourceFile);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
            } else {
                File[] listFiles = sourceFile.listFiles();
                if (listFiles == null || listFiles.length == 0) {
                    if (keepDirStructure) {
                        zos.putNextEntry(new ZipEntry(name + "/"));
                        zos.closeEntry();
                    }
                } else {
                    for (File file : listFiles) {
                        if (keepDirStructure) {
                            compress(file, zos, name + "/" + file.getName(), keepDirStructure);
                        } else {
                            compress(file, zos, file.getName(), keepDirStructure);
                        }
                    }
                }
            }
        }
    }

    // =================================判断文件后缀==========================

    /**
     * 允许文件类型
     *
     * @param fileType 文件所有类型
     * @param fileExtension 当前文件类型
     * @return
     */
    public static boolean fileType(String fileType, String fileExtension) {
        String[] allowExtension = fileType.split(",");
        return Arrays.asList(allowExtension).contains(fileExtension.toLowerCase());
    }

    /**
     * 允许图片类型
     *
     * @param imageType 图片所有类型
     * @param fileExtension 当前图片类型
     * @return
     */
    public static boolean imageType(String imageType, String fileExtension) {
        String[] allowExtension = imageType.split(",");
        return Arrays.asList(allowExtension).contains(fileExtension.toLowerCase());
    }

    /**
     * 允许上传大小
     *
     * @param fileSize 文件大小
     * @param maxSize 最大的文件
     * @return
     */
    public static boolean fileSize(Long fileSize, int maxSize) {
        if (fileSize > maxSize) {
            return true;
        }
        return false;
    }

    /**
     * 导入生成临时文件后，获取文件内容
     *
     * @param multipartFile 文件
     * @param filePath 路径
     * @return
     */
    public static String getFileContent(MultipartFile multipartFile, String filePath) {
        // 文件名
        String fileName = multipartFile.getName();
        // 上传到项目文件路径中
        FileUtil.upFile(multipartFile, filePath, fileName);
        // 读取文件文件内容
        String fileContent = FileUtil.getString(filePath, fileName);
        return fileContent;
    }

    /**
     * 判断是否为json格式且不为空
     *
     * @param multipartFile
     * @param type 类型
     * @return
     */
    public static boolean existsSuffix(MultipartFile multipartFile, String type) {
        if (!multipartFile.getOriginalFilename().endsWith("." + type) || multipartFile.getSize() < 1) {
            return true;
        }
        return false;
    }

    /**
     * File转MultipartFile
     *
     * @param file
     * @return
     */
    public static MultipartFile createFileItem(File file) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem("textField", "text/plain", true, file.getName());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            @Cleanup FileInputStream fis = new FileInputStream(file);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            LogUtils.error(e);
        }
        MultipartFile multipartFile = new CommonsMultipartFile(item);
        return multipartFile;
    }
}
