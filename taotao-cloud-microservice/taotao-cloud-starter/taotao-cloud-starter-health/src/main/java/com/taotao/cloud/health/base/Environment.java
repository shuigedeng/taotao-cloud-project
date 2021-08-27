package com.taotao.cloud.health.base;

/**
 * @author: chejiangyi
 * @version: 2019-05-27 13:44
 **/
public enum Environment {
	//开发环境
    dev("开发"),
    test("测试"),
    //预生产
    pre("预生产"),
    //生产环境
    prd("生产");

    private String name;
    Environment(String name)
    {
        this.name = name;
    }
}
