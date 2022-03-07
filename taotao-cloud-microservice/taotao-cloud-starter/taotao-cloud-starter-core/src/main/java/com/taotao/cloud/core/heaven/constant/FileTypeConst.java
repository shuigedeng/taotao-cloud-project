/*
 * Copyright (c)  2019. houbinbin Inc.
 * heaven All rights reserved.
 */

package com.taotao.cloud.core.heaven.constant;


/**
 * 文件类型常量
 * @author bbhou
 * @since 0.0.1
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
