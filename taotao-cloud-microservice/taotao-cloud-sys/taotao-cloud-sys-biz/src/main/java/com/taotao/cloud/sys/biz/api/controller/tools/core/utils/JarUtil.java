package com.taotao.cloud.sys.biz.api.controller.tools.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.jar.Manifest;

import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Administrator
 * 打 jar 包工具
 */
@Slf4j
public class JarUtil {

    /**
     * 获取标准格式的 classpath
     * @param classpath
     * @return
     */
    public static String standardClasspath(String classpath){
        final String[] items = StringUtils.split(classpath, ";");
        return StringUtils.join(items," ");
    }

    /**
     * 创建一个临时的 manifest  MANIFEST.MF
     * @param outputDir 文件输出目录, 会自动在这个目录下创建 META-INF/MANIFEST.MF 文件
     * @param manifest 清单信息
     * @return
     */
    public static File createManifestFile(File outputDir, Manifest manifest) throws IOException {
        final File file = new File(outputDir, "META-INF/MANIFEST.MF");
        file.getParentFile().mkdirs();
        try(final FileOutputStream fileOutputStream = new FileOutputStream(file)){
            manifest.write(fileOutputStream);
        }
        return file;
    }

    /**
     * 添加 jar 包
     * @param outputFile 输出文件
     * @param files
     * @return
     */
    public static File jar(File outputFile, File... files) throws IOException {
        if (!outputFile.getParentFile().exists()){
            // 创建父级目录
            outputFile.getParentFile().mkdirs();
        }
        try(final JarArchiveOutputStream jarArchiveOutputStream = new JarArchiveOutputStream(new FileOutputStream(outputFile));){
            for (File file : files) {
                if (file.isFile()){
                    addFile(jarArchiveOutputStream,file,file.getName());
                }else if (file.isDirectory()){
                    addDirectory(jarArchiveOutputStream,file,new OnlyPath(file.getParentFile()));
                }
            }

            jarArchiveOutputStream.finish();
        }

        return outputFile;
    }

    /**
     * jar 中添加一个目录
     * @param jarArchiveOutputStream
     * @param file
     * @param path
     * @throws IOException
     */
    private static void addDirectory(JarArchiveOutputStream jarArchiveOutputStream,File file,OnlyPath path) throws IOException {
        final Collection<File> listFilesAndDirs = FileUtils.listFilesAndDirs(file, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        for (File listFilesAndDir : listFilesAndDirs) {
            final String relativePath = path.relativize(new OnlyPath(listFilesAndDir)).toString();
            if (listFilesAndDir.isDirectory()) {
                // 如果是目录, 先添加一个 entry
                JarArchiveEntry jarArchiveEntry = new JarArchiveEntry(relativePath + "/");
                jarArchiveOutputStream.putArchiveEntry(jarArchiveEntry);
                jarArchiveOutputStream.closeArchiveEntry();
                continue;
            }
            addFile(jarArchiveOutputStream,listFilesAndDir,relativePath);
        }
    }

    /**
     * jar 文件中添加一个文件
     * @param jarArchiveOutputStream
     * @param file
     * @param path
     * @throws IOException
     */
    private static void addFile(JarArchiveOutputStream jarArchiveOutputStream,File file,String path) throws IOException {
        JarArchiveEntry jarArchiveEntry = new JarArchiveEntry(path);
        jarArchiveOutputStream.putArchiveEntry(jarArchiveEntry);
        try(final FileInputStream fileInputStream = new FileInputStream(file)){
            IOUtils.copy(fileInputStream,jarArchiveOutputStream);
            jarArchiveOutputStream.closeArchiveEntry();
        }
    }

}
