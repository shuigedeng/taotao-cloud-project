package com.taotao.cloud.promotion.api.vo;

import cn.lili.modules.member.entity.dos.Member;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 拼图会员视图对象
 *
 * 
 * @since 2021/3/3
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PintuanMemberVO {

    @Schema(description =  "会员编号")
    private String memberId;

    @Schema(description =  "会员用户名")
    private String memberName;

    @Schema(description =  "会员头像")
    private String face;

    @Schema(description =  "昵称")
    private String nickName;

    @Schema(description =  "参团订单编号")
    private String orderSn;

    @Schema(description =  "已参团人数")
    private long groupedNum;

    @Schema(description =  "待参团人数")
    private long toBeGroupedNum;

    @Schema(description =  "成团人数")
    private long groupNum;

    public PintuanMemberVO(Member member) {
        this.memberId = member.getId();
        this.memberName = member.getUsername();
        this.face = member.getFace();
        this.nickName = member.getNickName();
    }
}
