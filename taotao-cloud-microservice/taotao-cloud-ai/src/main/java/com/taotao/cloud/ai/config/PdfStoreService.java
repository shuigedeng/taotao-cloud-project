package com.taotao.cloud.ai.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PdfStoreService {

	private final DefaultResourceLoader resourceLoader;
	private final VectorStore vectorStore;
	private final TokenTextSplitter tokenTextSplitter;

	/**
	 * 根据PDF的页数进行分割
	 *
	 * @param url
	 */
	public void saveSourceByPage(String url) {
		// 加载资源，需要本地路径的信息
		Resource resource = resourceLoader.getResource(url);
		// 加载PDF文件时的配置对象
		PdfDocumentReaderConfig loadConfig = PdfDocumentReaderConfig.builder()
			.withPageExtractedTextFormatter(
				new ExtractedTextFormatter
					.Builder()
					.withNumberOfBottomTextLinesToDelete(3)
					.withNumberOfTopPagesToSkipBeforeDelete(1)
					.build()
			)
			.withPagesPerDocument(1)
			.build();

		PagePdfDocumentReader pagePdfDocumentReader = new PagePdfDocumentReader(resource,
			loadConfig);
		// 存储到向量数据库中
		vectorStore.accept(tokenTextSplitter.apply(pagePdfDocumentReader.get()));
	}

	/**
	 * 根据PDF的目录（段落）进行划分
	 *
	 * @param url
	 */
	public void saveSourceByParagraph(String url) {
		Resource resource = resourceLoader.getResource(url);

		PdfDocumentReaderConfig loadConfig = PdfDocumentReaderConfig.builder()
			.withPageExtractedTextFormatter(
				new ExtractedTextFormatter
					.Builder()
					.withNumberOfBottomTextLinesToDelete(3)
					.withNumberOfTopPagesToSkipBeforeDelete(1)
					.build()
			)
			.withPagesPerDocument(1)
			.build();

		ParagraphPdfDocumentReader pdfReader = new ParagraphPdfDocumentReader(
			resource,
			loadConfig
		);
		vectorStore.accept(tokenTextSplitter.apply(pdfReader.get()));
	}

	/**
	 * MultipartFile对象存储，采用PagePdfDocumentReader
	 *
	 * @param file
	 */
	public void saveSource(MultipartFile file) {
		try {
			// 获取文件名
			String fileName = file.getOriginalFilename();
			// 获取文件内容类型
			String contentType = file.getContentType();
			// 获取文件字节数组
			byte[] bytes = file.getBytes();
			// 创建一个临时文件
			Path tempFile = Files.createTempFile("temp-", fileName);
			// 将文件字节数组保存到临时文件
			Files.write(tempFile, bytes);
			// 创建一个 FileSystemResource 对象
			Resource fileResource = new FileSystemResource(tempFile.toFile());
			PdfDocumentReaderConfig loadConfig = PdfDocumentReaderConfig.builder()
				.withPageExtractedTextFormatter(
					new ExtractedTextFormatter
						.Builder()
						.withNumberOfBottomTextLinesToDelete(3)
						.withNumberOfTopPagesToSkipBeforeDelete(1)
						.build()
				)
				.withPagesPerDocument(1)
				.build();
			PagePdfDocumentReader pagePdfDocumentReader = new PagePdfDocumentReader(fileResource,
				loadConfig);
			vectorStore.accept(tokenTextSplitter.apply(pagePdfDocumentReader.get()));
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}
}
