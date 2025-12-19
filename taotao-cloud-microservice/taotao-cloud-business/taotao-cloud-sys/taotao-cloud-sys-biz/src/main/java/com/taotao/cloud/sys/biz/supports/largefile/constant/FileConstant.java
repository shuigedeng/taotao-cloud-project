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

package com.taotao.cloud.sys.biz.supports.largefile.constant;

/**
 * FileConstant
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class FileConstant {

    /**
     * 保存文件所在路径的key，eg.FILE_MD5:1243jkalsjflkwaejklgjawe
     */
    public static final String FILE_MD5_KEY = "FILE_MD5:";
    /**
     * 保存上传文件的状态
     */
    public static final String FILE_UPLOAD_STATUS = "FILE_UPLOAD_STATUS";

    public static final String TYPE_FOLDER = "folder";

    public static final String MODE_ALL = "-rwx rwx rwx(0777)";

    public static final String FILE_SEPARATORCHAR = "/";

    public static final String FILE_ATOMIC_STATUS = "FILE_ATOMIC_STATUS:";

    public static final long EXPIRE = 10;

    public static final String ROOT_PATH_ID = "1";
}
