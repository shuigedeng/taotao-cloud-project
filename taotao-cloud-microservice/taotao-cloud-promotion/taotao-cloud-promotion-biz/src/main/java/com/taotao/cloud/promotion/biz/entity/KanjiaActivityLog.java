package com.taotao.cloud.promotion.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 砍价活动商品实体类
 *
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = KanjiaActivityLog.TABLE_NAME)
@TableName(KanjiaActivityLog.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = KanjiaActivityLog.TABLE_NAME, comment = "砍价活动日志对象")
public class KanjiaActivityLog extends BaseSuperEntity<KanjiaActivityLog, Long> {

	public static final String TABLE_NAME = "li_kanjia_activity_log";

    @Schema(description =  "砍价活动参与记录id")
    private String kanjiaActivityId;

    @Schema(description =  "砍价会员id")
    private String kanjiaMemberId;

    @Schema(description =  "砍价会员名称")
    private String kanjiaMemberName;

    @Schema(description =  "砍价会员头像")
    private String kanjiaMemberFace;

    @Schema(description =  "砍价金额")
    private BigDecimal kanjiaPrice;

    @Schema(description =  "剩余购买金额")
    private BigDecimal surplusPrice;
}
