package com.taotao.cloud.sys.biz.tools.core.utils;

import com.taotao.cloud.common.utils.LogUtil;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 功能:zip 处理工具类,需要 apache-commons-compress 支持 <br/>
 */
public class ZipUtil {

	/**
	 * 
	 * 功能: 压缩文件 <br/>
	 * @param dirOrFile 需要压缩的文件或目录
	 * @param zipFile 压缩文件路径和名称
	 */
	public static void zip(File dirOrFile, File zipFile) {
		List<File> files = new ArrayList<File>();
		findFiles(dirOrFile, files);
		String basePath = dirOrFile.getParent();
		ZipArchiveOutputStream zaos = null;
		try {
			zaos = new ZipArchiveOutputStream(zipFile);
			zaos.setUseZip64(Zip64Mode.AsNeeded);
			for (File file : files) {
				// 需要获取文件的相对路径,相对于打包路径
				String currentPath = file.getPath();
				String relativePath = currentPath.replace(basePath + File.separator, "");
				relativePath = relativePath.replaceAll("\\\\", "/");			//兼容linux 环境
				ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, relativePath);
				zaos.putArchiveEntry(zipArchiveEntry);
				if (file.isDirectory()) {
					zaos.closeArchiveEntry();
					continue;
				}
				FileInputStream inputStream = new FileInputStream(file);
				IOUtils.copy(inputStream, zaos);
				zaos.closeArchiveEntry();
				IOUtils.closeQuietly(inputStream);
			}
			zaos.finish();
		} catch (IOException e) {
			LogUtil.error("ZipUtil zip error: {}",e.getMessage(),e);
		} finally {
			IOUtils.closeQuietly(zaos);
		}
	}
	
	/**
	 * 
	 * 功能:对 zip 方法的优化,直接在当前目录创建压缩文件  <br/>
	 * @param dirOrFile 需要压缩的文件或目录
	 * @return 
	 */
	public static File zip(File dirOrFile){
		File zipFile = new File(dirOrFile.getParentFile(),dirOrFile.getName()+".zip");
		zip(dirOrFile, zipFile);
		return zipFile;
	}

	/**
	 * 
	 * 功能:递归目录下所有文件 <br/>
	 * 入参: <br/>
	 */
	private static void findFiles(File dirOrFile, List<File> files) {
		if (dirOrFile.isDirectory()) {
			files.add(dirOrFile); // 把目录也加上
			File[] listFiles = dirOrFile.listFiles();
			for (File file : listFiles) {
				findFiles(file, files);
			}
		} else {
			files.add(dirOrFile);
		}
	}

	/**
	 * 
	 * 功能: zip 解压缩<br/>
	 * 入参: <br/>
	 */
	public static void unzip(File zipFile, String outputPath) {
		if(StringUtils.isBlank(outputPath)){
			//如果没有指定输出目录,则使用 zipFile 文件目录
			outputPath = zipFile.getParent();
		}
		if (zipFile != null && zipFile.exists()) {
			//创建输出目录
			File outputDir = new File(outputPath);
			if(!outputDir.exists()){
				outputDir.mkdirs();
			}
			
			//开始解压
			InputStream is = null;
			ZipArchiveInputStream zais = null;
			try {
				is = new FileInputStream(zipFile);
				zais = new ZipArchiveInputStream(is);
				ArchiveEntry archiveEntry = null;
				FileOutputStream fos = null;
				while ((archiveEntry = zais.getNextEntry()) != null) {   
					String entryFileName = archiveEntry.getName();
					File entryFile = new File(outputDir,entryFileName);
					if(archiveEntry.isDirectory()){
						entryFile.mkdirs();
						continue;
					}
					fos = new FileOutputStream(entryFile);
					IOUtils.copy(zais, fos);
					IOUtils.closeQuietly(fos);
				}
			} catch (IOException e) {
				LogUtil.error("ZipUtil unzip error: {}",e.getMessage(),e);
			} finally{
				IOUtils.closeQuietly(is);
				IOUtils.closeQuietly(zais);
			}
		}
	}

}
