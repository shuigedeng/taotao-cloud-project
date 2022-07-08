package com.taotao.cloud.auth.biz.authentication.oauth2.qq.other;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 由于与QQ接口的交互上，响应类型都为text/html的形式，且RestTemplate没有默认支持该解析模型，所以应当自行添加。
 * 主要是两类，一类text/html转普通文本，一类则是text/html转JSON对象。
 */
public class TextHtmlHttpMessageConverter extends AbstractHttpMessageConverter {


    public TextHtmlHttpMessageConverter() {
        super(StandardCharsets.UTF_8, new MediaType[]{MediaType.TEXT_HTML});
    }

    @Override
    protected boolean supports(Class clazz) {
        return String.class == clazz;
    }

    @Override
    protected Object readInternal(Class aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        Charset charset = this.getContentTypeCharset(httpInputMessage.getHeaders().getContentType());
        return StreamUtils.copyToString(httpInputMessage.getBody(), charset);
    }

    @Override
    protected void writeInternal(Object o, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
    }

    private Charset getContentTypeCharset(MediaType contentType) {
        return contentType != null && contentType.getCharset() != null ? contentType.getCharset() : this.getDefaultCharset();
    }

}
