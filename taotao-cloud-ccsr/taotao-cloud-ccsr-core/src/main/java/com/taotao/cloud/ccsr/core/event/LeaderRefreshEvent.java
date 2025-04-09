package com.taotao.cloud.ccsr.core.event;

import com.alipay.sofa.jraft.entity.PeerId;
import com.taotao.cloud.ccsr.api.event.Event;


public record LeaderRefreshEvent(PeerId peerId) implements Event {
}
