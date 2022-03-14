package com.taotao.cloud.promotion.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * 秒杀活动视图对象
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class SeckillVO extends Seckill {

    private static final long serialVersionUID = 2891461638257152270L;

    /**
     * @see cn.lili.modules.promotion.entity.enums.SeckillApplyStatusEnum
     */
    @Schema(description =  "报名状态")
    private String seckillApplyStatus;

    /**
     * 当前秒杀活动下所有的秒杀申请信息
     */
    private List<SeckillApply> seckillApplyList;

}
