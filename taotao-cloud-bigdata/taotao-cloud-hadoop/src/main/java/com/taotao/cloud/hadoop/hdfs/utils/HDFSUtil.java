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

package com.taotao.cloud.hadoop.hdfs.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.taotao.boot.common.utils.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.io.IOUtils;

/**
 * HDFSUtil
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/10/29 15:27
 */
public class HDFSUtil {

    private final Configuration conf;

    /**
     * 默认的HDFS路径，比如：hdfs://192.168.197.130:9000
     */
    private final String defaultHdfsUri;

    private final String username;

    private static final int BUFFER_SIZE = 1024 * 1024 * 64;

    public HDFSUtil(Configuration conf, String defaultHdfsUri, String username) {
        this.conf = conf;
        this.defaultHdfsUri = defaultHdfsUri;
        this.username = username;
    }

    /**
     * 获取HDFS文件系统
     */
    private FileSystem getFileSystem() throws Exception {
        return FileSystem.get(new URI(defaultHdfsUri), conf, username);
    }

    /**
     * 在HDFS创建文件夹
     */
    public boolean mkdir(String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        if (existFile(path)) {
            return true;
        }
        // 目标路径
        Path srcPath = new Path(path);
        FileSystem fileSystem = getFileSystem();
        boolean isOk = fileSystem.mkdirs(srcPath);
        fileSystem.close();
        return isOk;
    }

    /**
     * 判断HDFS文件是否存在
     */
    public boolean existFile(String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        Path srcPath = new Path(path);
        FileSystem fileSystem = getFileSystem();
        return fileSystem.exists(srcPath);
    }

    /**
     * 读取HDFS目录信息
     */
    public List<Map<String, Object>> readPathInfo(String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        if (!existFile(path)) {
            return null;
        }
        // 目标路径
        Path newPath = new Path(path);
        FileSystem fileSystem = getFileSystem();
        FileStatus[] statusList = fileSystem.listStatus(newPath);

        List<Map<String, Object>> list = new ArrayList<>();
        if (null != statusList && statusList.length > 0) {
            for (FileStatus fileStatus : statusList) {
                Map<String, Object> map = new HashMap<>();
                map.put("filePath", fileStatus.getPath());
                map.put("fileStatus", fileStatus.toString());
                list.add(map);
            }
            return list;
        } else {
            return null;
        }
    }

    //	/**
    //	 * HDFS创建文件
    //	 */
    //	public void createFile(String path, MultipartFile file) throws Exception {
    //		if (StringUtils.isEmpty(path)) {
    //			return;
    //		} else {
    //			file.getBytes();
    //		}
    //		String fileName = file.getOriginalFilename();
    //		// 上传时默认当前目录，后面自动拼接文件的目录
    //		Path newPath = new Path(path + "/" + fileName);
    //		// 打开一个输出流
    //		FileSystem fileSystem = getFileSystem();
    //		FSDataOutputStream outputStream = fileSystem.create(newPath);
    //		outputStream.write(file.getBytes());
    //		outputStream.close();
    //		fileSystem.close();
    //	}

    /**
     * 读取HDFS文件内容
     */
    public String readFile(String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        if (!existFile(path)) {
            return null;
        }
        // 目标路径
        Path srcPath = new Path(path);
        try (FileSystem fileSystem = getFileSystem();
                FSDataInputStream inputStream = fileSystem.open(srcPath)) {
            // 防止中文乱码
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String lineTxt = "";
            StringBuffer sb = new StringBuffer();
            while ((lineTxt = reader.readLine()) != null) {
                sb.append(lineTxt);
            }
            return sb.toString();
        }
    }

    /**
     * 读取HDFS文件列表
     */
    public List<Map<String, String>> listFile(String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        if (!existFile(path)) {
            return null;
        }
        // 目标路径
        Path srcPath = new Path(path);
        // 递归找到所有文件
        FileSystem fileSystem = getFileSystem();
        RemoteIterator<LocatedFileStatus> filesList = fileSystem.listFiles(srcPath, true);
        List<Map<String, String>> returnList = new ArrayList<>();
        while (filesList.hasNext()) {
            LocatedFileStatus next = filesList.next();
            String fileName = next.getPath().getName();
            Path filePath = next.getPath();
            Map<String, String> map = new HashMap<>();
            map.put("fileName", fileName);
            map.put("filePath", filePath.toString());
            returnList.add(map);
        }
        fileSystem.close();
        return returnList;
    }

    /**
     * HDFS重命名文件
     */
    public boolean renameFile(String oldName, String newName) throws Exception {
        if (StringUtils.isEmpty(oldName) || StringUtils.isEmpty(newName)) {
            return false;
        }
        // 原文件目标路径
        Path oldPath = new Path(oldName);
        // 重命名目标路径
        Path newPath = new Path(newName);
        FileSystem fileSystem = getFileSystem();
        boolean isOk = fileSystem.rename(oldPath, newPath);
        fileSystem.close();
        return isOk;
    }

    /**
     * 删除HDFS文件
     */
    public boolean deleteFile(String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        if (!existFile(path)) {
            return false;
        }
        Path srcPath = new Path(path);
        FileSystem fileSystem = getFileSystem();
        boolean isOk = fileSystem.deleteOnExit(srcPath);
        fileSystem.close();
        return isOk;
    }

    /**
     * 上传HDFS文件
     */
    public void uploadFile(String path, String uploadPath) throws Exception {
        if (StringUtils.isEmpty(path) || StringUtils.isEmpty(uploadPath)) {
            return;
        }
        // 上传路径
        Path clientPath = new Path(path);
        // 目标路径
        Path serverPath = new Path(uploadPath);
        FileSystem fileSystem = getFileSystem();
        // 调用文件系统的文件复制方法，第一个参数是否删除原文件true为删除，默认为false
        fileSystem.copyFromLocalFile(false, clientPath, serverPath);
        fileSystem.close();
    }

    /**
     * 下载HDFS文件
     */
    public void downloadFile(String path, String downloadPath) throws Exception {
        if (StringUtils.isEmpty(path) || StringUtils.isEmpty(downloadPath)) {
            return;
        }
        // 上传路径
        Path clientPath = new Path(path);
        // 目标路径
        Path serverPath = new Path(downloadPath);
        FileSystem fileSystem = getFileSystem();
        // 调用文件系统的文件复制方法，第一个参数是否删除原文件true为删除，默认为false
        fileSystem.copyToLocalFile(false, clientPath, serverPath);
        fileSystem.close();
    }

    /**
     * HDFS文件复制
     */
    public void copyFile(String sourcePath, String targetPath) throws Exception {
        if (StringUtils.isEmpty(sourcePath) || StringUtils.isEmpty(targetPath)) {
            return;
        }
        // 原始文件路径
        Path oldPath = new Path(sourcePath);
        // 目标路径
        Path newPath = new Path(targetPath);

        FSDataInputStream inputStream = null;
        FSDataOutputStream outputStream = null;
        try (FileSystem fileSystem = getFileSystem()) {
            inputStream = fileSystem.open(oldPath);
            outputStream = fileSystem.create(newPath);

            IOUtils.copyBytes(inputStream, outputStream, BUFFER_SIZE, false);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    /**
     * 打开HDFS上的文件并返回byte数组
     */
    public byte[] openFileToBytes(String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        if (!existFile(path)) {
            return null;
        }
        // 目标路径
        Path srcPath = new Path(path);
        try (FileSystem fileSystem = getFileSystem()) {
            FSDataInputStream inputStream = fileSystem.open(srcPath);
            return IOUtils.readFullyToByteArray(inputStream);
        }
    }

    /**
     * 打开HDFS上的文件并返回java对象
     */
    public <T> T openFileToObject(String path, Class<T> clazz) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        if (!existFile(path)) {
            return null;
        }
        String jsonStr = readFile(path);
        //		return JsonUtil.(jsonStr, clazz);
        return null;
    }

    /**
     * 获取某个文件在HDFS的集群位置
     */
    public BlockLocation[] getFileBlockLocations(String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        if (!existFile(path)) {
            return null;
        }
        // 目标路径
        Path srcPath = new Path(path);
        FileSystem fileSystem = getFileSystem();
        FileStatus fileStatus = fileSystem.getFileStatus(srcPath);
        return fileSystem.getFileBlockLocations(fileStatus, 0, fileStatus.getLen());
    }
}
