/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.common.constant;


/**
 * 文件类型常量
 */
public final class FileTypeConst {

    /**    
     *  file type const    
     */    
    private FileTypeConst(){}

    /**
     * 文件类型过滤
     */
    public static class Glob {
        private Glob(){}

        /**
         * 所有文件类型
         */
        public static final String ALL = "*.*";
    }

    /**
     * 压缩文件
     */
    public static class Compress {
        private Compress(){}

        public static final String ZIP = ".zip";
        public static final String RAR = ".rar";
        public static final String JAR = ".jar";
    }

    /**
     * 图片
     */
    public static class Image {
        private Image(){}

        public static final String PNG = ".png";
        public static final String JPG = ".jpg";
        public static final String JPEG = ".jpeg";
        public static final String GIF = ".gif";
    }



}
