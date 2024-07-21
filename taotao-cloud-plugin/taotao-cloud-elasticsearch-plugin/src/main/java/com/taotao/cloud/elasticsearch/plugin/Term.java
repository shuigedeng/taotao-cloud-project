package com.taotao.cloud.elasticsearch.plugin;

import java.io.Serializable;

public class Term implements Serializable {
    //词元的起始位移
    private int offset;
    //词元的相对起始位置
    private int end;
    //词元文本
    private String text;
    //词元类型
    private String lexemeType;
}
