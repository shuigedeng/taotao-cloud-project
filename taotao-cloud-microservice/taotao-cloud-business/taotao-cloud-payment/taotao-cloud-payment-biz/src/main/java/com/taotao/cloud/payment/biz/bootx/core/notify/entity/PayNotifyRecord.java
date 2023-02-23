package com.taotao.cloud.payment.biz.bootx.core.notify.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.payment.biz.bootx.core.notify.convert.PayNotifyConvert;
import com.taotao.cloud.payment.biz.bootx.dto.notify.PayNotifyRecordDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**   
* 回调记录
* @author xxm  
* @date 2021/6/22 
*/
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_pay_notify_record")
public class PayNotifyRecord extends MpBaseEntity implements EntityBaseFunction<PayNotifyRecordDto> {

    /** 支付记录id */
    private Long paymentId;
    /**
     * 支付通道
     * @see PayChannelCode
     */
    private int payChannel;
    /** 通知消息 */
    private String notifyInfo;
    /**
     * 处理状态
     * @see PayStatusCode#NOTIFY_PROCESS_SUCCESS
     */
    private int status;
    /** 提示信息 */
    private String msg;
    /** 回调时间 */
    private LocalDateTime notifyTime;

    @Override
    public PayNotifyRecordDto toDto() {
        return PayNotifyConvert.CONVERT.convert(this);
    }
}
