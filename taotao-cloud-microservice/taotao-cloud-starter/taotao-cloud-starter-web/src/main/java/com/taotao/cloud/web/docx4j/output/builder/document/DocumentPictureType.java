package com.taotao.cloud.web.docx4j.output.builder.document;

import com.taotao.cloud.web.docx4j.output.utils.FileUtil;
import org.apache.poi.xwpf.usermodel.Document;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 文档支持的图片类型
 */
public enum DocumentPictureType {
    /**
     * emf
     */
    EMF("emf", Document.PICTURE_TYPE_EMF),
    WMF("wmf", Document.PICTURE_TYPE_WMF),
    PICT("pict", Document.PICTURE_TYPE_PICT),
    /**
     * jpeg
     */
    JPEG("jpeg", Document.PICTURE_TYPE_JPEG),
    JPG("jpg", Document.PICTURE_TYPE_JPEG),
    PNG("png", Document.PICTURE_TYPE_PNG),
    DIB("dib", Document.PICTURE_TYPE_DIB),
    GIF("gif", Document.PICTURE_TYPE_GIF),
    TIFF("tiff", Document.PICTURE_TYPE_TIFF),
    EPS("eps", Document.PICTURE_TYPE_EPS),
    BMP("bmp", Document.PICTURE_TYPE_BMP),
    WPG("wpg", Document.PICTURE_TYPE_WPG);
    /**
     * 图片后缀
     */
    private final String suffix;
    /**
     * 图片对应格式
     */
    private final int format;

    /**
     * 获取图片文件poi格式类型
     * @param fileName 文件名
     * @return 格式类型
     */
    public static int getFormat(String fileName) {
        return
            Optional.ofNullable(fileName)
                .map(FileUtil::suffix)
                .filter(it -> !it.isEmpty())
                .map(String::toLowerCase)
                .flatMap(it ->
                    Stream.of(DocumentPictureType.values())
                        .filter(type -> Objects.equals(type.suffix, it))
                        .findFirst()
                        .map(type -> type.format)
                )
                .orElseThrow(() -> new DocumentExportException("document not support picture file:" + fileName));
    }

    /**
     * 是否支持图片类型
     * @param fileName 文件名
     * @return true/false
     */
    public static boolean isSupport(String fileName) {
        try {
            DocumentPictureType.getFormat(fileName);
            return true;
        } catch (Exception ignore) {
            return false;
        }
    }

	DocumentPictureType(String suffix, int format) {
		this.suffix = suffix;
		this.format = format;
	}
}
