package com.taotao.cloud.wechat.biz.niefy.modules.wx.form;

import com.github.niefy.common.utils.Json;
import lombok.Data;

@Data
public class MaterialFileDeleteForm {
    String mediaId;

    @Override
    public String toString() {
        return Json.toJsonString(this);
    }
}
