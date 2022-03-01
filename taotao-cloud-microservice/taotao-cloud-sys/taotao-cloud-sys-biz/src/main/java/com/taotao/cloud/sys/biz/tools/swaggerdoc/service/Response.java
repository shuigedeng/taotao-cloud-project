package com.taotao.cloud.sys.biz.tools.swaggerdoc.service;

import java.io.Serializable;

/**
 * Created by XiuYin.Cui on 2018/1/11.
 */
public class Response implements Serializable{

    /**
     * 返回参数
     */
    private String description;

    /**
     * 参数名
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
