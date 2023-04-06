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

package com.taotao.cloud.media.biz.opencv.common.utils;

import org.springframework.web.context.ContextLoader;

/** 常量 创建者 Songer 创建时间 2018年3月09日 */
public class Constants {
    public static final String CURRENT_USER = "UserInfo";
    public static final String WECHAT_USER = "weChatUserInfo";
    public static final String REFERENCE_CODE = "referenceCode";

    public static final String SUCCESS = "success";
    public static final String ERROR = "error";

    public static final String SF_FILE_SEPARATOR = System.getProperty("file.separator"); // 文件分隔符
    public static final String SF_LINE_SEPARATOR = System.getProperty("line.separator"); // 行分隔符
    public static final String SF_PATH_SEPARATOR = System.getProperty("path.separator"); // 路径分隔符

    public static final String PATH =
            ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/");
    /** 文件 */
    public static final String SOURCE_IMAGE_PATH = Constants.SF_FILE_SEPARATOR
            + "statics"
            + Constants.SF_FILE_SEPARATOR
            + "sourceimage"
            + Constants.SF_FILE_SEPARATOR; // 图片原地址

    public static final String DEST_IMAGE_PATH = Constants.SF_FILE_SEPARATOR
            + "statics"
            + Constants.SF_FILE_SEPARATOR
            + "destimage"
            + Constants.SF_FILE_SEPARATOR; // 图片生成地址

    /** 返回参数规范 */
    /** 区分类型 1 -- 无错误，Code重复 */
    public static final String CODE_DUPLICATE = "1";
    /** 区分类型 2 -- 无错误，名称重复 */
    public static final String NAME_DUPLICATE = "2";
    /** 区分类型 3 -- 数量超出 */
    public static final String NUMBER_OVER = "3";
    /** 区分类型 0 -- 无错误，程序正常执行 */
    public static final String NO_ERROR = "0";
    /** 区分类型 -1 -- 无错误，返回结果为空 */
    public static final String NULL_POINTER = "-1";
    /** 区分类型 -2 -- 错误，参数不正确 */
    public static final String INCORRECT_PARAMETER = "-2";
    /** 区分类型 -3 -- 错误，程序执行错误 */
    public static final String PROGRAM_EXECUTION_ERROR = "-3";
    /** 区分类型 -5 -- 错误，数据已删除 */
    public static final String DATA_DELETED = "-5";
    /** 区分类型 -6 -- 错误，参数不一致（验证码） */
    public static final String DATA_NOT_SAME = "-6";
    /** json文件缺失 */
    public static final String NO_JSON_FILE = "-7";

    /** 分页中可能用到的常量 */
    public static final Integer PAGE_SIZE = 10; // 一页共有十条内容
}
