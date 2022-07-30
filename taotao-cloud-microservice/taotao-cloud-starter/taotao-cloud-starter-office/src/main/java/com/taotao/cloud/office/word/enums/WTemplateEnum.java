//package com.taotao.cloud.doc.word.enums;
//
//
//public enum WTemplateEnum {
//    /**
//     * 表格
//     */
//    TABLE("#"),
//    /**
//     * string
//     */
//    STRING("\u0000"),
//    /**
//     * 图片
//     */
//    PICTURE("@"),
//    /**
//     * 数字列
//     */
//    NUMBERIC("*");
//
//    private String value;
//
//    private WTemplateEnum(String value) {
//        this.value = value;
//    }
//
//    public String getValue() {
//        return value;
//    }
//
//    public static WTemplateEnum getByValue(String value) {
//        WTemplateEnum[] wTemplateEnums = values();
//        for (WTemplateEnum wTemplateEnum : wTemplateEnums) {
//            if (wTemplateEnum.value.equals(value)) {
//                return wTemplateEnum;
//            }
//        }
//        return null;
//    }
//
//
//}
