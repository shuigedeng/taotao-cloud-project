package com.taotao.cloud.sys.biz.support.docx4j.output.utils;

import com.taotao.cloud.sys.biz.support.docx4j.output.OutputConstants;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * {@link HttpServletResponse}工具
 */
public interface HttpResponseUtil {
    /**
     * 文件下载文件名称处理
     * @param originFileName 文件原始名称
     * @param response       {@link HttpServletResponse}
     */
    static void handleOutputFileName(String originFileName, HttpServletResponse response) {
        // 设置下载文件名
        String newFileName =
            Optional.of(HttpRequestUtil.isIeBrowser())
                .filter(it -> it)
                .map(t -> {
                    try {
                        return
                            // ie系浏览器需要url编码
                            URLEncoder.encode(originFileName, StandardCharsets.UTF_8.name())
                                // ie浏览器空格变+号问题
                                .replace("+", "%20");
                    } catch (UnsupportedEncodingException e) {
                        return OutputConstants.EMPTY;
                    }
                })
                .orElseGet(() ->
                    // 其他浏览器需要转为iso-8859-1编码
                    new String(originFileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1)
                );

        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", newFileName));
    }
}
