package com.taotao.cloud.promotion.biz.entity;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;


/**
 * 砍价活动商品实体类
 *
 * @author qiuqiu
 * @date 2020-7-1 10:44 上午
 */
@Entity
@Table(name = KanjiaActivityLog.TABLE_NAME)
@TableName(KanjiaActivityLog.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = KanjiaActivityLog.TABLE_NAME, comment = "砍价活动日志对象")
public class KanjiaActivityLog extends BaseSuperEntity<KanjiaActivityLog, Long> {

	public static final String TABLE_NAME = "li_kanjia_activity_log";

    @ApiModelProperty(value = "砍价活动参与记录id")
    private String kanjiaActivityId;

    @ApiModelProperty(value = "砍价会员id")
    private String kanjiaMemberId;

    @ApiModelProperty(value = "砍价会员名称")
    private String kanjiaMemberName;

    @ApiModelProperty(value = "砍价会员头像")
    private String kanjiaMemberFace;

    @ApiModelProperty(value = "砍价金额")
    private Double kanjiaPrice;

    @ApiModelProperty(value = "剩余购买金额")
    private Double surplusPrice;


}
