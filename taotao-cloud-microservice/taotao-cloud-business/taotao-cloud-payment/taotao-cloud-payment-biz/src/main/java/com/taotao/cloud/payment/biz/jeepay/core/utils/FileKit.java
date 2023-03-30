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

package com.taotao.cloud.payment.biz.jeepay.core.utils;

import com.taotao.cloud.payment.biz.jeepay.core.constants.CS;
import com.taotao.cloud.payment.biz.jeepay.core.exception.BizException;

/*
 * 文件工具类
 *
 * @author terrfly
 * @site https://www.jeequan.com
 * @date 2021/6/8 16:50
 */
public class FileKit {

    /**
     * 获取文件的后缀名
     *
     * @param appendDot 是否拼接.
     * @return
     */
    public static String getFileSuffix(String fullFileName, boolean appendDot) {
        if (fullFileName == null || fullFileName.indexOf(".") < 0 || fullFileName.length() <= 1) {
            return "";
        }
        return (appendDot ? "." : "") + fullFileName.substring(fullFileName.lastIndexOf(".") + 1);
    }

    /** 获取有效的图片格式， 返回null： 不支持的图片类型 * */
    public static String getImgSuffix(String filePath) {

        String suffix = getFileSuffix(filePath, false).toLowerCase();
        if (CS.ALLOW_UPLOAD_IMG_SUFFIX.contains(suffix)) {
            return suffix;
        }
        throw new BizException("不支持的图片类型");
    }
}
