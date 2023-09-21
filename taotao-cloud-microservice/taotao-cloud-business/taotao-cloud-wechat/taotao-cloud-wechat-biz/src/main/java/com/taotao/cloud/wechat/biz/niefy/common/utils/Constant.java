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

package com.taotao.cloud.wechat.biz.niefy.common.utils;

/**
 * 常量
 *
 * @author Mark sunlightcs@gmail.com
 */
public class Constant {
    /** 超级管理员ID */
    public static final int SUPER_ADMIN = 1;
    /** 当前页码 */
    public static final String PAGE = "page";
    /** 每页显示记录数 */
    public static final String LIMIT = "limit";
    /** 排序字段 */
    public static final String ORDER_FIELD = "sidx";
    /** 排序方式 */
    public static final String ORDER = "order";
    /** 升序 */
    public static final String ASC = "asc";

    /** 请求header中的微信用户端源链接参数 */
    public static final String WX_CLIENT_HREF_HEADER = "wx-client-href";

    /**
     * 菜单类型
     *
     * @author chenshun
     * @email sunlightcs@gmail.com
     * @since 2016年11月15日 下午1:24:29
     */
    public enum MenuType {
        /** 目录 */
        CATALOG(0),
        /** 菜单 */
        MENU(1),
        /** 按钮 */
        BUTTON(2);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 定时任务状态
     *
     * @author chenshun
     * @email sunlightcs@gmail.com
     * @since 2016年12月3日 上午12:07:22
     */
    public enum ScheduleStatus {
        /** 正常 */
        NORMAL(0),
        /** 暂停 */
        PAUSE(1);

        private int value;

        ScheduleStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /** 云服务商 */
    public enum CloudService {
        /** 七牛云 */
        QINIU(1),
        /** 阿里云 */
        ALIYUN(2),
        /** 腾讯云 */
        QCLOUD(3);

        private int value;

        CloudService(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
