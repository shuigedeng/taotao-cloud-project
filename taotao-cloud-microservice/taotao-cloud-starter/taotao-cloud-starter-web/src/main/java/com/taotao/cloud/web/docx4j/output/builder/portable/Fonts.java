package com.taotao.cloud.web.docx4j.output.builder.portable;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;

import java.io.IOException;

/**
 * pdf字体工具类
 */
public enum Fonts {
    /**
     * 正文、表格数据单元格
     */
    NORMAL(10, Font.NORMAL),
    /**
     * 表格表头单元格
     */
    BOLD_NORMAL(10, Font.BOLD),
    /**
     * 页眉页脚
     */
    HEADER_FOOTER(8, Font.NORMAL) {
        @Override
        public Font font() {
            Font font = super.font();
            // 设置页眉页脚字体颜色
            font.setColor(BaseColor.GRAY);
            return font;
        }
    },
    /**
     * 对应word标题一
     */
    HEADING_1(21, Font.BOLD),
    /**
     * 对应word标题二
     */
    HEADING_2(18, Font.BOLD),
    /**
     * 对应word标题三
     */
    HEADING_3(16, Font.BOLD),
    /**
     * 对应word标题五
     */
    HEADING_5(14, Font.BOLD),
    /**
     * 对应word标题七
     */
    HEADING_7(12, Font.BOLD),
    /**
     * 对应word标题九
     */
    HEADING_9(10, Font.BOLD);
    /**
     * 字体大小
     */
    public final float size;
    /**
     * 字体样式
     */
    public final int style;
    /**
     * 基准中文字体
     */
    public static final BaseFont BASE_CN_FONT;

    static {
        try {
            // 基础中文字体
            BASE_CN_FONT = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        } catch (DocumentException | IOException e) {
            throw new PortableExportException(e);
        }
    }

    /**
     * 在基础字体样式基础上创建不同大小、样式的字体
     * @param size  字体大小
     * @param style 字体风格{@link Font#BOLD}
     * @return {@link Font}
     */
    public static Font font(float size, int style) {
        return new Font(BASE_CN_FONT, size, style);
    }

    /**
     * 获得枚举对应字体
     * @return {@link Font}
     */
    public Font font() {
        return Fonts.font(this.size, this.style);
    }

	Fonts(float size, int style) {
		this.size = size;
		this.style = style;
	}
}
