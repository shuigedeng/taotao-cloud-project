package com.taotao.cloud.workflow.biz.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 引擎模板
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月29日 上午9:18
 */
public enum FormEnum {

    //子表
    table("table"),
    //主表
    mast("mast"),
    //表单子表
    mastTable("mastTable"),

    //栅格
    row("row"),
    //折叠
    collapse("collapse"),
    //标签
    tab("tab"),
    //卡片
    card("card"),

    //分组标题
    groupTitle("groupTitle"),
    //分割线
    divider("divider"),
    //文本
    JNPFText("JNPFText"),
    //按钮
    button("button"),
    //关联表单属性
    relationFormAttr("relationFormAttr"),
    //关联表单属性
    popupAttr("popupAttr"),
    // 条形码
    BARCODE ("barcode"),
    //二维码
    QR_CODE("qrcode");


    private String message;

    FormEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    //无用的对象
    private static List<String> isNodeList=new ArrayList<String>(){{
        add(FormEnum.groupTitle.getMessage());
        add(FormEnum.divider.getMessage());
        add(FormEnum.JNPFText.getMessage());
        add(FormEnum.button.getMessage());
//        add(FormEnum.relationFormAttr.getMessage());
        add(FormEnum.BARCODE.getMessage());
        add(FormEnum.QR_CODE.getMessage());
    }};


    public static boolean isModel(String value){
        boolean isData = isNodeList.contains(value);
        return isData;
    }


}
