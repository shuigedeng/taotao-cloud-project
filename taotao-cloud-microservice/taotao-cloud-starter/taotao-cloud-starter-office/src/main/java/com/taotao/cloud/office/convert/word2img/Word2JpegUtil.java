package com.taotao.cloud.office.convert.word2img;

import com.aspose.words.Document;
import com.aspose.words.ImageSaveOptions;
import com.aspose.words.PageSet;
import com.aspose.words.SaveFormat;
import com.google.common.collect.Lists;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

/**
 * <p>
 * Word 转 JPEG 工具类
 * </p>
 *
 * @description
 * @since 2020/11/23 16:00
 */
public class Word2JpegUtil {

	/**
	 * `word` 转 `jpeg`
	 *
	 * @param wordBytes: word字节码数据
	 * @return 图片字节码数据列表
	 * @since 2020/11/24 11:52
	 */
	public static List<byte[]> wordBytes2JpegBytes(byte[] wordBytes) throws Exception {
		Document doc = new Document(new ByteArrayInputStream(wordBytes));
		ImageSaveOptions iso = new ImageSaveOptions(SaveFormat.JPEG);
		iso.setResolution(128);
		iso.setPrettyFormat(true);
		iso.setUseAntiAliasing(true);

		List<byte[]> jpegList = Lists.newArrayList();
		for (int i = 0; i < doc.getPageCount(); i++) {
			PageSet pageSet = new PageSet(i);
			iso.setPageSet(pageSet);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			doc.save(outputStream, iso);
			jpegList.add(outputStream.toByteArray());
		}

		return jpegList;
	}

	/**
	 * `word` 转 `jpeg`
	 *
	 * @param wordBytes:   word字节码数据
	 * @param imgRootPath: 生成图片根路径
	 * @return 图片文件数据列表
	 * @since 2020/11/24 11:52
	 */
	public static List<File> wordBytes2JpegFileList(byte[] wordBytes, String imgRootPath) throws Exception {
		Document doc = new Document(new ByteArrayInputStream(wordBytes));
		ImageSaveOptions iso = new ImageSaveOptions(SaveFormat.JPEG);
		iso.setResolution(128);
		iso.setPrettyFormat(true);
		iso.setUseAntiAliasing(true);

		List<File> jpegList = Lists.newArrayList();
		for (int i = 0; i < doc.getPageCount(); i++) {
			String imgPath = imgRootPath + "/" + (i + 1) + ".jpg";
			PageSet pageSet = new PageSet(i);
			iso.setPageSet(pageSet);
			doc.save(imgPath, iso);
			jpegList.add(new File(imgPath));
		}

		return jpegList;
	}

}
