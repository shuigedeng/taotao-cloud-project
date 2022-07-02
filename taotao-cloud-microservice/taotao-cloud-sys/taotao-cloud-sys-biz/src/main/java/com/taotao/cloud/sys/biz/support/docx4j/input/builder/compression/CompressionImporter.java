package com.taotao.cloud.sys.biz.support.docx4j.input.builder.compression;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.poi.util.IOUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 压缩包导入
 *
 */
public class CompressionImporter {

	/**
	 * {@link ZipInputStream}
	 */
	private final ZipInputStream zipInputStream;
	/**
	 * 是否在结束后关闭流
	 */
	private final boolean closeable;
	/**
	 * 压缩包中文件夹消费
	 */
	private Consumer<String> folderConsumer;
	/**
	 * 压缩包中文件消费
	 */
	private BiConsumer<String, InputStream> fileConsumer;

	CompressionImporter(InputStream is, boolean closeable) {
		this.closeable = closeable;
		this.zipInputStream = new ZipInputStream(is, Charset.forName("GBK"));
	}

	/**
	 * 快速创建压缩包导入构建器
	 *
	 * @param is        {@link InputStream}
	 * @param closeable 是否需要关闭输入流
	 * @return {@link CompressionImporter}
	 */
	public static CompressionImporter create(InputStream is, boolean closeable) {
		return new CompressionImporter(is, closeable);
	}

	/**
	 * 快速创建压缩包导入构建器
	 *
	 * @param is {@link InputStream}
	 * @return {@link CompressionImporter}
	 */
	public static CompressionImporter create(InputStream is) {
		return CompressionImporter.create(is, true);
	}

	/**
	 * {@link MultipartFile}快速创建
	 *
	 * @param file {@link MultipartFile}
	 * @return {@link CompressionImporter}
	 */
	public static CompressionImporter create(MultipartFile file) {
		try {
			return CompressionImporter.create(file.getInputStream());
		} catch (IOException e) {
			throw new CompressionImportException(e);
		}
	}

	/**
	 * 压缩包目录消费
	 *
	 * @param consumer 目录消费
	 * @return {@link CompressionImporter}
	 */
	public CompressionImporter folder(Consumer<String> consumer) {
		this.folderConsumer = consumer;
		return this;
	}

	/**
	 * 压缩包文件消费
	 *
	 * @param consumer 文件消费
	 * @return {@link CompressionImporter}
	 */
	public CompressionImporter file(BiConsumer<String, InputStream> consumer) {
		this.fileConsumer = consumer;
		return this;
	}

	/**
	 * 压缩文件读取
	 */
	public void resolve() {
		try {
			ZipEntry nextEntry;
			while ((nextEntry = this.zipInputStream.getNextEntry()) != null) {
				if (nextEntry.isDirectory() && Objects.nonNull(this.folderConsumer)) {
					this.folderConsumer.accept(nextEntry.getName());
				} else if (!nextEntry.isDirectory() && Objects.nonNull(this.fileConsumer)) {
					this.fileConsumer.accept(nextEntry.getName(), this.zipInputStream);
				}
			}
		} catch (IOException e) {
			throw new CompressionImportException(e);
		} finally {
			if (this.closeable) {
				IOUtils.closeQuietly(this.zipInputStream);
			}
		}
	}
}
