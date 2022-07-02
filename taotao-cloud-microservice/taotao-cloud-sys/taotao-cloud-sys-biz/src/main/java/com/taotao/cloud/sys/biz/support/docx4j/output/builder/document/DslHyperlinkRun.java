package com.taotao.cloud.sys.biz.support.docx4j.output.builder.document;

import java.util.function.Consumer;
import org.apache.poi.xwpf.usermodel.XWPFHyperlinkRun;

/**
 * 超链接 {@link XWPFHyperlinkRun}
 */
public class DslHyperlinkRun {
	private final XWPFHyperlinkRun hyperlinkRun;

    DslHyperlinkRun(XWPFHyperlinkRun hyperlinkRun) {
        this.hyperlinkRun = hyperlinkRun;
    }

    /**
     * 超链接更多设置
     * @param consumer 超链接消费
     * @return {@link DslHyperlinkRun}
     */
    public DslHyperlinkRun more(Consumer<XWPFHyperlinkRun> consumer) {
        consumer.accept(this.hyperlinkRun);
        return this;
    }

    /**
     * 快速创建超链接
     * @param text 超链接文本
     * @return {@link DslHyperlinkRun}
     */
    public DslHyperlinkRun text(String text) {
        return this.more(r -> r.setText(text));
    }
}
