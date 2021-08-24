/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.common.utils;

import com.taotao.cloud.common.exception.BaseException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * 压缩文件夹工具类
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/4/30 10:45
 */
public class CompressUtil {

	/**
	 * 压缩文件夹到指定zip文件
	 *
	 * @param srcDir     源文件夹
	 * @param targetFile 目标知道zip文件
	 */
	public static void zip(String srcDir, String targetFile) {
		try (
			OutputStream outputStream = new FileOutputStream(targetFile);
		) {
			zip(srcDir, outputStream);
		} catch (Exception exp) {
			throw new BaseException("压缩文件出错", exp);
		}
	}

	/**
	 * 压缩文件夹到指定输出流中，可以是本地文件输出流，也可以是web响应下载流
	 *
	 * @param srcDir       源文件夹
	 * @param outputStream 压缩后文件的输出流
	 */
	public static void zip(String srcDir, OutputStream outputStream) {
		try (
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
			ArchiveOutputStream out = new ZipArchiveOutputStream(bufferedOutputStream);
		) {
			Path start = Paths.get(srcDir);
			Files.walkFileTree(start, new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
					throws IOException {
					ArchiveEntry entry = new ZipArchiveEntry(dir.toFile(),
						start.relativize(dir).toString());
					out.putArchiveEntry(entry);
					out.closeArchiveEntry();
					return super.preVisitDirectory(dir, attrs);
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
					throws IOException {
					try (
						InputStream input = new FileInputStream(file.toFile())
					) {
						ArchiveEntry entry = new ZipArchiveEntry(file.toFile(),
							start.relativize(file).toString());
						out.putArchiveEntry(entry);
						IOUtils.copy(input, out);
						out.closeArchiveEntry();
					}
					return super.visitFile(file, attrs);
				}

			});

		} catch (Exception exp) {
			throw new BaseException("压缩文件出错", exp);
		}
	}

	/**
	 * 解压zip文件到指定文件夹
	 *
	 * @param zipFileName 源zip文件路径
	 * @param destDir     解压后输出路径
	 */
	public static void unzip(String zipFileName, String destDir) {
		try (
			InputStream inputStream = new FileInputStream(zipFileName);
		) {
			unzip(inputStream, destDir);
		} catch (Exception exp) {
			throw new BaseException("解压文件出错", exp);
		}
	}

	/**
	 * 从输入流中获取zip文件，并解压到指定文件夹
	 *
	 * @param inputStream zip文件输入流，可以是本地文件输入流，也可以是web请求上传流
	 * @param destDir     解压后输出路径
	 */
	public static void unzip(InputStream inputStream, String destDir) {
		try (
			BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
			ArchiveInputStream in = new ZipArchiveInputStream(bufferedInputStream);
		) {
			ArchiveEntry entry = null;
			while (Objects.nonNull(entry = in.getNextEntry())) {
				if (in.canReadEntryData(entry)) {
					File file = Paths.get(destDir, entry.getName()).toFile();
					if (entry.isDirectory()) {
						if (!file.exists()) {
							file.mkdirs();
						}
					} else {
						try (
							OutputStream out = new FileOutputStream(file);
						) {
							IOUtils.copy(in, out);
						}
					}
				} else {
					System.out.println(entry.getName());
				}
			}
		} catch (Exception exp) {
			throw new BaseException("解压文件出错", exp);
		}
	}
}
