package com.taotao.cloud.goods.biz.entity;

import cn.lili.mybatis.BaseIdEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 直播间商品
 *
 * 
 * @since 2021/5/18 5:42 下午
 */
@Entity
@Table(name = StudioCommodity.TABLE_NAME)
@TableName(StudioCommodity.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = StudioCommodity.TABLE_NAME, comment = "直播间商品")
public class StudioCommodity extends BaseSuperEntity<StudioCommodity, Long> {

	public static final String TABLE_NAME = "li_studio_commodity";

    @ApiModelProperty(value = "房间ID")
    private Integer roomId;

    @ApiModelProperty(value = "商品ID")
    private Integer goodsId;

    public StudioCommodity(Integer roomId, Integer goodsId) {
        this.roomId = roomId;
        this.goodsId = goodsId;
    }
}
