package com.taotao.cloud.promotion.api.dto.search;


import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 砍价活动商品查询通用类
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class KanjiaActivityGoodsParams extends BasePromotionsSearchParams implements Serializable {

    private static final long serialVersionUID = 1344104067705714289L;

    @Schema(description =  "活动商品")
    private String goodsName;

    @Schema(description =  "skuId")
    private String skuId;

    @Override
    public <T> QueryWrapper<T> queryWrapper() {
        QueryWrapper<T> queryWrapper = super.queryWrapper();

        if (CharSequenceUtil.isNotEmpty(goodsName)) {
            queryWrapper.like("goods_name", goodsName);
        }
        //if (UserContext.getCurrentUser() != null && UserContext.getCurrentUser().getRole().equals(UserEnums.MEMBER)) {
        //    queryWrapper.gt("stock", 0);
        //}
        queryWrapper.eq("delete_flag", false);
        return queryWrapper;
    }

}
