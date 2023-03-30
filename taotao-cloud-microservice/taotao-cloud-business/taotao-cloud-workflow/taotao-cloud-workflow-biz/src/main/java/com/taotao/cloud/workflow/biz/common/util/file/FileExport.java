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

package com.taotao.cloud.workflow.biz.common.util.file;

/** 导入导出工厂类 */
public interface FileExport {

    /**
     * 导出
     *
     * @param obj 要转成Json的类
     * @param filePath 写入位置
     * @param fileName 文件名
     * @param tableName 表明
     * @return
     */
    DownloadVO exportFile(Object obj, String filePath, String fileName, String tableName);
}
