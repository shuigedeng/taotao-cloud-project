package com.taotao.cloud.workflow.biz.engine.model.flowengine.shuntjson.childnode;

import lombok.Data;

/**
 * 解析引擎
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 9:10
 */
@Data
public class ProperCond {
    private String fieldName;
    private String symbolName;
    private String filedValue;
    private String logicName;
    private String field;
    private String symbol;
    private String logic;
}
