package com.taotao.cloud.open.openapi.doc.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * API接口信息
 *
 * @author wanghuidong
 * 时间： 2022/6/21 21:31
 */
@Data
public class Api {

    /**
     * 开放api名称
     */
    private String openApiName;

    /**
     * 接口名
     */
    private String name;

    /**
     * 接口中文名
     */
    private String cnName;

    /**
     * 接口完整名
     */
    private String fullName;

    /**
     * 接口描述
     */
    private String describe;

    /**
     * 接口里的方法
     */
    private List<Method> methods = new ArrayList<>();
}
