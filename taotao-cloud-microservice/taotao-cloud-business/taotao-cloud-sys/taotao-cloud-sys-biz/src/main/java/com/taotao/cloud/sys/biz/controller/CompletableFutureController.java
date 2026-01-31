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

package com.taotao.cloud.sys.biz.controller;

import com.taotao.boot.common.utils.json.JacksonUtils;
import com.taotao.boot.common.utils.log.LogUtils;
import jakarta.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * CompletableFutureController
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@RestController
@RequestMapping("/index")
public class CompletableFutureController {

    @Resource
    private ThreadPoolExecutor asyncThreadPoolExecutor;

    @RequestMapping(
            value = "/homeIndex",
            method = {RequestMethod.POST, RequestMethod.GET})
    public String homeIndex( @RequestParam(required = false) String userId,
            @RequestParam(value = "lang") String lang ) {
        ResultData<HomeVO> result = new ResultData<>();

        // 获取Banner轮播图信息
        CompletableFuture<List<BannerVO>> future1 =
                CompletableFuture.supplyAsync(() -> this.buildBanners(userId, lang), asyncThreadPoolExecutor);
        // 获取用户message通知信息
        CompletableFuture<List<NotificationVO>> future2 =
                CompletableFuture.supplyAsync(() -> this.buildNotifications(userId, lang), asyncThreadPoolExecutor);
        // 获取用户权益信息
        CompletableFuture<List<BenefitVO>> future3 =
                CompletableFuture.supplyAsync(() -> this.buildBenefits(userId, lang), asyncThreadPoolExecutor);
        // 获取优惠券信息
        CompletableFuture<List<CouponVO>> future4 =
                CompletableFuture.supplyAsync(() -> this.buildCoupons(userId), asyncThreadPoolExecutor);

        CompletableFuture<Void> allOfFuture = CompletableFuture.allOf(future1, future2, future3, future4);

        HomeVO finalHomeVO = new HomeVO();
        CompletableFuture<HomeVO> resultFuture = allOfFuture
                .thenApply(v -> {
                    try {
                        finalHomeVO.setBanners(future1.get());
                        finalHomeVO.setNotifications(future2.get());
                        finalHomeVO.setBenefits(future3.get());
                        finalHomeVO.setCoupons(future4.get());
                        return finalHomeVO;
                    } catch (Exception e) {
                        LogUtils.error("[Error] assemble homeVO data error: {}", e);
                        throw new RuntimeException(e);
                    }
                })
                .exceptionally(error -> {
                    // 通过exceptionally捕获异常，打印日志并返回默认值
                    LogUtils.error("RemoteDictService.getDictDataAsync Exception dictId =" + " {}", error);
                    return null;
                });

        HomeVO homeVO = resultFuture.join();
        result.setData(homeVO);
        return JacksonUtils.toJSONString(result);
    }

    @SneakyThrows
    public List<BannerVO> buildBanners( String userId, String lang ) {
        // 模拟请求耗时0.5秒
        Thread.sleep(500);
        return new ArrayList<BannerVO>();
    }

    @SneakyThrows
    public List<NotificationVO> buildNotifications( String userId, String lang ) {
        // 模拟请求耗时0.5秒
        Thread.sleep(500);
        return new ArrayList<NotificationVO>();
    }

    @SneakyThrows
    public List<BenefitVO> buildBenefits( String userId, String lang ) {
        // 模拟请求耗时0.5秒
        Thread.sleep(500);
        return new ArrayList<BenefitVO>();
    }

    @SneakyThrows
    public List<CouponVO> buildCoupons( String userId ) {
        // 模拟请求耗时0.5秒
        Thread.sleep(500);
        return new ArrayList<>();
    }

    public static class HomeVO {

        public void setBanners( List<BannerVO> bannerVOS ) {
        }

        public void setNotifications( List<NotificationVO> notificationVOS ) {
        }

        public void setBenefits( List<BenefitVO> benefitVOS ) {
        }

        public void setCoupons( List<CouponVO> couponVOS ) {
        }
    }

    public static class NotificationVO {

    }

    public static class BenefitVO {

    }

    public static class CouponVO {

    }

    public static class BannerVO {

    }

    public static class ResultData<T> {

        public void setData( T homeVO ) {
        }
    }
}
