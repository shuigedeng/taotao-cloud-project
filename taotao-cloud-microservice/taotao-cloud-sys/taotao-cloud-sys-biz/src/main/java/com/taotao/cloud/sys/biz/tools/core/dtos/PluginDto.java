package com.taotao.cloud.sys.biz.tools.core.dtos;//package com.sanri.tools.modules.core.dtos;
//
//import lombok.Builder;
//import lombok.Data;
//import lombok.experimental.Tolerate;
//
//@Data
//@Builder
//public class PluginDto {
//    /**
//     * 模块名
//     */
//    private String module;
//    /**
//     * 插件名
//     */
//    private String name;
//
//    /**
//     * 插件作者
//     */
//    private String author;
//    /**
//     * logo
//     */
//    private String logo;
//    /**
//     * 插件标题
//     */
//    private String title;
//    /**
//     * 插件描述
//     */
//    private String desc;
//    /**
//     * 插件帮助信息
//     */
//    private String help;
//    /**
//     * 插件帮助信息文档
//     */
//    private String helpContent;
//
//    /**
//     * 环境
//     */
//    private String envs;
//    /**
//     * 依赖项
//     */
//    private String dependencies;
//
//    @Tolerate
//    public PluginDto() {
//    }
//
//    /**
//     * 模块唯一主键
//     * @return
//     */
//    public String key(){
//        return module+":"+name;
//    }
//}
