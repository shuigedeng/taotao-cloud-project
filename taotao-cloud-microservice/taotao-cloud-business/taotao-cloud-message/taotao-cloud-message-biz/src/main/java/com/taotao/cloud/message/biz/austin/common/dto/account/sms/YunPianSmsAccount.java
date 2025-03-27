package com.taotao.cloud.message.biz.austin.common.dto.account.sms;

import lombok.*;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 云片账号信息
 * <p>
 * 账号参数示例：
 * {"url":"https://sms.yunpian.com/v2/sms/tpl_batch_send.json","apikey":"caffff8234234231b5cd7","tpl_id":"523333332","supplierId":20,"supplierName":"云片","scriptName":"YunPianSmsScript"}
 *
 * @author shuigedeng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class YunPianSmsAccount extends SmsAccount {

    /**
     * apikey
     */
    private String apikey;
    /**
     * tplId
     */
    private String tplId;

    /**
     * api相关
     */
    private String url;

    /**
     * 重写equals方法
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        YunPianSmsAccount that = (YunPianSmsAccount) o;
        return apikey.equals(that.apikey) &&
                tplId.equals(that.tplId) &&
                url.equals(that.url);
    }

    /**
     * 重写hashCode方法
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), apikey, tplId, url);
    }
}
