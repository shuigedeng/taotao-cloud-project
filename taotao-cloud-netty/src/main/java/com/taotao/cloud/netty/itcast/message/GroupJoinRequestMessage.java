package com.taotao.cloud.netty.itcast.message;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class GroupJoinRequestMessage extends Message {
    private String groupName;

    private String username;

    public GroupJoinRequestMessage(String username, String groupName) {
        this.groupName = groupName;
        this.username = username;
    }

    @Override
    public int getMessageType() {
        return GroupJoinRequestMessage;
    }
}
