package com.taotao.cloud.office.easypoi.test.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * 超链接测试
 * @author JueYue
 *   2015年7月20日 下午10:35:20
 */
public class HyperLinkEntity {

    @Excel(name = "名称", isHyperlink = true)
    private String name;
    @Excel(name = "URL")
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
