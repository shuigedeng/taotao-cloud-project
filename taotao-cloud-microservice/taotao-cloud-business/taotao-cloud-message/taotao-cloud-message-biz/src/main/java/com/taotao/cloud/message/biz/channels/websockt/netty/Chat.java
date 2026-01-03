package com.taotao.cloud.message.biz.channels.websockt.netty;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.*;

import java.time.LocalDateTime;

/**
 * Chat
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Data
public class Chat {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long targetUserId;

    private LocalDateTime createTime;

    private String userName;

    private String targetUserName;

    private String content;

}
