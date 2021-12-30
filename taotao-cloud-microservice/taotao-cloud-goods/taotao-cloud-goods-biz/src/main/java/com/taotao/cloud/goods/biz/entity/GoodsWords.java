package com.taotao.cloud.goods.biz.entity;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 商品关键字
 *
 * 
 * @since 2020/10/15
 */
@Entity
@Table(name = GoodsWords.TABLE_NAME)
@TableName(GoodsWords.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = GoodsWords.TABLE_NAME, comment = "商品关键字")
public class GoodsWords extends BaseSuperEntity<GoodsWords, Long> {

	public static final String TABLE_NAME = "li_goods_words";


    /**
     * 商品关键字
     */
    @ApiModelProperty(value = "商品关键字")
    private String words;

    /**
     * 全拼音
     */
    @ApiModelProperty(value = "全拼音")
    private String wholeSpell;

    /**
     * 缩写
     */
    @ApiModelProperty(value = "缩写")
    private String abbreviate;

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    private String type;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;


}
