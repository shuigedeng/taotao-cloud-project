package com.taotao.cloud.office.convert.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import com.taotao.cloud.common.utils.log.LogUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * <p>
 * 文件工具类
 * </p>
 *
 * @description
 * @since 2020/8/27 19:21
 */
public class MyFileUtil {

	/**
	 * 多文件或目录压缩：将`srcPath`目录以及其目录下的所有文件目录打包到`zipPath`+`suffixFileName`文件中 【采用hutool工具类进行打包文件】
	 *
	 * @param srcPath:        需打包的源目录
	 * @param zipPath:        打包后的路径+文件后缀名
	 * @param isWithSrcDir:   是否带目录显示 （true:表示带目录显示）
	 * @param isDeleteSrcZip: 是否删除源目录
	 * @return java.lang.String
	 * @since 2020/8/27 19:25
	 */
	public static File zip(String srcPath, String zipPath, boolean isWithSrcDir, boolean isDeleteSrcZip) {
		LogUtils.debug("【压缩文件】 源目录路径: 【{}】 打包后的路径+文件后缀名: 【{}】", srcPath, zipPath);
		File zipFile = ZipUtil.zip(srcPath, zipPath, isWithSrcDir);
		// 删除目录 -> 保证下次生成文件的时候不会累计上次留下的文件
		if (isDeleteSrcZip) {
			MyFileUtil.deleteFileOrFolder(srcPath);
		}
		return zipFile;
	}

	/**
	 * 根据路径删除指定的目录或文件，无论存在与否
	 *
	 * @param fullFileOrDirPath: 要删除的目录或文件
	 * @return 删除成功返回 true，否则返回 false
	 * @since 2020/9/5 20:56
	 */
	public static boolean deleteFileOrFolder(String fullFileOrDirPath) {
		return FileUtil.del(fullFileOrDirPath);
	}

	/**
	 * 根据路径创建文件
	 *
	 * @param fullFilePath: 文件生成路径
	 * @return 文件信息
	 * @since 2020/9/8 21:41
	 */
	public static File touch(String fullFilePath) {
		return FileUtil.touch(fullFilePath);
	}

	/**
	 * 解压
	 *
	 * @param inputStream: 流
	 * @param zipFilePath: zip文件路径
	 * @param outFileDir:  解压后的目录路径
	 * @param isDeleteZip: 是否删除源zip文件
	 * @return 解压后的文件File信息
	 * @since 2020/9/5 20:50
	 */
	public static File unzip(InputStream inputStream, String zipFilePath, String outFileDir, boolean isDeleteZip) throws IOException {
		LogUtils.debug("【解压文件】 zip文件路径: 【{}】 解压后的目录路径: 【{}】", zipFilePath, outFileDir);
		// zip压缩文件
		File zipFile = FileUtil.newFile(zipFilePath);
		// 写入文件
		FileUtils.copyInputStreamToFile(inputStream, zipFile);
		// 编码方式 "UTF-8" 、"GBK" 【注： gbk编码才能解决报错: java.lang.IllegalArgumentException: MALFORMED】
		File outFile = ZipUtil.unzip(zipFilePath, outFileDir, Charset.forName("GBK"));
		// 删除zip -> 保证下次解压后的文件数据不会累计上次解压留下的文件
		if (isDeleteZip) {
			MyFileUtil.deleteFileOrFolder(zipFilePath);
		}
		return outFile;
	}

	/**
	 * 读取文件内容
	 *
	 * @param file: 文件数据
	 * @return 文件内容
	 * @since 2020/9/5 23:00
	 */
	public static String readFileContent(File file) {
		return FileUtil.readUtf8String(file);
	}

	/**
	 * 读取文件内容
	 *
	 * @param filePath: 文件路径
	 * @return 文件内容
	 * @since 2020/9/5 23:00
	 */
	public static String readFileContent(String filePath) {
		return FileUtil.readUtf8String(filePath);
	}

	/**
	 * 读取文件数据
	 *
	 * @param filePath: 文件路径
	 * @return 文件字节码
	 * @since 2020/9/5 23:00
	 */
	public static byte[] readBytes(String filePath) {
		return FileUtil.readBytes(filePath);
	}

	/**
	 * 写入文件内容
	 *
	 * @param fileContent: 文件内容
	 * @param filePath:    文件路径
	 * @return 文件信息
	 * @since 2020/11/17 21:38
	 */
	public static File writeFileContent(String fileContent, String filePath) {
		return FileUtil.writeUtf8String(fileContent, filePath);
	}

	/**
	 * 字节码写入文件
	 *
	 * @param data:     字节码
	 * @param filePath: 文件路径
	 * @return 文件信息
	 * @since 2020/11/24 14:36
	 */
	public static File writeFileContent(byte[] data, String filePath) {
		return FileUtil.writeBytes(data, filePath);
	}

	public static void main(String[] args) {
		try {
			String filePath = "E:\\IT_zhengqing\\code\\me-workspace\\最新代码生成器\\code-api\\document\\import\\bLogUtils.zip";
			String filePathX = "E:\\IT_zhengqing\\code\\me-workspace\\最新代码生成器\\code-api\\document\\import";
			// File file =
			// FileUtil.newFile(filePath);
			// InputStream fileInputStream = new FileInputStream(file);
			File unzip = ZipUtil.unzip(filePath, filePathX);
			System.out.println(unzip);

			String fileContent = FileUtil.readUtf8String(filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
