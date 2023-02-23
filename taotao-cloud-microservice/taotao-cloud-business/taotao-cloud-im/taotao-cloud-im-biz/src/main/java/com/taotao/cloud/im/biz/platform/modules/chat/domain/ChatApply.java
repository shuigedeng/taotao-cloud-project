package com.taotao.cloud.im.biz.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.chat.enums.ApplySourceEnum;
import com.platform.modules.chat.enums.ApplyStatusEnum;
import com.platform.modules.chat.enums.ApplyTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 好友申请表实体类
 * q3z3
 * </p>
 */
@Data
@TableName("chat_apply")
@Accessors(chain = true) // 链式调用
public class ChatApply extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 发起id
     */
    private Long fromId;
    /**
     * 接收id
     */
    private Long toId;
    /**
     * 目标id
     */
    private Long targetId;
    /**
     * 申请类型1好友2群组
     */
    private ApplyTypeEnum applyType;
    /**
     * 申请状态0无1同意2拒绝
     */
    private ApplyStatusEnum applyStatus;
    /**
     * 申请来源
     */
    private ApplySourceEnum applySource;
    /**
     * 理由
     */
    private String reason;
    /**
     * 申请时间
     */
    private Date createTime;

}
