/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.workflow.biz.common.model;

import java.util.ArrayList;
import java.util.List;

/** 引擎模板 */
public enum FormEnum {

    // 子表
    table("table"),
    // 主表
    mast("mast"),
    // 表单子表
    mastTable("mastTable"),

    // 栅格
    row("row"),
    // 折叠
    collapse("collapse"),
    // 标签
    tab("tab"),
    // 卡片
    card("card"),

    // 分组标题
    groupTitle("groupTitle"),
    // 分割线
    divider("divider"),
    // 文本
    WORKFLOWText("WORKFLOWText"),
    // 按钮
    button("button"),
    // 关联表单属性
    relationFormAttr("relationFormAttr"),
    // 关联表单属性
    popupAttr("popupAttr"),
    // 条形码
    BARCODE("barcode"),
    // 二维码
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

    // 无用的对象
    private static List<String> isNodeList = new ArrayList<String>() {
        {
            add(FormEnum.groupTitle.getMessage());
            add(FormEnum.divider.getMessage());
            add(FormEnum.WORKFLOWText.getMessage());
            add(FormEnum.button.getMessage());
            //        add(FormEnum.relationFormAttr.getMessage());
            add(FormEnum.BARCODE.getMessage());
            add(FormEnum.QR_CODE.getMessage());
        }
    };

    public static boolean isModel(String value) {
        boolean isData = isNodeList.contains(value);
        return isData;
    }
}
