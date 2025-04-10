package com.taotao.cloud.payment.biz.daxpay.single.service.entity.notice.notify;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import com.taotao.cloud.payment.biz.daxpay.service.common.entity.MchAppRecordEntity;
import com.taotao.cloud.payment.biz.daxpay.service.convert.notice.MerchantNotifyConvert;
import com.taotao.cloud.payment.biz.daxpay.service.enums.NoticeSendTypeEnum;
import com.taotao.cloud.payment.biz.daxpay.service.result.notice.notify.MerchantNotifyRecordResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.*;
import lombok.EqualsAndHashCode;
import lombok.experimental.*;

/**
 * 客户订阅通知发送记录
 * @author xxm
 * @since 2024/7/30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_merchant_notify_record")
public class MerchantNotifyRecord extends MchAppRecordEntity implements ToResult<MerchantNotifyRecordResult> {

    /** 任务ID */
    private Long taskId;

    /** 请求次数 */
    private Integer reqCount;

    /** 发送是否成功 */
    private boolean success;

    /**
     * 发送类型, 自动发送, 手动发送
     * @see NoticeSendTypeEnum
     */
    private String sendType;

    /** 错误码 */
    private String errorCode;

    /** 错误信息 */
    private String errorMsg;

    /**
     * 转换
     */
    @Override
    public MerchantNotifyRecordResult toResult() {
        return MerchantNotifyConvert.CONVERT.toResult(this);
    }
}
