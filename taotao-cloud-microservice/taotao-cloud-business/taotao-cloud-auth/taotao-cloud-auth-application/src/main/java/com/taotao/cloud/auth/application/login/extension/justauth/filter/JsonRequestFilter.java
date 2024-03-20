/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

package com.taotao.cloud.auth.application.login.extension.justauth.filter;

import static java.util.Optional.ofNullable;

import com.taotao.cloud.auth.application.login.extension.justauth.consts.SecurityConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 增加对 Json 格式的解析, 解析数据时默认使用 UTF-8 格式, 覆写了
 * <pre>
 *     JsonRequest#getParameter(String);
 *     JsonRequest#getInputStream();
 * </pre>, 添加了
 * <pre>
 *     JsonRequest#getFormMap();
 *     JsonRequest#getBody();
 * </pre><br><br>
 * 解决  Json 格式的{@code getInputStream()}被读取一次后, 不能再次读取的问题.
 *
 * @author YongWu zheng
 * @version V1.0  Created by 2020/6/9 14:01
 */
public class JsonRequestFilter extends OncePerRequestFilter {

    /**
     * Creates a new instance.
     */
    public JsonRequestFilter() {}

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        filterChain.doFilter(new JsonRequest(request), response);
    }

    @Slf4j
    public static class JsonRequest extends HttpServletRequestWrapper {

        @Getter
        private final byte[] body;

        @Getter
        private final Map<String, Object> formMap;

        JsonRequest(HttpServletRequest request) {
            super(request);
            String contentType = request.getContentType();
            String method = request.getMethod();
            boolean isPostOrPutRequest = SecurityConstants.POST_METHOD.equalsIgnoreCase(method)
                    || SecurityConstants.PUT_METHOD.equalsIgnoreCase(method);
            boolean isJsonContentType = contentType != null && contentType.contains(MediaType.APPLICATION_JSON_VALUE);
            if (isPostOrPutRequest && isJsonContentType) {
                Map<String, Object> map = null;
                byte[] bytes = null;
                try {
                    // 获取 表单 字节数据
                    //					bytes = readAllBytes(request.getInputStream());
                    //					if (bytes.length != 0) {
                    //						String jsonData = new String(bytes, StandardCharsets.UTF_8).trim();
                    //						// 转换为 map 类型, 并放入 request 域方便下次调用
                    //						//noinspection unchecked
                    //						map = MvcUtil.json2Object(jsonData, Map.class);
                    //					}
                } catch (Exception e) {
                    log.error(String.format("读取请求数据失败: %s", e.getMessage()), e);
                }
                formMap = ofNullable(map).orElse(new HashMap<>(0));
                body = bytes;
            } else {
                body = null;
                formMap = null;
            }
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            if (body == null) {
                return super.getInputStream();
            }
            return new BodyInputStream(body);
        }

        @Override
        public String getParameter(String name) {
            String parameter = super.getParameter(name);
            if (parameter == null && formMap != null) {
                return (String) formMap.get(name);
            }
            return parameter;
        }
    }

    private static class BodyInputStream extends ServletInputStream {

        private final InputStream delegate;

        public BodyInputStream(byte[] body) {
            this.delegate = new ByteArrayInputStream(body);
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int read() throws IOException {
            return this.delegate.read();
        }

        @Override
        public int read(@NonNull byte[] b, int off, int len) throws IOException {
            return this.delegate.read(b, off, len);
        }

        @Override
        public int read(@NonNull byte[] b) throws IOException {
            return this.delegate.read(b);
        }

        @Override
        public long skip(long n) throws IOException {
            return this.delegate.skip(n);
        }

        @Override
        public int available() throws IOException {
            return this.delegate.available();
        }

        @Override
        public void close() throws IOException {
            this.delegate.close();
        }

        @Override
        public synchronized void mark(int readlimit) {
            this.delegate.mark(readlimit);
        }

        @Override
        public synchronized void reset() throws IOException {
            this.delegate.reset();
        }

        @Override
        public boolean markSupported() {
            return this.delegate.markSupported();
        }
    }
}
