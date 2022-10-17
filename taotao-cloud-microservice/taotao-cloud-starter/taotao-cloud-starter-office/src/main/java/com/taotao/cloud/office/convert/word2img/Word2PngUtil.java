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
 * Word 转 PNG 工具类
 * </p>
 *
 * @description
 * @since 2020/11/23 16:00
 */
public class Word2PngUtil {

	/**
	 * `word` 转 `png`
	 *
	 * @param wordBytes: word字节码数据
	 * @return 图片字节码数据列表
	 * @since 2020/11/24 11:52
	 */
	public static List<byte[]> wordBytes2PngBytes(byte[] wordBytes) throws Exception {
		Document doc = new Document(new ByteArrayInputStream(wordBytes));
		ImageSaveOptions iso = new ImageSaveOptions(SaveFormat.PNG);
		iso.setResolution(128);
		iso.setPrettyFormat(true);
		iso.setUseAntiAliasing(true);

		List<byte[]> pngList = Lists.newArrayList();
		for (int i = 0; i < doc.getPageCount(); i++) {
			PageSet pageSet = new PageSet(i);
			iso.setPageSet(pageSet);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			doc.save(outputStream, iso);
			pngList.add(outputStream.toByteArray());
		}

		return pngList;
	}

	/**
	 * `word` 转 `png`
	 *
	 * @param wordBytes:   word字节码数据
	 * @param imgRootPath: 生成图片路径
	 * @return 图片文件数据列表
	 * @since 2020/11/24 11:52
	 */
	public static List<File> wordBytes2PngFileList(byte[] wordBytes, String imgRootPath) throws Exception {
		Document doc = new Document(new ByteArrayInputStream(wordBytes));
		ImageSaveOptions iso = new ImageSaveOptions(SaveFormat.PNG);
		iso.setResolution(128);
		iso.setPrettyFormat(true);
		iso.setUseAntiAliasing(true);

		List<File> pngList = Lists.newArrayList();
		for (int i = 0; i < doc.getPageCount(); i++) {
			String imgPath = imgRootPath + "/" + (i + 1) + ".png";
			PageSet pageSet = new PageSet(i);
			iso.setPageSet(pageSet);
			doc.save(imgPath, iso);
			pngList.add(new File(imgPath));
		}

		return pngList;
	}

}
