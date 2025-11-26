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

package com.taotao.cloud.shortlink.biz.other.server.application.handler;

import com.csvreader.CsvWriter;
import com.github.benmanes.caffeine.cache.Cache;
import com.taotao.cloud.shortlink.biz.other.server.application.dto.UrlRequest;
import com.taotao.cloud.shortlink.biz.other.server.application.dto.UrlResponse;
import com.taotao.cloud.shortlink.biz.other.server.application.util.ConversionUtil;
import com.taotao.cloud.shortlink.biz.other.server.application.util.IdWorkerInstance;
import lombok.extern.slf4j.Slf4j;
import com.taotao.boot.common.utils.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class UrlMapHandler implements Handler {

    private static final String LONG2SHORTURL = "长链接转短连接";
    private static final String SHORT2LONGURL = "根据短链接获取长链接";

    // 缓存要生成缓存
    @Autowired
    Cache<String, String> caffeineCache;

    @Value("${shorturl.prefix}")
    private String shortUrlPrefix;

    UrlMapHandler() {
        writeCSV("./testData/shortTestUrl.csv");
    }

    @Override
    public void handle(UrlRequest urlReq, UrlResponse urlResponse) {
        log.info("urlResonse {}", urlResponse.getDescription());
        if (StringUtils.equals(urlResponse.getDescription(), LONG2SHORTURL)) {
            long2ShortUrl(urlReq, urlResponse);
        } else {
            short2LongUrl(urlReq, urlResponse);
        }
    }

    private void long2ShortUrl(UrlRequest longUrlReq, UrlResponse urlResponse) {
        // 1.先在内存中查询关键字longUrl是否存在
        log.info("{} long Url {}", longUrlReq.getRequestId(), longUrlReq.getLongUrl());
        // 要做一个长度过滤
        String shortUrl = caffeineCache.getIfPresent(longUrlReq.getLongUrl());
        if (shortUrl == null || StringUtils.isBlank(shortUrl)) {
            long id = IdWorkerInstance.INSTANCE.IdWorkerInstance().generate();
            shortUrl = ConversionUtil.encode(id, 11);
            int length = shortUrl.length();
            shortUrl = shortUrlPrefix + shortUrl.substring(length - 8, length);
            caffeineCache.put(longUrlReq.getLongUrl(), shortUrl);
            // 2.3 在存入已端域名为key,value为shortUrl为对象保存入缓存,便于做反向解析
            caffeineCache.put(shortUrl, longUrlReq.getLongUrl());
        }

        urlResponse.setRequestId(longUrlReq.getRequestId());
        urlResponse.setDescription(LONG2SHORTURL);
        urlResponse.setLongUrl(longUrlReq.getLongUrl());
        urlResponse.setShortUrl(shortUrl);
    }

    private void short2LongUrl(UrlRequest shortUrlReq, UrlResponse urlResponse) {
        log.info("{} short request {}", shortUrlReq.getRequestId(), shortUrlReq.getShortUrl());
        String longUrl = caffeineCache.getIfPresent(shortUrlReq.getShortUrl());
        if (longUrl == null || StringUtils.isBlank(longUrl)) {
            urlResponse.setLongUrl("");
            urlResponse.setErrMsg("链接失效或存在");

            return;
        }
        urlResponse.setRequestId(shortUrlReq.getRequestId());
        urlResponse.setDescription(SHORT2LONGURL);
        urlResponse.setLongUrl(longUrl);
        urlResponse.setShortUrl(shortUrlReq.getShortUrl());
    }

    public void writeCSV(String csvFilePath) {
        log.info("start write csv");
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            CsvWriter csvWriter = new CsvWriter(csvFilePath, ';', Charset.forName("UTF-8"));
                            Iterator<String> it = caffeineCache.asMap().keySet().iterator();
                            while (it.hasNext()) {
                                String[] csvContent = new String[1];
                                String t = (String) it.next();
                                csvContent[0] = t;

                                csvWriter.writeRecord(csvContent);
                            }
                            csvWriter.close();
                            log.info("<--------CSV文件写入成功-------->");
                        } catch (IOException e) {
                            log.error("write csv failed");
                        }
                    }
                    // 压测以10分钟中单位，避免压测时写入数据带来性能影响
                },
                12,
                13,
                TimeUnit.MINUTES);
    }
}
