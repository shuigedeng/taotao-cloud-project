package com.taotao.cloud.payment.biz.daxpay.single.service.entity.allocation.receiver;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.taotao.cloud.payment.biz.daxpay.service.bo.allocation.AllocGroupResultBo;
import com.taotao.cloud.payment.biz.daxpay.service.common.entity.MchAppBaseEntity;
import com.taotao.cloud.payment.biz.daxpay.service.convert.allocation.AllocGroupConvert;

import java.math.BigDecimal;

/**
 * 分账组
 * @author xxm
 * @since 2024/6/4
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_alloc_group")
public class AllocGroup extends MchAppBaseEntity implements ToResult<AllocGroupResultBo> {

    /** 分账组编码 */
    private String groupNo;

    /** 名称 */
    private String name;

    /** 通道 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channel;

    /** 是否为默认分账组 */
    private boolean defaultGroup;

    /** 总分账比例(百分之多少) */
    private BigDecimal totalRate;

    /** 备注 */
    private String remark;

    @Override
    public AllocGroupResultBo toResult() {
        return AllocGroupConvert.CONVERT.convert(this);
    }
}
