package com.taotao.cloud.promotion.biz.entity;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.taotao.cloud.promotion.api.vo.SeckillVO;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 秒杀活动实体类
 *
 * 
 */
@Data
@Entity
@Table(name = Seckill.TABLE_NAME)
@TableName(Seckill.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Seckill.TABLE_NAME, comment = "秒杀活动实体类")
public class Seckill extends BaseSuperEntity<Seckill, Long> {

	public static final String TABLE_NAME = "li_seckill";

    @NotNull(message = "请填写报名截止时间")
    @Schema(description =  "报名截至时间", required = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyEndTime;

    @Schema(description =  "申请规则")
    private String seckillRule;

    @Schema(description =  "开启几点场 例如：6，8，12")
    @NotNull(message = "活动时间段不能为空")
    private String hours;

    /**
     * 已参与此活动的商家id集合
     */
    @Schema(description =  "商家id集合以逗号分隔")
    private String storeIds;

    @Schema(description =  "商品数量")
    private Integer goodsNum;

    public Seckill(int day, String hours, String seckillRule) {
        //默认创建*天后的秒杀活动
        DateTime dateTime = DateUtil.beginOfDay(DateUtil.offset(new DateTime(), DateField.DAY_OF_YEAR, day));
        this.applyEndTime = dateTime;
        this.hours = hours;
        this.seckillRule = seckillRule;
        this.goodsNum = 0;
        //BasePromotion
        this.setStoreName("platform");
        this.setStoreId("platform");
        this.setPromotionName(DateUtil.formatDate(dateTime) + " 秒杀活动");
        this.setStartTime(dateTime);
        this.setEndTime(DateUtil.endOfDay(dateTime));
    }

    public Seckill(SeckillVO seckillVO) {
        BeanUtils.copyProperties(seckillVO, this);
    }
}
