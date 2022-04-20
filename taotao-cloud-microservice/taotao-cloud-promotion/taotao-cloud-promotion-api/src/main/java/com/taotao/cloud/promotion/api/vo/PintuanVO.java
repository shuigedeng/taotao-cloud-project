package com.taotao.cloud.promotion.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * 拼团视图对象
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PintuanVO extends PintuanBaseVO {

    private static final long serialVersionUID = 218582640653676201L;

    private List<PromotionGoodsVO> promotionGoodsList;
}
